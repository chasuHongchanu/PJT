package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import com.example.trend.course.dto.SpotRequestDto;
import com.example.trend.course.mapper.CourseMapper;
import com.example.trend.course.mapper.SpotMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.FileUtil;
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

    @Override
    @Transactional
    public void registCourse(CourseRegistRequestDto courseRegistRequestDto) {
        // 코스 등록
        try {
            courseMapper.insertCourse(courseRegistRequestDto);
        }catch (Exception e){
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE, e);
        }
        int courseId = courseRegistRequestDto.getCourseId();

        // 등록된 여행 코스 등록
        saveCourseSpots(courseRegistRequestDto.getSpotList(), courseId);

        // 이미지 저장
        saveCourseImages(courseRegistRequestDto.getImageList(), courseId);

    }

    @Override
    public void updateCourse(CourseUpdateRequestDto courseUpdateRequestDto) {
        int courseId = courseUpdateRequestDto.getCourseId();
        // 코스 데이터 업데이트
        try {
            courseMapper.updateCourse(courseUpdateRequestDto);
        }catch (Exception e){
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_COURSE, e);
        }

        // 여행 코스 수정(기존 코스 모두 삭제 후 새로 등록)
        // 기존 경유지 삭제
        spotMapper.deleteCourseSpotByCourseId(courseId);
        // 수정된 경유지 입력
        saveCourseSpots(courseUpdateRequestDto.getSpotList(), courseId);

        // 이미지 수정(db와 storage 모두 기존 파일 삭제 후 새로 등록)
        // 기존 이미지 파일 삭제
        fileUtil.deleteFiles("courses", courseId);
        // 수정할 이미지 저장
        saveCourseImages(courseUpdateRequestDto.getImageList(), courseId);
    }

    private void saveCourseSpots(List<SpotRequestDto> spotList, int courseId) {
        for (SpotRequestDto spotRequestDto : spotList) {
            spotRequestDto.setCourseId(courseId);
            try {
                spotMapper.insertCourseSpot(spotRequestDto);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_SPOT, e);
            }
        }
    }

    private void saveCourseImages(List<MultipartFile> imageList, int courseId) {
        // 파일 이름을 추출하여 itemImageNames 리스트에 추가
        List<String> courseImageNames = imageList.stream()
                .map(file -> "courses/" + courseId + "/" + file.getOriginalFilename())
                .toList();

        // db에 이미지 경로 및 이름 저장
        // db에 물품 이미지 이름 정보 insert
        for(String courseImageName: courseImageNames) {
            courseMapper.insertCourseImageName(courseId, courseImageName);
        }

        // 이미지 저장
        if(!(imageList.size() == 1 && imageList.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("courses", courseId, imageList);
        }
    }
}
