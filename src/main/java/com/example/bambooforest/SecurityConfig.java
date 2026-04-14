package com.example.bambooforest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/", "/login", "/signup" ,"/username", "/nickname").permitAll()
                        .anyRequest().authenticated()
                )
                // 2. 폼 로그인 설정 (핵심 수정 구간)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")

                        // [수정] JS 핸들러 대신 표준 리다이렉트를 사용합니다.
                        // WAS가 302 Found 응답을 던져야 WAF가 경로를 https로 정확히 보정해줍니다.
                        .defaultSuccessUrl("/main", true)

                        // 실패 시에도 깔끔하게 로그인 페이지로 리다이렉트
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // 3. 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // 4. 보안 설정
                .csrf(csrf -> csrf.disable()); // 테스트 단계에서는 disable 유지

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}