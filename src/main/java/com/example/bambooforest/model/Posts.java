package com.example.bambooforest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    private Long postNo;                // post_no -> postNo
    private Long userNo;                // user_no -> userNo
    private String title;
    private String category;
    private String content;
    private String imagePath;           // image_path -> imagePath
    private String imageOriginalName;   // image_original_name -> imageOriginalName
    private int viewCount;              // view_count -> viewCount
    private LocalDateTime createdAt;    // created_at -> createdAt
    private LocalDateTime updatedAt;    // updated_at -> updatedAt
    private String status;
    private String writerNickname;

    private int likeCount;              // 추가: 좋아요 총 개수를 담을 변수
}