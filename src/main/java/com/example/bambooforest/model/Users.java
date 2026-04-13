package com.example.bambooforest.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    private int userNo;
    private String username;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;
    private String status;
}