package com.example.chatapp.security;

import com.example.chatapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long expiration = 1000 * 60 * 60; // 1시간
    private final long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // 7일

    // 토큰 생성
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // 이메일을 subject로
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 🔑 토큰에서 이메일 추출
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 내부적으로 Claims 꺼내기
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}