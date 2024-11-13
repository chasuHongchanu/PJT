package com.example.trend.user.repository;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean duplicateCheck(String newId) {
        System.out.println(newId);
        int cnt = userMapper.findDuplicatedId(newId);
        if (cnt > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveUser(UserSignupRequestDto userSignupRequestDto) {
        int cnt = userMapper.insertNewUser(userSignupRequestDto);
        if (cnt != 1) {
            throw new CustomException(ErrorCode.FAIL_TO_SAVE_USER);
        }
    }
}
