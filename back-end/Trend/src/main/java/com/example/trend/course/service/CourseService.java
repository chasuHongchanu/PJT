package com.example.trend.course.service;

import com.example.trend.course.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    void registCourse(@Valid CourseRegistRequestDto courseRegistRequestDto);

    void updateCourse(@Valid CourseUpdateRequestDto courseUpdateRequestDto);

    void deleteCourse(int courseId, String userId);

    List<CourseListResponseDto> getAllCourse();

    CourseResponseDto getCourseById(int courseId);

    void likeCourse(int courseId, String userId);

    void unLikeCourse(int courseId, String userId);

    boolean isLikeCourse(int courseId, String userId);

    void likeCourseComment(int commentId, String userId);

    void unLikeCourseComment(int courseCommentId, String userId);

    boolean isLikeCourseComment(int courseCommentId, String userId);

    void registComment(CourseCommentRequestDto commentRequestDto);

    void registCommentReply(CourseCommentRequestDto commentRequestDto);

    void updateComment(CourseCommentUpdateDto commentRequestDto);
}
