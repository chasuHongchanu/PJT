package com.example.trend.course.controller;

import com.example.trend.config.SkipJwt;
import com.example.trend.course.dto.*;
import com.example.trend.course.dto.comment.CourseCommentDeleteDto;
import com.example.trend.course.dto.comment.CourseCommentRequestDto;
import com.example.trend.course.dto.comment.CourseCommentResponseDto;
import com.example.trend.course.dto.comment.CourseCommentUpdateDto;
import com.example.trend.course.service.CourseService;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.Pagination;
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
    public ResponseEntity<?> regist(@Valid @ModelAttribute CourseRegistRequestDto courseRegistRequestDto, @RequestAttribute("userId") String userId) {
        // 유저 id 입력
        courseRegistRequestDto.setCourseWriterId(userId);

        // JSON 문자열을 파싱하여 List<CourseSpotDto>로 변환
        courseRegistRequestDto.setSpotList(getSpotList(courseRegistRequestDto.getSpotListJson()));

        // 코스 등록
        courseService.registCourse(courseRegistRequestDto);
        return ResponseEntity.ok("Regist CourseListResponseDto Successful");
    }

    @PutMapping
    @Operation(summary = "추천 코스 수정", description = "추천 코스 수정 기능")
    public ResponseEntity<?> update(@Valid @ModelAttribute CourseUpdateRequestDto courseUpdateRequestDto, @RequestAttribute("userId") String userId) {
        // 유저 id 입력
        courseUpdateRequestDto.setCourseWriterId(userId);

        // JSON 문자열을 파싱하여 List<CourseSpotDto>로 변환
        courseUpdateRequestDto.setSpotList(getSpotList(courseUpdateRequestDto.getSpotListJson()));

        // 코스 등록
        courseService.updateCourse(courseUpdateRequestDto);
        return ResponseEntity.ok("Update CourseListResponseDto Successful");
    }

    @DeleteMapping
    @Operation(summary = "여행 코스 삭제", description = "추천 코스 게시물 삭제 기능")
    public ResponseEntity<?> delete(@RequestParam int courseId, @RequestAttribute String userId) {
        // 게시글 삭제
        courseService.deleteCourse(courseId, userId);
        return ResponseEntity.ok("Delete CourseListResponseDto Successful");
    }

    private List<CourseSpotDto> getSpotList(String spotListJson) {
        // JSON 문자열을 파싱하여 List<CourseSpotDto>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<CourseSpotDto> spotList = objectMapper.readValue(
                    spotListJson,
                    new TypeReference<List<CourseSpotDto>>() {
                    }
            );
            return spotList;
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCCESSING_EXCEPTION, e);
        }
    }

    @SkipJwt
    @GetMapping("/list")
    @Operation(summary = "전체 여행 코스 조회", description = "전체 여행 코스 목록을 조회")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Pagination<CourseListResponseDto> courseListResponseDtos = courseService.getAllCourse(page, size);
        return ResponseEntity.ok(courseListResponseDtos);
    }

    @SkipJwt
    @GetMapping("/detail/{courseId}")
    @Operation(summary = "여행 코스 상세 조회", description = "전체 여행 코스 목록을 조회")
    public ResponseEntity<?> getCourseDetail(@PathVariable int courseId) {
        CourseResponseDto courseResponseDto = courseService.getCourseDetail(courseId);
        return ResponseEntity.ok(courseResponseDto);
    }

    @SkipJwt
    @GetMapping("/search")
    @Operation(summary = "여행 코스 검색 및 필터링", description = "코스 검색 및 필터링 기능")
    public ResponseEntity<?> searchCourse(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                          @RequestBody CourseSearchRequestDto courseSearchRequestDto) {
        Pagination<CourseListResponseDto> courseListResponseDtos = courseService.searchCourses(page, size, courseSearchRequestDto);
        return ResponseEntity.ok(courseListResponseDtos);
    }



    //=================댓글===========================
    // 작성
    @PostMapping("/{courseId}/comment")
    @Operation(summary = "여행 코스 댓글 작성 기능", description = "여행 코스 댓글 작성하는 기능")
    public ResponseEntity<?> createComment(@PathVariable int courseId, @RequestBody CourseCommentRequestDto commentRequestDto, @RequestAttribute("userId") String userId) {
        commentRequestDto.setCourseId(courseId);
        commentRequestDto.setUserId(userId);
        courseService.registComment(commentRequestDto);
        return ResponseEntity.ok("Regist Course Comment Successful");
    }
    // 대댓글 작성
    @PostMapping("/{courseId}/comment/{parentsCommentId}")
    @Operation(summary = "여행 코스 대댓글 작성 기능", description = "여행 코스 대댓글을 작성하는 기능")
    public ResponseEntity<?> createCommentReply(@PathVariable int courseId, @PathVariable int parentsCommentId, @RequestBody CourseCommentRequestDto commentRequestDto, @RequestAttribute("userId") String userId) {
        commentRequestDto.setCourseId(courseId);
        commentRequestDto.setUserId(userId);
        commentRequestDto.setParentsCommentId(parentsCommentId);
        courseService.registCommentReply(commentRequestDto);
        return ResponseEntity.ok("Regist Course Comment Reply Successful");
    }

    // 수정
    @PutMapping("/{courseId}/comment/{commentId}")
    @Operation(summary = "여행 코스 댓글 수정 기능", description = "여행 코스 댓글을 수정하는 기능")
    public ResponseEntity<?> updateComment(@PathVariable int courseId, @PathVariable int commentId, @RequestBody CourseCommentUpdateDto commentRequestDto, @RequestAttribute("userId") String userId) {
        commentRequestDto.setCourseId(courseId);
        commentRequestDto.setCommentId(commentId);
        commentRequestDto.setUserId(userId);
        courseService.updateComment(commentRequestDto);
        return ResponseEntity.ok("Update Course Comment Successful");
    }

    // 삭제
    @DeleteMapping("/{courseId}/comment/{commentId}")
    @Operation(summary = "여행 코스 댓글 삭제 기능", description = "여행 코스 댓글을 삭제하는 기능")
    public ResponseEntity<?> deleteComment(@PathVariable int courseId, @PathVariable int commentId, @RequestAttribute("userId") String userId) {
        CourseCommentDeleteDto courseCommentDeleteDto = new CourseCommentDeleteDto();
        courseCommentDeleteDto.setCourseId(courseId);
        courseCommentDeleteDto.setCommentId(commentId);
        courseCommentDeleteDto.setUserId(userId);
        courseService.deleteComment(courseCommentDeleteDto);
        return ResponseEntity.ok("Delete Course Comment Successful");
    }

    // 코스 게시물의 댓글 목록 조회
    @SkipJwt
    @GetMapping("/{courseId}/comment")
    @Operation(summary = "여행 코스의 댓글 목록 조회", description = "여행 코스의 댓글 목록만 새로 고침하거나 불러올 필요가 있을 때 사용하는 메서드")
    public ResponseEntity<?> getCommentList(@PathVariable int courseId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pagination<CourseCommentResponseDto> courseCommentResponseDtos = courseService.getCommentList(courseId, page, size);
        return ResponseEntity.ok(courseCommentResponseDtos);
    }

    // 게시물 댓글의 대댓글 목록 조회(페이징)
    @SkipJwt
    @GetMapping("/{courseId}/comment/{commentId}")
    @Operation(summary = "여행 코스 댓글의 댓글 목록 조회", description = "여행 코스의 대댓글 목록만 새로 고침하거나 불러올 필요가 있을 때 사용하는 메서드")
    public ResponseEntity<?> getCommentReplyList(@PathVariable int courseId,
                                                 @PathVariable int commentId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pagination<CourseCommentResponseDto> courseCommentResponseDtos = courseService.getCommentReplyList(courseId, commentId, page, size);
        return ResponseEntity.ok(courseCommentResponseDtos);
    }


    //=================좋아요===========================
    @PostMapping("/{courseId}/like")
    @Operation(summary = "여행 코스 게시물 좋아요", description = "여행 코스 좋아요 처리 기능")
    public ResponseEntity<?> likeCourse(@PathVariable int courseId, @RequestAttribute("userId") String userId) {
        courseService.likeCourse(courseId, userId);
        return ResponseEntity.ok("Like Course Successful");
    }

    @DeleteMapping("/{courseId}/like")
    @Operation(summary = "여행 코스 게시물 좋아요 취소", description = "여행 코스 좋아요  취소 처리 기능")
    public ResponseEntity<?> unLikeCourse(@PathVariable int courseId, @RequestAttribute("userId") String userId) {
        courseService.unLikeCourse(courseId, userId);
        return ResponseEntity.ok("UnLike Course Successful");
    }

    @GetMapping("/{courseId}/like")
    @Operation(summary = "여행 코스 게시물 좋아요 확인", description = "좋아요 한 여행 코스인지 확인하는 기능")
    public ResponseEntity<?> isLikeCourse(@PathVariable int courseId, @RequestAttribute("userId") String userId) {
        boolean result = courseService.isLikeCourse(courseId, userId);
        return ResponseEntity.ok("좋아요 여부: " + result);
    }

    @PostMapping("/comment/like")
    @Operation(summary = "여행 코스 게시물 댓글 좋아요", description = "여행 코스 댓글 좋아요 처리 기능")
    public ResponseEntity<?> likeCourseComment(@RequestParam int courseCommentId, @RequestAttribute("userId") String userId) {
        courseService.likeCourseComment(courseCommentId, userId);
        return ResponseEntity.ok("Like CourseComment Successful");
    }

    @DeleteMapping("/comment/like")
    @Operation(summary = "여행 코스 게시물 댓글 좋아요 취소", description = "여행 코스 댓글 좋아요  취소 처리 기능")
    public ResponseEntity<?> unLikeCourseComment(@RequestParam int courseCommentId, @RequestAttribute("userId") String userId) {
        courseService.unLikeCourseComment(courseCommentId, userId);
        return ResponseEntity.ok("UnLike CourseComment Successful");
    }

    @GetMapping("/comment/like")
    @Operation(summary = "여행 코스 게시물 댓글 좋아요 확인", description = "좋아요 한 여행 코스 댓글인지 확인하는 기능")
    public ResponseEntity<?> isLikeCourseComment(@RequestParam int courseCommentId, @RequestAttribute("userId") String userId) {
        boolean result = courseService.isLikeCourseComment(courseCommentId, userId);
        return ResponseEntity.ok("댓글 좋아요 여부: " + result);
    }

    @SkipJwt
    @GetMapping("/spot")
    @Operation(summary = "관광지 검색 api")
    public ResponseEntity<?> getSpot(@RequestParam String keyWord){
        log.info("keyWord: ", keyWord);
        List<SpotDto> spotDto = courseService.getSpot(keyWord);
        return ResponseEntity.ok(spotDto);
    }
}