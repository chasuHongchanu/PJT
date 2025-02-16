package com.example.trend.user.service;

import com.example.trend.user.dto.*;

public interface UserService {
    void signUp(UserSignupRequestDto userSignupRequestDto) throws Exception;

    boolean duplicateCheck(String newId);

    TokenDto login(UserLoginRequestDto userLoginRequestDto) throws Exception;

    String refreshAccessToken(String refreshToken) throws Exception;

    UserInfoResponseDto findUserInfo(String userId);

    UserUpdateResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    void logout(String requestUserId);

    void deleteUser(String requestUserId);

    void resetPassword(UserResetPwRequestDto userResetPwRequestDto);

    String getUserProfileImage(String userId);
}
