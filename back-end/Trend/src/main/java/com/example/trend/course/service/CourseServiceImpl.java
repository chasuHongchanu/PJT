package com.example.trend.course.service;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.SpotRequestDto;
import com.example.trend.course.mapper.CourseMapper;
import com.example.trend.course.mapper.SpotMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;
    private final SpotMapper spotMapper;

    @Override
    @Transactional
    public void registCourse(CourseRegistRequestDto courseRegistRequestDto) {
        // 코스 등록
        try {
            courseMapper.insertCourse(courseRegistRequestDto);
        }catch (Exception e){
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE, e);
        }

        // 등록된 여행 코스 등록
        for (SpotRequestDto spotRequestDto : courseRegistRequestDto.getSpotList()) {
            spotRequestDto.setCourseId(courseRegistRequestDto.getCourseId());
            try {
                spotMapper.insertCourseSpot(spotRequestDto);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_COURSE_SPOT, e);
            }

        }
    }
}
