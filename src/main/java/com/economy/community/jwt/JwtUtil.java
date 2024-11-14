//package com.economy.community.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import java.util.Date;
//import javax.crypto.SecretKey;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtil {
//
//    private final SecretKey secretKey;
//
//    @Value("${jwt.expiration}")
//    private Long expirationTime; // 토큰 만료 시간 (밀리초 단위)
//
//    // secretKey 초기화 (Keys를 사용하여 SecretKey 객체 생성)
//    public JwtUtil(@Value("${jwt.secret}") String secret) {
//        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    // JWT 토큰 생성 메서드
//    public String generateToken(Long userId, String userNickname) {
//        return Jwts.builder()
//                .subject("User Authentication")
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + expirationTime))
//                .claim("userId", userId)
//                .claim("userNickname", userNickname)
//                .signWith(secretKey)
//                .compact();
//    }
//
//    // JWT 토큰에서 클레임 추출
//    public Claims getClaimsFromToken(String token) {
//        return Jwts.parser() // parser() 대신 parserBuilder() 사용
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    // JWT 토큰에서 사용자 ID 추출
//    public Long getUserIdFromToken(String token) {
//        return Long.valueOf(getClaimsFromToken(token).get("userId").toString());
//    }
//
//    // JWT 토큰에서 사용자 닉네임 추출
//    public String getUserNicknameFromToken(String token) {
//        return getClaimsFromToken(token).get("userNickname").toString();
//    }
//
//    // JWT 토큰 검증 메서드
//    public boolean isTokenValid(String token) {
//        try {
//            Claims claims = getClaimsFromToken(token);
//            return !claims.getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
