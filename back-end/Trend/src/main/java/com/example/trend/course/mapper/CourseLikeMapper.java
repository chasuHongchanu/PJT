package com.example.trend.course.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseLikeMapper {
    @Insert("INSERT INTO course_like VALUES (#{courseId}, #{userId})")
    void insertLikeCourse(int courseId, String userId);

    @Delete("DELETE FROM course_like WHERE course_id = #{courseId} AND user_id = #{userId}")
    void deleteLikeCourse(int courseId, String userId);

    @Select("SELECT COUNT(*) FROM course_like WHERE course_id = #{courseId} AND user_id = #{userId}")
    int selectCourseLikeByCourseIdAndUserId(int courseId, String userId);

    @Insert("INSERT INTO course_comment_like VALUES (#{commentId}, #{userId})")
    void insertCourseCommentLike(int commentId, String userId);

    @Delete("DELETE FROM course_comment_like WHERE course_comment_id = #{courseCommentId} AND user_id = #{userId}")
    void deleteCourseCommentLike(int courseCommentId, String userId);

    @Select("SELECT COUNT(*) FROM course_comment_like WHERE course_comment_id = #{courseCommentId} AND user_id = #{userId}")
    int selectCourseCommentLikeByCourseIdAndUserId(int courseCommentId, String userId);
}
