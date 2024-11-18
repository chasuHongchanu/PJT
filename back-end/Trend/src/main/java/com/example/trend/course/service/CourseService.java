package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import jakarta.validation.Valid;

public interface CourseService {
    void registCourse(@Valid CourseRegistRequestDto courseRegistRequestDto);

    void updateCourse(@Valid CourseUpdateRequestDto courseUpdateRequestDto);
}
