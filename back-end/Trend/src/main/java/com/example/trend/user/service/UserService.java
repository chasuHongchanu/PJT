package com.example.trend.user.service;

import com.example.trend.user.dto.TokenResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import jakarta.validation.Valid;

public interface UserService {
    void signUp(UserSignupRequestDto userSignupRequestDto);

    boolean duplicateCheck(String newId);

    TokenResponseDto login(@Valid UserLoginRequestDto userLoginRequestDto);

}
