package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
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
        for (SpotRequestDto spotRequestDto : courseRegistRequestDto.getSpotList()) {
            spotRequestDto.setCourseId(courseId);
            try {
                spotMapper.insertCourseSpot(spotRequestDto);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_SPOT, e);
            }

        }

        // 파일 이름을 추출하여 itemImageNames 리스트에 추가
        List<String> courseImageNames = courseRegistRequestDto.getImageList().stream()
                .map(file -> "courses/" + courseId + "/" + file.getOriginalFilename())
                .toList();

        // 이미지 저장
        List<MultipartFile> files = courseRegistRequestDto.getImageList();
        if(!(files.size() == 1 && files.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("courses", courseId, files);
        }

        // db에 이미지 경로 및 이름 저장
        // db에 물품 이미지 이름 정보 insert
        for(String courseImageName: courseImageNames) {
            courseMapper.insertCourseImageName(courseId, courseImageName);
        }
    }
}
