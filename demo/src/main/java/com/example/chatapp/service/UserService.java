package com.example.chatapp.service;

import com.example.chatapp.model.dto.SignupRequest;
import com.example.chatapp.model.entity.User;
import com.example.chatapp.repository.UserMapper;
import com.example.chatapp.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void registerUser(SignupRequest request) {
//        if(isEmailDuplicate(user.getEmail())) {
//            throw new IllegalArgumentException("Email address already in use");
//        }
        if (userMapper.findByUsername(request.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userMapper.insertUser(user);
    }

    public boolean isEmailDuplicate(String email) {
        User existUser = userMapper.findByUsername(email);
        return existUser != null && existUser.getEmail().equals(email);
    }

    public Map<String, Object> login(String email, String rawPassword, HttpServletResponse response) {
        User user = userMapper.findByUsername(email);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 틀렸습니다.") {
            };
        }
        System.out.println("✅ DB 비밀번호: " + user.getPassword());
        System.out.println("✅ 매치 결과: " + passwordEncoder.matches(rawPassword, user.getPassword()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);


        return Map.of("accessToken", accessToken);
    }
}
