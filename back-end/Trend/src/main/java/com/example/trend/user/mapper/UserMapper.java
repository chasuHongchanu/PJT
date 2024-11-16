package com.example.trend.user.mapper;

import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.dto.UserUpdateRequestDto;
import com.example.trend.user.entity.User;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;

@Mapper
public interface UserMapper {
    @Select("SELECT count(*) FROM `user` WHERE user_id= #{newId}")
    int findDuplicatedId(String newId);

    @Insert("INSERT INTO `user` (user_id, user_password, user_nickname, user_address, user_email, user_phone_number) " +
            "VALUES (#{userId}, #{userPassword}, #{userNickname}, #{userAddress}, #{userEmail}, #{userPhoneNumber})")
    int insertNewUser(UserSignupRequestDto userSignupRequestDto);

    @Select("SELECT * FROM `user` WHERE user_id = #{userId}")
    User selectUserByUserId(String userId);

    @Select("SELECT * FROM `user` WHERE user_id = #{userId} AND user_password = #{userPassword}")
    User selectUserByIdAndPassword(UserLoginRequestDto userLoginRequestDto);

    @Update("UPDATE user\n" +
            "SET user_password = #{dto.userPassword}, " +
            "user_nickname = #{dto.userNickname}, " +
            "user_address = #{dto.userAddress}, " +
            "user_email = #{dto.userEmail}, " +
            "user_phone_number = #{dto.userPhoneNumber}, " +
            "user_profile_img = #{imgUrl}, " +
            "user_introduction = #{dto.userIntroduction}, " +
            "user_rating = #{dto.userRating}, " +
            "country = #{dto.country}\n" +
            "WHERE user_id = #{dto.userId}")
    void updateUser(@Param("dto") UserUpdateRequestDto userUpdateRequestDto, @Param("imgUrl") String imgUrl);
}
