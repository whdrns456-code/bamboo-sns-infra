package com.example.bambooforest.mapper;

import com.example.bambooforest.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 1. 댓글 작성
    void insertComment(Comment comment);
    // 2. 특정 게시글의 모든 댓글 조회 (작성자 정보 포함)
    List<Comment> findCommentsByPostNo(Long postNo);
    // 3. 댓글 삭제 (상태값 변경 또는 물리 삭제)
    void deleteComment(Long commentNo);
    void saveComment(Comment comment);


    void insertCommentLike(Long commentNo, String username);

    void deleteCommentLike(Long commentNo, String username);

    int checkCommentLikeExists(Long commentNo, String username);

    int countCommentLikes(Long commentNo);
}