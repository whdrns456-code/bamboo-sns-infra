package com.example.bambooforest.controller;

import com.example.bambooforest.model.Comment;
import com.example.bambooforest.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final BoardService boardService;

    @PostMapping("/save")
    public ResponseEntity<?> saveComment(@RequestBody Comment comment, Authentication authentication) {
        System.out.println("DEBUG: PostNo = " + comment.getPostNo());
        System.out.println("DEBUG: Content = " + comment.getContent());
        System.out.println("DEBUG: User = " + authentication.getName());

        // 1. 보안 체크: 로그인하지 않은 사용자는 차단
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("AUTH_REQUIRED");
        }

        try {
            // 2. 작성자 정보 설정 (SecurityContext에서 가져온 username)
            comment.setWriterUsername(authentication.getName());

            // 3. 서비스 호출 (이미 구현된 saveComment 메서드 활용)
            // parentCommentNo가 null이면 일반 댓글, 값이 있으면 대댓글로 DB에 들어갑니다.
            boardService.saveComment(comment);

            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL: " + e.getMessage());
        }
    }
    @PostMapping("/like/{commentNo}")
    public ResponseEntity<?> toggleCommentLike(@PathVariable Long commentNo, Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(403).build();

        Map<String, Object> result = boardService.toggleCommentLike(commentNo, authentication.getName());
        return ResponseEntity.ok(result);
    }
}
