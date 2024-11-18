package com.example.trend.course.mapper;

import com.example.trend.course.dto.SpotRequestDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpotMapper {
    @Insert("INSERT INTO course_spot (course_id, spot_id, visit_order)" +
            "VALUES (#{courseId}, #{spotId}, #{visitOrder})")
    void insertCourseSpot(SpotRequestDto spotRequestDto);

    @Delete("DELETE FROM course_spot WHERE course_id = #{courseId}")
    void deleteCourseSpotByCourseId(int courseId);
}
