package com.example.bambooforest.service;

import com.example.bambooforest.dto.PostRequest;
import com.example.bambooforest.mapper.CommentMapper;
import com.example.bambooforest.mapper.PostMapper;
import com.example.bambooforest.model.Comment;
import com.example.bambooforest.model.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public void savePost(PostRequest request, String username) {
        String imagePath = null;
        String originalName = null;

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            originalName = request.getImageFile().getOriginalFilename();

            // 1. 확장자 추출 (안전하게 처리)
            String extension = "";
            if (originalName != null && originalName.lastIndexOf(".") != -1) {
                extension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
            } else {
                throw new RuntimeException("INVALID_FILE_NAME: No extension found");
            }

            // 2. 허용 확장자 체크
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
            if (!allowedExtensions.contains(extension)) {
                throw new RuntimeException("INVALID_FILE_EXTENSION: " + extension);
            }

            // 3. 파일 저장용 이름 생성 (UUID + 확장자만 사용)
            String savedName = UUID.randomUUID().toString() + "." + extension;

            if (!allowedExtensions.contains(extension)) {
                // 허용되지 않은 확장자일 경우에는 즉시 차단
                throw new RuntimeException("INVALID_FILE_EXTENSION: " + extension);
            }

            try {
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();
                request.getImageFile().transferTo(new File(uploadDir, savedName));
                imagePath = "/uploads/" + savedName;
            } catch (IOException e) {
                throw new RuntimeException("FILE_SYSTEM_ERROR", e);
            }
        }

        Posts post = new Posts();
        post.setTitle(request.getTitle());
        post.setCategory(request.getCategory());
        post.setContent(request.getContent());
        post.setImagePath(imagePath);
        post.setImageOriginalName(originalName);

        int result = postMapper.insertPost(post, username);

        if (result <= 0) {
            throw new RuntimeException("DATABASE_INJECTION_FAILED");
        }
    }

    public List<Posts> findAllPosts() {
        return postMapper.findAllWithNickname();
    }

    public List<Posts> findMorePosts(int lastPostNo, int size) {
        return postMapper.findMorePosts(lastPostNo, size);
    }
    public Map<String, Object> getRootNotices(int page, int size) {
        int offset = page * size;
        List<Posts> posts = postMapper.findRootAnnouncements(size, offset);
        int totalCount = postMapper.countRootPosts();
        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);
        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        return response;
    }
    // 조회수 증가
    @Transactional
    public void plusViewCount(Long postNo) {
        postMapper.updateViewCount(postNo);
    }

    // 단건 조회
    public Posts findPostByNo(Long postNo) {
        return postMapper.selectPostByNo(postNo);
    }

    // 1. 좋아요 여부 확인 (있으면 true, 없으면 false)
    public boolean isLiked(Long postNo, String username) {
        int count = postMapper.checkLikeExists(postNo, username);
        return count > 0;
    }

    // 2. 좋아요 취소
    @Transactional
    public void removeLike(Long postNo, String username) {
        postMapper.deleteLike(postNo, username);
    }

    // 3. 좋아요 추가
    @Transactional
    public void addLike(Long postNo, String username) {
        postMapper.insertLike(postNo, username);
    }

    // 4. 현재 게시글의 총 좋아요 수 조회 (프런트 갱신용)
    public int getLikeCount(Long postNo) {
        return postMapper.countLikesByPostNo(postNo);
    }

    public void saveComment(Comment comment) {
        commentMapper.saveComment(comment);
    }

    public List<Comment> getComments(Long postNo) {
        // 1. DB에서 해당 게시글의 모든 댓글(원본 리스트)을 가져옵니다.
        List<Comment> allComments = commentMapper.findCommentsByPostNo(postNo);

        List<Comment> rootComments = new ArrayList<>();
        Map<Long, Comment> map = new HashMap<>();

        // 2. 효율적인 조회를 위해 모든 댓글을 Map에 담습니다 (Key: commentNo)
        for (Comment comment : allComments) {
            map.put(comment.getCommentNo(), comment);
        }

        // 3. 계층 구조 조립
        for (Comment comment : allComments) {
            if (comment.getParentCommentNo() == null || comment.getParentCommentNo() == 0) {
                // 부모가 없으면 최상위 댓글 리스트에 추가
                rootComments.add(comment);
            } else {
                // 부모가 있으면 부모의 replies 리스트에 자기를 추가
                Comment parent = map.get(comment.getParentCommentNo());
                if (parent != null) {
                    // Comment 모델에 @Data(Lombok)가 있으므로 getReplies() 접근 가능
                    parent.getReplies().add(comment);
                }
            }
        }

        return rootComments;
    }


    @Transactional
    public Map<String, Object> toggleCommentLike(Long commentNo, String username) {
        int count = commentMapper.checkCommentLikeExists(commentNo, username);
        boolean isLiked;

        if (count > 0) {
            commentMapper.deleteCommentLike(commentNo, username);
            isLiked = false;
        } else {
            commentMapper.insertCommentLike(commentNo, username);
            isLiked = true;
        }

        int likeCount = commentMapper.countCommentLikes(commentNo);

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);
        return response;
    }
}
