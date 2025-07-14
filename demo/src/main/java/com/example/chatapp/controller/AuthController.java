package com.example.chatapp.controller;

import com.example.chatapp.model.dto.SignupRequest;
import com.example.chatapp.model.entity.User;
import com.example.chatapp.security.JwtUtil;
import com.example.chatapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", 201, "message", "회원가입이 완료되었습니다"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request, HttpServletResponse response) {
        Map<String, Object> token = userService.login(request.getEmail(), request.getPassword(), response);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(401).body("No refresh token");

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userService.findByUsername(email);
        String newAccessToken = jwtUtil.generateAccessToken(user);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @GetMapping("/auth/check")
    public ResponseEntity<?> checkAuth(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("isAuthenticated", false));
        }

        String email = jwtUtil.extractEmail(token);
        User user = userService.findByUsername(email);
        return ResponseEntity.ok(Map.of("isAuthenticated", true));
    }
}
