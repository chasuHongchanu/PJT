package com.example.trend.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class UserUpdateRequestDto {
    private String userId;
    private String userPassword;
    private String userNickname;
    private String userAddress;
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;
    private String userPhoneNumber;
    private MultipartFile userProfileImg;
    private String userIntroduction;
    private double userRating;
    private String country;
    private Timestamp userCreatedAt;
}
