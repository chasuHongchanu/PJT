package com.example.trend.user.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

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
}
