package com.example.bambooforest.service;

import com.example.bambooforest.dto.SignupRequest;
import com.example.bambooforest.mapper.UserMapper;
import com.example.bambooforest.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(SignupRequest request) {
        // 1. 비밀번호 일치 여부 검증 (자바스크립트에서 했어도 서버에서 한 번 더!)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("ACCESS_KEYS_DO_NOT_MATCH");
        }

        // 2. 아이디 중복 체크
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("USER_ID_ALREADY_EXISTS");
        }

        // 3. 비밀번호 암호화 (BCrypt)
        //
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. DB 저장을 위한 User 객체 생성 (Lombok @Builder 사용 가정)
        // 만약 Builder가 없다면 new User(...) 또는 Setter를 사용하세요.
        Users user = Users.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .status("Y") // 기본 활성화 상태
                .build();

        // 5. MyBatis를 통한 DB INSERT
        int result = userMapper.insertUser(user);

        if (result <= 0) {
            throw new RuntimeException("REGISTRATION_PROTOCOL_FAILED");
        }
    }

    public boolean isNicknameAvailable(String nickname) {
        return userMapper.findByNickname(nickname) == null;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.findByUsername(username) == null;
    }
}