package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseResponseDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    void registCourse(@Valid CourseRegistRequestDto courseRegistRequestDto);

    void updateCourse(@Valid CourseUpdateRequestDto courseUpdateRequestDto);

    void deleteCourse(int courseId, String userId);

    List<CourseResponseDto> getAllCourse();
}
