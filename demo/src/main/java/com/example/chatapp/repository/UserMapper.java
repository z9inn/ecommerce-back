package com.example.chatapp.repository;

import com.example.chatapp.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    void insertUser(User user);
}

