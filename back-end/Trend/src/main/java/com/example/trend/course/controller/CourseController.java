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
        courseRegistRequestDto.setSpotList(getSpotList(courseRegistRequestDto.getSpotListJson()));

        // 코스 등록
        courseService.registCourse(courseRegistRequestDto);
        return ResponseEntity.ok("Regist Course Successful");
    }

    @PutMapping
    @Operation(summary = "추천 코스 수정", description = "추천 코스 수정 기능")
    public ResponseEntity<?> update(@Valid @ModelAttribute CourseUpdateRequestDto courseUpdateRequestDto, @RequestAttribute("userId") String userId){
        // 유저 id 입력
        courseUpdateRequestDto.setCourseWriterId(userId);

        // JSON 문자열을 파싱하여 List<SpotRequestDto>로 변환
        courseUpdateRequestDto.setSpotList(getSpotList(courseUpdateRequestDto.getSpotListJson()));

        // 코스 등록
        courseService.updateCourse(courseUpdateRequestDto);
        return ResponseEntity.ok("Update Course Successful");
    }

    @DeleteMapping
    @Operation(summary = "여행 코스 삭제", description = "추천 코스 게시물 삭제 기능")
    public ResponseEntity<?> delete(@RequestParam int courseId, @RequestAttribute String userId){
        // 게시글 삭제
        courseService.deleteCourse(courseId, userId);
        return ResponseEntity.ok("Delete Course Successful");
    }

    private List<SpotRequestDto> getSpotList(String spotListJson){
        // JSON 문자열을 파싱하여 List<SpotRequestDto>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<SpotRequestDto> spotList = objectMapper.readValue(
                    spotListJson,
                    new TypeReference<List<SpotRequestDto>>() {}
            );
            return spotList;
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCCESSING_EXCEPTION, e);
        }
    }
}
