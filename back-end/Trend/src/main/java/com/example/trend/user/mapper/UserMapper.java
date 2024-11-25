package com.example.trend.user.mapper;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserResetPwRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.dto.UserUpdateRequestDto;
import com.example.trend.user.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT count(*) FROM `user` WHERE user_id= #{newId} AND user_deleted_at IS NULL")
    int findDuplicatedId(String newId);

    @Insert("INSERT INTO `user` (user_id, user_password, user_nickname, user_address, user_email, user_phone_number, country) " +
            "VALUES (#{userId}, #{userPassword}, #{userNickname}, #{userAddress}, #{userEmail}, #{userPhoneNumber}, #{country})")
    int insertNewUser(UserSignupRequestDto userSignupRequestDto);

    @Select("SELECT * FROM `user` WHERE user_id = #{userId} AND user_deleted_at IS NULL")
    User selectUserByUserId(String userId);

    @Select("SELECT * FROM `user` WHERE user_id = #{userId} AND user_password = #{userPassword} AND user_deleted_at IS NULL")
    User selectUserByIdAndPassword(UserLoginRequestDto userLoginRequestDto);

    @Update("<script>" +
            "UPDATE user " +
            "<set>" +
            "<if test='dto.userPassword != null and dto.userPassword != \"\"'>" +
            "user_password = #{dto.userPassword}, " +
            "</if>" +
            "<if test='dto.userNickname != null and dto.userNickname != \"\"'>" +
            "user_nickname = #{dto.userNickname}, " +
            "</if>" +
            "<if test='dto.userAddress != null and dto.userAddress != \"\"'>" +
            "user_address = #{dto.userAddress}, " +
            "</if>" +
            "<if test='dto.userEmail != null and dto.userEmail != \"\"'>" +
            "user_email = #{dto.userEmail}, " +
            "</if>" +
            "<if test='dto.userPhoneNumber != null and dto.userPhoneNumber != \"\"'>" +
            "user_phone_number = #{dto.userPhoneNumber}, " +
            "</if>" +
            "<if test='dto.userProfileImgUrl != null'>" +
            "user_profile_img = #{dto.userProfileImgUrl}, " +
            "</if>" +
            "<if test='dto.userIntroduction != null'>" +
            "user_introduction = #{dto.userIntroduction}, " +
            "</if>" +
            "<if test='dto.country != null and dto.country != \"\"'>" +
            "country = #{dto.country}, " +
            "</if>" +
            "</set>" +
            "WHERE user_id = #{dto.userId}" +
            "</script>")
    void updateUser(@Param("dto") UserUpdateRequestDto userUpdateRequestDto);

    @Update("UPDATE user SET user_deleted_at = NOW()  WHERE user_id = #{requestUserId}")
    int deleteUser(String requestUserId);

    @Select("SELECT COUNT(*) FROM user WHERE user_id = #{userId} AND user_email = #{userEmail}")
    int selectUserByIdAndEmail(UserResetPwRequestDto userResetPwRequestDto);

    @Update("UPDATE user SET user_password = #{newPassword} WHERE user_id = #{userId} AND user_email = #{userEmail}")
    int updateUserPassword(UserResetPwRequestDto userResetPwRequestDto);

    @Select("SELECT `user_profile_img` FROM user WHERE user_id = #{userId}")
    String selectUserProfileImage(String userId);
}
