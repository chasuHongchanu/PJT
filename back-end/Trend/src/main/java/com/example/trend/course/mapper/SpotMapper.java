package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseSpotDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpotMapper {
    @Insert("INSERT INTO course_spot (spot_id, course_id, spot_name, visit_order, address, latitude, longitude)" +
            "VALUES (#{spotId}, #{courseId}, #{spotName}, #{visitOrder}, #{address}, #{latitude}, #{longitude})")
    void insertCourseSpot(CourseSpotDto courseSpotDto);

    @Delete("DELETE FROM course_spot WHERE course_id = #{courseId}")
    void deleteCourseSpotByCourseId(int courseId);

    // 수정 필요!!
    @Select("""
            SELECT course_spot_id, course_id, spot_name, visit_order, spot_address
            FROM course_spot
            WHERE course_id = #{courseId}
            """)
    List<CourseSpotDto> selectCourseSpots(int courseId);
}
