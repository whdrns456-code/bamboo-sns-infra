package com.example.bambooforest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String category;
    private String content;
    private MultipartFile imageFile; //HTML의 name="imageFile"과 매핑
}

