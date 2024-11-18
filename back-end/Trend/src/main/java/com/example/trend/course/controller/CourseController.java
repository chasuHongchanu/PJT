package com.example.trend.course.controller;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import com.example.trend.course.dto.SpotRequestDto;
import com.example.trend.course.service.CourseService;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@Slf4j
@Tag(name = "Course API", description = "API for course operations")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "추천 코스 등록", description = "추천 코스 등록 기능")
    public ResponseEntity<?> regist(@Valid @ModelAttribute CourseRegistRequestDto courseRegistRequestDto, @RequestAttribute("userId") String userId){
        // 유저 id 입력
        courseRegistRequestDto.setCourseWriterId(userId);

        // JSON 문자열을 파싱하여 List<SpotRequestDto>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<SpotRequestDto> spotList = objectMapper.readValue(
                    courseRegistRequestDto.getSpotListJson(),
                    new TypeReference<List<SpotRequestDto>>() {}
            );
            courseRegistRequestDto.setSpotList(spotList);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCCESSING_EXCEPTION, e);
        }

        // 코스 등록
        courseService.registCourse(courseRegistRequestDto);
        return ResponseEntity.ok("Regist Course Successful");
    }

    @PutMapping
    @Operation(summary = "추천 코스 등록", description = "추천 코스 등록 기능")
    public ResponseEntity<?> update(@Valid @ModelAttribute CourseUpdateRequestDto courseUpdateRequestDto, @RequestAttribute("userId") String userId){
        // 유저 id 입력
        courseUpdateRequestDto.setCourseWriterId(userId);

        // JSON 문자열을 파싱하여 List<SpotRequestDto>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<SpotRequestDto> spotList = objectMapper.readValue(
                    courseUpdateRequestDto.getSpotListJson(),
                    new TypeReference<List<SpotRequestDto>>() {}
            );
            courseUpdateRequestDto.setSpotList(spotList);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCCESSING_EXCEPTION, e);
        }

        // 코스 등록
        courseService.updateCourse(courseUpdateRequestDto);
        return ResponseEntity.ok("Regist Course Successful");
    }

}
