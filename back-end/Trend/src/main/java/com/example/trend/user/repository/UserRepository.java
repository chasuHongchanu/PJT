package com.example.trend.user.repository;

import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;

public interface UserRepository {
    boolean duplicateCheck(String newId);

    void saveUser(UserSignupRequestDto userSignupRequestDto);

    String searchUserByIdAndPassword(UserLoginRequestDto userLoginRequestDto);

    void saveRefreshToken(String userId, String refreshToken);
}
