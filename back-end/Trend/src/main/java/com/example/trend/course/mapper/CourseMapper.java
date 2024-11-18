package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO course (course_writer_id, course_title, course_content, province, district, town) VALUES (#{courseWriterId}, #{courseTitle}, #{courseContent}, #{province}, #{district}, #{town})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    void insertCourse(CourseRegistRequestDto courseRegistRequestDto);

    @Update("UPDATE course " +
            "SET course_title = #{courseTitle}, course_content = #{courseContent}, province = #{province}, district = #{district}, town = #{town}" +
            "WHERE course_id = #{courseId} AND course_writer_id = #{courseWriterId}")
    int updateCourse(CourseUpdateRequestDto courseUpdateRequestDto);

    @Insert("INSERT INTO course_image (course_id, course_img) VALUES (#{courseId},#{courseImageName})")
    void insertCourseImageName(int courseId, String courseImageName);

}
