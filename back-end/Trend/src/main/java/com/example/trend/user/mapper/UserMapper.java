package com.example.trend.user.mapper;

import com.example.trend.user.dto.UserSignupRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT count(*) FROM `user` WHERE user_id= #{newId}")
    int findDuplicatedId(String newId);

    @Insert("INSERT INTO user (user_id, user_password, user_nickname, user_address, user_email, user_phone_number) " +
            "VALUES (#{userId}, #{userPassword}, #{userNickname}, #{userAddress}, #{userEmail}, #{userPhoneNumber})")
    int insertNewUser(UserSignupRequestDto userSignupRequestDto);
}
