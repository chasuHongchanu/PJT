package com.example.trend.user.service;

import com.example.trend.user.dto.UserSignupRequestDto;

public interface UserService {
    void signUp(UserSignupRequestDto userSignupRequestDto);

    boolean duplicateCheck(String newId);
}
