package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseListResponseDto {
    private int courseId;
    private String userProfileImg;
    private String userId;
    private String userNickname;
    private int likeCount;
    private int commentCount;
    private String courseTitle;
    private String courseContent;
}
