package com.example.trend.user.service;

import com.example.trend.common.MailService;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.*;
import com.example.trend.user.entity.RefreshToken;
import com.example.trend.user.entity.User;
import com.example.trend.user.jwt.JwtUtil;
import com.example.trend.user.mapper.RefreshTokenMapper;
import com.example.trend.user.mapper.UserMapper;
import com.example.trend.util.FileUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;
    private final FileUtil fileUtil;
    private final MailService mailService;

    @Value("${jwt.refreshToken-validity}")
    private long MAX_REFRESH_TOKEN_AGE_MS;

    public UserServiceImpl(MailService mailService, FileUtil fileUtil, RefreshTokenMapper refreshTokenMapper, JwtUtil jwtUtil, UserMapper userMapper) {
        this.mailService = mailService;
        this.fileUtil = fileUtil;
        this.refreshTokenMapper = refreshTokenMapper;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void signUp(UserSignupRequestDto userSignupRequestDto) throws Exception {
        // ID 중복 확인
        boolean isDuplicated = duplicateCheck(userSignupRequestDto.getUserId());
        if (isDuplicated) {
            // 에러 반환
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        String hashedPassword = hashPassword(userSignupRequestDto.getUserPassword());

        userSignupRequestDto.setUserPassword(hashedPassword);

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
    @Transactional
    public TokenDto login(UserLoginRequestDto userLoginRequestDto) {
        // 비밀번호 해싱
        String hashedPassword = hashPassword(userLoginRequestDto.getUserPassword());
        log.info("hashedPassword: {}", hashedPassword);

        userLoginRequestDto.setUserPassword(hashedPassword);

        // 유저 확인
        User user = userMapper.selectUserByIdAndPassword(userLoginRequestDto);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_LOGIN_USER);
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
    private String hashPassword(String password) {
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
            throw new CustomException(ErrorCode.FAIL_TO_HASING_PASSWORD);
        }
    }

    // refresh token을 이용해 새 access token 생성
    @Override
    @Transactional
    public String refreshAccessToken(String refreshToken){
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

    }

    @Override
    public UserInfoResponseDto findUserInfo(String userId) {
        User user = userMapper.selectUserByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .userProfileImg(user.getUserProfileImg())
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userAddress(user.getUserAddress())
                .userEmail(user.getUserEmail())
                .userPhoneNumber(user.getUserPhoneNumber())
                .userIntroduction(user.getUserIntroduction())
                .userRating(user.getUserRating())
                .country(user.getCountry())
                .userCreatedAt(user.getUserCreatedAt())
                .build();

        return userInfoResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateRequestDto userUpdateRequestDto){
        // 비밀번호 해싱
        String hashedPassword = hashPassword(userUpdateRequestDto.getUserPassword());
        userUpdateRequestDto.setUserPassword(hashedPassword);

        //image file의 이름을 경로를 추가한 이름으로 변경
        String userId = userUpdateRequestDto.getUserId();
        log.info("userId: {}", userId);
        String imgUrl =
                "users/"
                + userId +"/"
                + userUpdateRequestDto.getUserProfileImg().getOriginalFilename();
        log.info("imgUrl: {}", imgUrl);

        // storage에 이미지 저장
        fileUtil.saveFileIntoStorage("users", userId, userUpdateRequestDto.getUserProfileImg());

        // DB에 유저 정보와 이미지 이름을 저장
        try {
            userMapper.updateUser(userUpdateRequestDto, imgUrl);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_USER);
        }
    }

    @Override
    @Transactional
    public void logout(String requestUserId) {
        try {
            refreshTokenMapper.deleteRefreshToken(requestUserId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_REFRESH_TOKEN, e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(String requestUserId) {
        try {
            int result = userMapper.deleteUser(requestUserId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_USER, e);
        }
    }

    @Override
    @Transactional
    public void resetPassword(UserResetPwRequestDto userResetPwRequestDto) {
        // 해당 유저 정보가 있는지 확인 (없으면 UserNotFound 반환)
        if (userMapper.selectUserByIdAndEmail(userResetPwRequestDto) != 1) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        // 임시 비밀번호 생성 및 비밀번호 업데이트
        String tmpPassword = generateTemporaryPassword();
        String hashedPassword = hashPassword(tmpPassword);
        userResetPwRequestDto.setNewPassword(hashedPassword);
        if (userMapper.updateUserPassword(userResetPwRequestDto) != 1) {
            throw new CustomException(ErrorCode.FAIL_TO_RESET_PASSWORD);
        }

        // 임시 비밀번호 메일로 전송
        String subject = userResetPwRequestDto.getUserId() + "님의 임시 비밀번호 입니다.";
        String body = userResetPwRequestDto.getUserId() + """
				님의 임시 비밀번호 입니다.
				로그인 후 비밀번호를 변경해주세요.
				임시 비밀번호 : 
				""" + tmpPassword;

        try {
            mailService.sendEmail(userResetPwRequestDto.getUserEmail(), subject, body);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SEND_EMAIL, e);
        }

    }

    // 8자리 임시 비밀번호 생성
    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
