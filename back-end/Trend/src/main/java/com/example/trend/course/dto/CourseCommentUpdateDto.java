package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCommentUpdateDto {
    private int courseId;
    private int commentId;
    private String userId;
    private String content;
}
