package com.example.trend.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.accessToken-validity}")
    private long accessTokenValidity;
    @Value("${jwt.refreshToken-validity}")
    private long refreshTokenValidity;

    private Key key;

    // 초기화 메서드: secret key를 Base64로 디코딩하여 Key 객체 생성
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 사용자 ID와 닉네임을 포함한 access token 생성
    public String generateAccessToken(String userId, String userNickname) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(userId)
                .claim("nickname", userNickname)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 사용자 ID를 포함한 refresh token 생성
    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증 메서드: 검증할 토큰을 입력받아 Claims 반환
    // Claims: token의 payload에 포함된 정보 조각(subject(user id), 닉네임, 만료 시간 등)
    public Claims validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 액세스 토큰의 만료 시간을 반환
    public long getExpirationTime(String token) throws JwtException {
        Claims claims = validateToken(token);
        return claims.getExpiration().getTime();
    }
}
