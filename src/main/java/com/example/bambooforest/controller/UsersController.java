package com.example.bambooforest.controller;

import com.example.bambooforest.dto.SignupRequest;
import com.example.bambooforest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor // 서비스 자동 주입을 위해 필요합니다
public class UsersController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseBody // JSON으로 응답하기 위해 필수!
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok().body(Map.of("message", "SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/username")
    @ResponseBody
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(Map.of("available", available));
    }
    @GetMapping("/nickname")
    @ResponseBody
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean available = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(Map.of("available", available));
    }



}