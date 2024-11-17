package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
import jakarta.validation.Valid;

public interface CourseService {
    void registCourse(@Valid CourseRegistRequestDto courseRegistRequestDto);
}
