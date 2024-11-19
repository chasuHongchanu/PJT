package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseCommentDeleteDto;
import com.example.trend.course.dto.CourseCommentRequestDto;
import com.example.trend.course.dto.CourseCommentResponseDto;
import com.example.trend.course.dto.CourseCommentUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("""
            SELECT course_comment_id    AS commentId,
                comment_writer_id   AS writerId,
                u.user_nickname     AS writerNickname,
                u.user_profile_img  AS writerProfileImg,
                parents_comment_id  AS parentsCommentId,
                course_comment_content  AS content,
                course_comment_created_at   AS createdAt,
                (SELECT COUNT(*)
                 FROM course_comment_like
                 WHERE course_comment_id = commentId) AS likesCount
            FROM course_comment c
                  LEFT JOIN user u
                            on c.comment_writer_id = u.user_id
            WHERE course_id = #{courseId} AND parents_comment_id IS NULL
            LIMIT #{size} OFFSET #{offset}
            """)
    List<CourseCommentResponseDto> selectCommentsByCourseId(@Param("courseId") int courseId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM course_comment
            WHERE course_id = #{courseId} AND parents_comment_id IS NULL
            """)
    int countCommentsByCourseId(int courseId);


    @Select("""
            SELECT course_comment_id    AS commentId,
                comment_writer_id   AS writerId,
                u.user_nickname     AS writerNickname,
                u.user_profile_img  AS writerProfileImg,
                parents_comment_id  AS parentsCommentId,
                course_comment_content  AS content,
                course_comment_created_at   AS createdAt,
                (SELECT COUNT(*)
                 FROM course_comment_like
                 WHERE course_comment_id = commentId) AS likesCount
            FROM course_comment c
                  LEFT JOIN user u
                            on c.comment_writer_id = u.user_id
            WHERE course_id = #{courseId} AND parents_comment_id = #{commentId}
            LIMIT #{size} OFFSET #{offset}
            """)
    List<CourseCommentResponseDto> selectCommentRepliesByCourseId(int courseId, int commentId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM course_comment
            WHERE course_id = #{courseId} AND parents_comment_id = #{commentId}
            """)
    int countCommentRepliesByCourseId(int courseId, int commentId);
}
