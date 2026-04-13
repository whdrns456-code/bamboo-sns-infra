package com.example.bambooforest.mapper;

import com.example.bambooforest.model.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 회원가입
    int insertUser(Users user);
    // ID 중복 체크용[]
    Users findByUsername(String username);
    // 닉네임으로 사용자 조회[중복 검사용]
    Users findByNickname(String nickname);
    



}