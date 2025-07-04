package com.example.chatapp.service;

import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public User findByUsername (String username) {
        System.out.println(username + "");
        return userMapper.findByUsername(username);
    }
}
