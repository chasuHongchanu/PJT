package com.example.trend.course.controller;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@Slf4j
@Tag(name = "Course API", description = "API for course operations")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/regist")
    @Operation(summary = "추천 코스 등록", description = "추천 코스 등록 기능")
    public ResponseEntity<?> regist(@Valid @RequestBody CourseRegistRequestDto courseRegistRequestDto, @RequestAttribute("userId") String userId) {
        // 유저 id 입력
        courseRegistRequestDto.setCourseWriterId(userId);
        // 코스 등록
        courseService.registCourse(courseRegistRequestDto);
        return ResponseEntity.ok("Regist Course Successful");
    }

}
