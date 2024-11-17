package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseRegistRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO course (course_writer_id, course_title, course_content, province, district, town) VALUES (#{courseWriterId}, #{courseTitle}, #{courseContent}, #{province}, #{district}, #{town})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    void insertCourse(CourseRegistRequestDto courseRegistRequestDto);
}
