package com.example.bambooforest.controller;

import com.example.bambooforest.dto.PostRequest;
import com.example.bambooforest.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    @PostMapping("/board/save")
    public ResponseEntity<?> write(@ModelAttribute PostRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            boardService.savePost(request, username);
            return ResponseEntity.ok().body("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }
    @GetMapping("/root-notices")
    public ResponseEntity<Map<String, Object>> getRootNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Map<String, Object> result = boardService.getRootNotices(page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/board/like/{postNo}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postNo, Authentication authentication) {
        String username = authentication.getName();
        // 1. 이미 좋아요를 눌렀는지 확인
        boolean exists = boardService.isLiked(postNo, username);

        if (exists) {
            boardService.removeLike(postNo, username); // 취소
        } else {
            boardService.addLike(postNo, username);    // 추가
        }

        // 2. 최신 좋아요 개수 반환
        int updatedCount = boardService.getLikeCount(postNo);

        return ResponseEntity.ok(Map.of(
                "isLiked", !exists,
                "likeCount", updatedCount
        ));
    }

}
