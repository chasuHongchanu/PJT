package com.example.trend.user.service;

import com.example.trend.user.dto.*;

public interface UserService {
    void signUp(UserSignupRequestDto userSignupRequestDto) throws Exception;

    boolean duplicateCheck(String newId);

    TokenDto login(UserLoginRequestDto userLoginRequestDto) throws Exception;

    String refreshAccessToken(String refreshToken) throws Exception;

    UserInfoResponseDto findUserInfo(String userId);

    void updateUser(UserUpdateRequestDto userUpdateRequestDto) throws Exception;
}
