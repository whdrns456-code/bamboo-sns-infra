package com.example.bambooforest.mapper;

import com.example.bambooforest.model.Posts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {


    int insertPost(@Param("post") Posts post, @Param("username") String username);
    List<Posts> findAllWithNickname();
    List<Posts> findMorePosts(@Param("lastPostNo") int lastPostNo, @Param("size") int size);

    List<Posts> findRootAnnouncements(@Param("limit") int limit, @Param("offset") int offset);
    int countRootPosts();

    void updateViewCount(Long postNo);
    Posts selectPostByNo(Long postNo);

    void deleteLike(Long postNo, String username);

    void insertLike(Long postNo, String username);
    int countLikesByPostNo(Long postNo);

    int checkLikeExists(Long postNo, String username);
}
