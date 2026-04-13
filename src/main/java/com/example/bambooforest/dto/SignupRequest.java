package com.example.bambooforest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SignupRequest {
    private String username;
    private String password;
    private String confirmPassword; // 비밀번호 확인용
    private String nickname;
}