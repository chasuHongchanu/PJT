package com.example.trend.user.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.TokenResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.entity.UserEntity;
import com.example.trend.user.mapper.UserMapper;
import com.example.trend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Value("jwt.refreshToken-validity")
    private long refreshTokenValidity;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
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

    @Override
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        // 유저 확인
        UserEntity userEntity = userMapper.findUserByIdAndPassword(userLoginRequestDto);
        if (userEntity == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MATCHED_USER);
        }
        String userId = userEntity.getUserId();

        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        // refresh token 저장
        new Timestamp(System.currentTimeMillis() + refreshTokenValidity);

        int result = userMapper.insertRefreshToken(userId, refreshToken, new Timestamp(System.currentTimeMillis() + refreshTokenValidity));

        // 생성한 토큰 반환
        TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
        return tokenResponseDto;
    }
}
