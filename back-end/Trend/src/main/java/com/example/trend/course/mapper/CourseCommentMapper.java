package com.example.trend.course.mapper;

import com.example.trend.course.dto.CourseCommentRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseCommentMapper {
    @Insert("INSERT INTO course_comment (course_id, comment_writer_id, course_comment_content)" +
            "VALUES (#{courseId}, #{userId}, #{content})")
    int insertComment(CourseCommentRequestDto commentRequestDto);

    @Insert("INSERT INTO course_comment (course_id, comment_writer_id, parents_comment_id, course_comment_content)" +
            "VALUES (#{courseId}, #{userId}, #{parentsCommentId}, #{content})")
    int insertCommentReply(CourseCommentRequestDto commentRequestDto);
}
