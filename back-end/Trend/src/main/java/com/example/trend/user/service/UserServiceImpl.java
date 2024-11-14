package com.example.trend.user.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.TokenResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.mapper.UserMapper;
import com.example.trend.user.repository.UserRepository;
import com.example.trend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

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
        String userId = userRepository.searchUserByIdAndPassword(userLoginRequestDto);

        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        userRepository.saveRefreshToken(userId, refreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
        return tokenResponseDto;
    }
}
