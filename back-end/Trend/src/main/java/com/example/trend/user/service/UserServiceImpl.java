package com.example.trend.user.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.TokenDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.entity.RefreshToken;
import com.example.trend.user.entity.User;
import com.example.trend.user.mapper.RefreshTokenMapper;
import com.example.trend.user.mapper.UserMapper;
import com.example.trend.user.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;

    @Value("${jwt.refreshToken-validity}")
    private long MAX_REFRESH_TOKEN_AGE_MS;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, RefreshTokenMapper refreshTokenMapper) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public void signUp(UserSignupRequestDto userSignupRequestDto) {
        // ID 중복 확인
        boolean isDuplicated = duplicateCheck(userSignupRequestDto.getUserId());
        if (isDuplicated) {
            // 에러 반환
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 정보 저장
        int cnt = userMapper.insertNewUser(userSignupRequestDto);
        if (cnt != 1) {
            throw new CustomException(ErrorCode.FAIL_TO_SAVE_USER);
        }
    }

    @Override
    public boolean duplicateCheck(String newId) {
        int cnt = userMapper.findDuplicatedId(newId);
        if (cnt > 0) {
            return true;
        }
        return false;
    }

    // 사용자 로그인 처리: 유저 정보 확인 후 토큰 반환
    @Override
    public TokenDto login(UserLoginRequestDto userLoginRequestDto) throws Exception {
        // 비밀번호 해싱
        String hashedPassword = hashPassword(userLoginRequestDto.getUserPassword());
        log.info("hashedPassword: {}", hashedPassword);

        userLoginRequestDto.setUserPassword(hashedPassword);

        // 유저 확인
        User user = userMapper.selectUserByIdAndPassword(userLoginRequestDto);
        if (user == null) {
            log.error("일치하는 유저 정보 없음");
            throw new CustomException(ErrorCode.NOT_FOUND_MATCHED_USER);
        }

        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getUserNickname());
        log.info("accessToken: {}", accessToken);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());
        log.info("refreshToken: {}", refreshToken);

        // 기존 리프레시 토큰 삭제 후 새 토큰 저장
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiresAt = new Timestamp(now.getTime() + MAX_REFRESH_TOKEN_AGE_MS);

        int deleteCnt = refreshTokenMapper.deleteRefreshToken(user.getUserId());
        log.info("deleteCnt: {}", deleteCnt);
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUserId(user.getUserId());
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpiresAt(expiresAt);
        log.info("Insert RefreshToken: userId={}, refreshToken={}", refreshTokenEntity.getUserId(), refreshTokenEntity.getRefreshToken());
        int insertCnt = refreshTokenMapper.insertRefreshToken(refreshTokenEntity);

        // 정상적으로 리프레시 토큰이 저장되지 않은 경우
        if (insertCnt != 1) {
            throw new CustomException(ErrorCode.FAIL_TO_SAVE_REFRESH_TOKEN);
        }

        // 생성한 토큰 반환
        return new TokenDto(accessToken, refreshToken);
    }

    // 비밀번호 해싱 함수
    private String hashPassword(String password) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Error hashing password", e);
        }
    }

    // refresh token을 이용해 새 access token 생성
    @Override
    public String refreshAccessToken(String refreshToken) throws Exception {
        try {
            // 리프레시 토큰 검증
            Claims claims = jwtUtil.validateToken(refreshToken);
            String userId = claims.getSubject();

            // 사용자 정보 조회
            User user = userMapper.selectUserByUserId(userId);
            if (user == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }

            // 데이터베이스에서 리프레시 토큰 조회
            RefreshToken storedRefreshToken = refreshTokenMapper.selectRefreshToken(userId, refreshToken);
            if (storedRefreshToken == null) {
                throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            // 추가 유효기간 검증 (예: 7일)
            long tokenAge = System.currentTimeMillis() - storedRefreshToken.getCreatedAt().getTime();
            if (tokenAge > MAX_REFRESH_TOKEN_AGE_MS) { // MAX_REFRESH_TOKEN_AGE_MS는 설정된 최대 유효기간
                refreshTokenMapper.deleteRefreshToken(userId);
                throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }

            // 새로운 액세스 토큰 반환
            return jwtUtil.generateAccessToken(user.getUserId(), user.getUserNickname());

        } catch (JwtException e) {
            throw new JwtException("유효한 토큰값이 아닙니다.", e);
        }
    }
}
