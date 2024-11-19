package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseCommentDeleteDto;
import com.example.trend.course.dto.CourseCommentRequestDto;
import com.example.trend.course.dto.CourseCommentUpdateDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseCommentMapper {
    @Insert("INSERT INTO course_comment (course_id, comment_writer_id, course_comment_content)" +
            "VALUES (#{courseId}, #{userId}, #{content})")
    int insertComment(CourseCommentRequestDto commentRequestDto);

    @Insert("INSERT INTO course_comment (course_id, comment_writer_id, parents_comment_id, course_comment_content)" +
            "VALUES (#{courseId}, #{userId}, #{parentsCommentId}, #{content})")
    int insertCommentReply(CourseCommentRequestDto commentRequestDto);

    @Update("""
            UPDATE course_comment
            SET course_comment_content = #{content}
            WHERE course_id = #{courseId} AND course_comment_id = #{commentId} AND comment_writer_id = #{userId}
            """)
    int updateComment(CourseCommentUpdateDto commentRequestDto);

    @Delete("""
            DELETE FROM course_comment
            WHERE course_id = #{courseId} AND course_comment_id = #{commentId} AND comment_writer_id = #{userId}
            """)
    int deleteComment(CourseCommentDeleteDto courseCommentDeleteDto);

    @Select("""
            SELECT COUNT(*)
            FROM course_comment
            WHERE course_id = #{courseId}
            """)
    int countByCourseId(@Param("courseId") String courseId);
}
