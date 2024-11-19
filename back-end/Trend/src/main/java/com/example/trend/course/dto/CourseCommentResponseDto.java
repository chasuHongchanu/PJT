package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCommentResponseDto {
    private String writerId;
    private String writerNickname;
    private String writerProfileImg;
    private String content;
    private String createdAt;
    private int parentsCommentId;
    private int likesCount;
}
