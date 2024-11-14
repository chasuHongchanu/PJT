package com.example.trend.user.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.TokenResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.repository.UserRepository;
import com.example.trend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void signUp(UserSignupRequestDto userSignupRequestDto) {
        // ID 중복 확인
        boolean isDuplicated = userRepository.duplicateCheck(userSignupRequestDto.getUserId());
        if (isDuplicated) {
            // 에러 반환
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 정보 저장
        userRepository.saveUser(userSignupRequestDto);

    }

    @Override
    public boolean duplicateCheck(String newId) {
        boolean result = userRepository.duplicateCheck(newId);
        return result;
    }

    @Override
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        String userId = userRepository.searchUserByIdAndPassword(userLoginRequestDto);

        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        userRepository.saveRefreshToken(userId, refreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
        return tokenResponseDto;
    }
}
