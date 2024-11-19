package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCommentRequestDto {
    private int courseId;
    private String userId;
    private int parentsCommentId;
    private String content;
}
