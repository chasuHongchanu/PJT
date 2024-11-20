package com.example.trend.course.service;

import com.example.trend.course.dto.*;
import com.example.trend.course.dto.comment.CourseCommentDeleteDto;
import com.example.trend.course.dto.comment.CourseCommentRequestDto;
import com.example.trend.course.dto.comment.CourseCommentResponseDto;
import com.example.trend.course.dto.comment.CourseCommentUpdateDto;
import com.example.trend.course.mapper.CourseCommentMapper;
import com.example.trend.course.mapper.CourseLikeMapper;
import com.example.trend.course.mapper.CourseMapper;
import com.example.trend.course.mapper.SpotMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.FileUtil;
import com.example.trend.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;
    private final SpotMapper spotMapper;
    private final FileUtil fileUtil;
    private final CourseLikeMapper courseLikeMapper;
    private final CourseCommentMapper courseCommentMapper;

    @Override
    @Transactional
    public void registCourse(CourseRegistRequestDto courseRegistRequestDto) {
        // 코스 등록
        try {
            courseMapper.insertCourse(courseRegistRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE, e);
        }
        int courseId = courseRegistRequestDto.getCourseId();
        log.info(courseId + "");
        log.info(courseRegistRequestDto.getCourseContent());

        // 등록된 여행 코스 등록
        saveCourseSpots(courseRegistRequestDto.getSpotList(), courseId);

        // 이미지 저장
        saveCourseImages(courseRegistRequestDto.getImageList(), courseId);

    }

    @Override
    public void updateCourse(CourseUpdateRequestDto courseUpdateRequestDto) {
        int courseId = courseUpdateRequestDto.getCourseId();
        // 권한 확인
        String writerId = courseMapper.selectWriterIdByCourseId(courseId);
        if (!writerId.equals(courseUpdateRequestDto.getCourseWriterId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 코스 데이터 업데이트
        try {
            courseMapper.updateCourse(courseUpdateRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE, e);
        }

        // 여행 코스 수정(기존 코스 모두 삭제 후 새로 등록)
        // 기존 경유지 삭제
        spotMapper.deleteCourseSpotByCourseId(courseId);
        // 수정된 경유지 입력
        saveCourseSpots(courseUpdateRequestDto.getSpotList(), courseId);

        // 이미지 수정(db와 storage 모두 기존 파일 삭제 후 새로 등록)
        // 기존 이미지 파일 삭제
        courseMapper.deleteCourseImage(courseId);
        fileUtil.deleteFiles("courses", courseId);
        // 수정할 이미지 저장
        saveCourseImages(courseUpdateRequestDto.getImageList(), courseId);
    }

    private void saveCourseSpots(List<CourseSpotDto> spotList, int courseId) {
        for (CourseSpotDto courseSpotDto : spotList) {
            courseSpotDto.setCourseId(courseId);
            try {
                spotMapper.insertCourseSpot(courseSpotDto);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_SPOT, e);
            }
        }
    }

    private void saveCourseImages(List<MultipartFile> imageList, int courseId) {
        // 이미지 저장
        if (!(imageList.size() == 1 && imageList.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("courses", courseId, imageList);


            // 파일 이름을 추출하여 itemImageNames 리스트에 추가
            List<String> courseImageNames = imageList.stream()
                    .map(file -> "courses/" + courseId + "/" + file.getOriginalFilename())
                    .toList();

            // db에 이미지 경로 및 이름 저장
            // Thumbnail 경로 및 이름 저장
            String thumbnail = courseImageNames.get(0);
            courseMapper.insertThumbnail(thumbnail, courseId);
            // db에 물품 이미지 이름 정보 insert
            for (String courseImageName : courseImageNames) {
                courseMapper.insertCourseImage(courseId, courseImageName);
            }
        }
    }

    @Override
    public void deleteCourse(int courseId, String userId) {
        // 해당 게시글이 있는지 확인
        int result = courseMapper.countCourseByCourseId(courseId);
        if (result != 1) {
            throw new CustomException(ErrorCode.NOT_FOUND_COURSE_INFO);
        }
        // 게시글 삭제 권한 확인
        String writerId = courseMapper.selectWriterIdByCourseId(courseId);
        if (!writerId.equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        // 게시글 삭제
        result = courseMapper.deleteCourse(courseId, userId);
        if (result != 1) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_COURSE);
        }
    }


    @Override
    public Pagination<CourseListResponseDto> getAllCourse(int page, int size) {
        try {
            int offset = (page - 1) * size;
            // 코스 목록 가져오기
            List<CourseListResponseDto> courseList = courseMapper.selectAllCourse(size, offset);
            // 전체 개수 파악
            int totalItems = courseMapper.countAllCourse(); // 총 데이터 수
            // 페이징 객체 반환
            return new Pagination<>(courseList, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Pagination<CourseListResponseDto> searchCourses(int page, int size, CourseSearchRequestDto courseSearchRequestDto) {
        try {
            int offset = (page - 1) * size;
            // 코스 목록 가져오기
            List<CourseListResponseDto> courseList = courseMapper.searchCourses(courseSearchRequestDto, size, offset);
            // 전체 개수 파악
            int totalItems = courseMapper.countSearchCourse(courseSearchRequestDto); // 총 데이터 수
            // 페이징 객체 반환
            return new Pagination<>(courseList, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public CourseResponseDto getCourseDetail(int courseId) {
        CourseResponseDto courseResponseDto = new CourseResponseDto();
        // 코스 정보 가져오기
        try {
            courseResponseDto = courseMapper.selectCourseByCourseId(courseId);
            courseMapper.updateViewCountByCourseId(courseId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NOT_FOUND_COURSE_INFO);
        }

        // 이미지 정보 가져오기
        try {
            courseResponseDto.setCourseImages(courseMapper.selectCourseImages(courseId));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_COURSE_IMAGES);
        }


        // 추천 관광지 정보 가져오기
        try {
            courseResponseDto.setSpots(spotMapper.selectCourseSpots(courseId));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_SPOT, e);
        }
        return courseResponseDto;
    }

    // -----------------좋아요-----------------
    @Override
    public void likeCourse(int courseId, String userId) {
        try {
            courseLikeMapper.insertLikeCourse(courseId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_LIKE, e);
        }
    }

    @Override
    public void unLikeCourse(int courseId, String userId) {
        try {
            courseLikeMapper.deleteLikeCourse(courseId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UNLIKE, e);
        }
    }

    @Override
    public boolean isLikeCourse(int courseId, String userId) {
        try {
            int result = courseLikeMapper.selectCourseLikeByCourseIdAndUserId(courseId, userId);
            if (result == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_LIKE, e);
        }
    }

    @Override
    public void likeCourseComment(int courseCommentId, String userId) {
        try {
            courseLikeMapper.insertCourseCommentLike(courseCommentId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_LIKE, e);
        }
    }

    @Override
    public void unLikeCourseComment(int courseCommentId, String userId) {
        try {
            courseLikeMapper.deleteCourseCommentLike(courseCommentId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_LIKE, e);
        }
    }

    @Override
    public boolean isLikeCourseComment(int courseCommentId, String userId) {
        try {
            int result = courseLikeMapper.selectCourseCommentLikeByCourseIdAndUserId(courseCommentId, userId);
            if (result == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_LIKE, e);
        }
        return false;
    }

    @Override
    public void registComment(CourseCommentRequestDto commentRequestDto) {
        try {
            int result = courseCommentMapper.insertComment(commentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_COMMENT);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_COMMENT, e);
        }
    }

    @Override
    public void registCommentReply(CourseCommentRequestDto commentRequestDto) {
        try {
            int result = courseCommentMapper.insertCommentReply(commentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_COMMENT);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_COMMENT, e);
        }
    }

    @Override
    public void updateComment(CourseCommentUpdateDto commentRequestDto) {
        try {
            int result = courseCommentMapper.updateComment(commentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE_COMMENT);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE_COMMENT, e);
        }
    }

    @Override
    public void deleteComment(CourseCommentDeleteDto courseCommentDeleteDto) {
        try {
            int result = courseCommentMapper.deleteComment(courseCommentDeleteDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE_COMMENT);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE_COMMENT, e);
        }
    }

    @Override
    public Pagination<CourseCommentResponseDto> getCommentList(int courseId, int page, int size) {
        int offset = (page - 1) * size;
        // 해당 코스의 댓글 목록 가져오기
        List<CourseCommentResponseDto> commentResponseDtos = courseCommentMapper.selectCommentsByCourseId(courseId, offset, size);
        // 전체 개수 파악
        int totalItems = courseCommentMapper.countCommentsByCourseId(courseId); // 총 데이터 수
        // 페이징 객체 반환
        return new Pagination<>(commentResponseDtos, totalItems, page, size);
    }

    @Override
    public Pagination<CourseCommentResponseDto> getCommentReplyList(int courseId, int commentId, int page, int size) {
        int offset = (page - 1) * size;
        // 해당 코스의 댓글 목록 가져오기
        List<CourseCommentResponseDto> commentResponseDtos = courseCommentMapper.selectCommentRepliesByCourseId(courseId, commentId, offset, size);
        // 전체 개수 파악
        int totalItems = courseCommentMapper.countCommentRepliesByCourseId(courseId, commentId); // 총 데이터 수
        // 페이징 객체 반환
        return new Pagination<>(commentResponseDtos, totalItems, page, size);
    }
}