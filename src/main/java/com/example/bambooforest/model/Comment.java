package com.example.bambooforest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long commentNo;
    private Long postNo;
    private Long userNo;
    private Long parentCommentNo; // 부모 번호
    private String content;
    private LocalDateTime createdAt;
    private String status;

    // 추가 필드 (Join 결과 및 계층 구조용)
    private String writerNickname;
    private String writerUsername;
    private List<Comment> replies = new ArrayList<>(); // 대댓글들을 담을 바구니


    // 🔍 이 필드들이 추가되어 있는지 확인하세요!
    private int likeCount;    // 댓글 좋아요 개수
    private boolean isLiked;  // 현재 로그인한 사용자의 좋아요 여부
}