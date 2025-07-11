package com.example.chatapp.controller;

import com.example.chatapp.model.LoginResponse;
import com.example.chatapp.model.User;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request, HttpServletResponse response) {
        String encoded = passwordEncoder.encode("test");
        User user = userService.findByUsername(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("status", 401,
                            "message", "이메일 또는 비밀번호가 틀렸습니다람쥐"
                    )
            );
        }
        System.out.println("✅ DB 비밀번호: " + user.getPassword());
        System.out.println("✅ 매치 결과: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);


        return ResponseEntity.ok((Map.of("accessToken", accessToken)));
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
