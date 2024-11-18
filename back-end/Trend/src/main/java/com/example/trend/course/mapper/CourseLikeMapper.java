package com.example.trend.course.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseLikeMapper {
    @Insert("INSERT INTO course_like VALUES (#{courseId}, #{userId})")
    void insertLikeCourse(int courseId, String userId);

}
