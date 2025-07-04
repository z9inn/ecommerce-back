package com.example.chatapp.controller;

import com.example.chatapp.model.LoginResponse;
import com.example.chatapp.model.User;
import com.example.chatapp.security.JwtUtil;
import com.example.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> login(@RequestBody User request) {
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
        String token = jwtUtil.generateToken(user);
        jwtUtil.generateAccessToken
        System.out.println("Generated JWT Token: " + token);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
