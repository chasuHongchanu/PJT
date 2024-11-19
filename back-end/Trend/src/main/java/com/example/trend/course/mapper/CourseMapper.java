package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseListResponseDto;
import com.example.trend.course.dto.CourseRegistRequestDto;
import com.example.trend.course.dto.CourseResponseDto;
import com.example.trend.course.dto.CourseUpdateRequestDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    void insertCourseImage(int courseId, String courseImageName);

    @Delete("DELETE FROM course WHERE course_id = #{courseId} AND course_writer_id = #{userId}")
    int deleteCourse(int courseId, String userId);

    @Select("SELECT course_writer_id FROM course WHERE course_id = #{courseId}")
    String selectWriterIdByCourseId(int courseId);

    @Delete("DELETE FROM course_image WHERE course_id = #{courscId}")
    void deleteCourseImage(int courseId);

    @Select("""
            SELECT
                c.course_id AS courseId,
                u.user_profile_img AS userProfileImg,
                c.course_writer_id AS userId,
                u.user_nickname AS userNickname,
                COUNT(DISTINCT cl.user_id) AS likeCount,
                COUNT(DISTINCT cc.course_comment_id) AS commentCount,
                c.course_title AS courseTitle,
                c.course_content AS courseContent
            FROM course c
                left join course_comment cc on c.course_id = cc.course_id
                left join course_like cl on c.course_id = cl.course_id
                left join user u on c.course_writer_id = u.user_id
            GROUP BY c.course_id
            LIMIT #{size} OFFSET #{offset}
            """)
    List<CourseListResponseDto> selectAllCourse(@Param("size") int size, @Param("offset") int offset);

    @Select("""
            SELECT u.user_profile_img AS writerProfileImg,
                   course_writer_id   AS writerId,
                   u.user_nickname    AS writerNickname,
                   course_title       AS courseTitle,
                   course_content     AS courseContent,
                   course_created_at,
                   view_count,
                   (SELECT COUNT(*)
                    FROM course_like
                    WHERE course_id = course.course_id) AS likesCount,
                   (SELECT COUNT(*)
                    FROM course_comment
                    WHERE course_id = course.course_id) AS commentCount
            FROM course
                     LEFT JOIN user u
                               on course_writer_id = u.user_id
            WHERE course_id = 1
            """)
    CourseResponseDto selectCourseByCourseId(int courseId);

    @Select("""
            SELECT course_img
            FROM course_image WHERE course_id = #{courseId}
            """)
    List<String> selectCourseImages(int courseId);

    @Select("""
            SELECT COUNT(*) AS totalCount
            FROM course;
            """)
    int countAllCourse();
}
