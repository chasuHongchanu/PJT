package com.example.trend.course.dto;

import com.example.trend.course.comment.dto.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class CourseResponseDto {
    private String writerProfileImg;
    private String writerId;
    private String writerNickname;
    private String courseTitle;
    private String courseContent;
    private List<String> courseImages;
    private Timestamp courseCreatedAt;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private List<CommentResponseDto> comments;
}
