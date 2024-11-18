package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponseDto {
    private int courseId;
    private String userProfileImg;
    private String userId;
    private String userNickname;
    private int likes;
    private int commentCount;
    private String courseTitle;
    private String courseContent;
}
