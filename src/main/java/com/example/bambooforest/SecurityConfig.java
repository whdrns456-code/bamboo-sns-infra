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
                // 1. 정적 리소스(CSS, JS, 이미지) 및 로그인/회원가입 페이지 및 검증 검증 기능
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        //
                        .requestMatchers("/", "/login", "/signup" ,"/username", "/nickname").permitAll()

                        .anyRequest().authenticated()
                )
                // 2. 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        // 성공 시: JSON => Redirect URL
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().print("/main"); // JS에서 window.location.href로 사용
                            response.getWriter().flush();
                        })
                        // 실패 시: 401 Unauthorized 상태 코드
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().print("INVALID_CREDENTIALS");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}