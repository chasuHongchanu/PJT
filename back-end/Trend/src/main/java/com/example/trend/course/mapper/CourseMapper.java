package com.example.trend.course.mapper;

import com.example.trend.course.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO course (course_writer_id, course_title, course_content, region) VALUES (#{courseWriterId}, #{courseTitle}, #{courseContent}, #{region})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    void insertCourse(CourseRegistRequestDto courseRegistRequestDto);

    @Update("UPDATE course " +
            "SET course_title = #{courseTitle}, course_content = #{courseContent}, address = #{address}" +
            "WHERE course_id = #{courseId} AND course_writer_id = #{courseWriterId}")
    int updateCourse(CourseUpdateRequestDto courseUpdateRequestDto);

    @Insert("INSERT INTO course_image (course_id, course_img) VALUES (#{courseId},#{courseImageName})")
    void insertCourseImage(int courseId, String courseImageName);

    @Delete("DELETE FROM course WHERE course_id = #{courseId} AND course_writer_id = #{userId}")
    int deleteCourse(int courseId, String userId);

    @Select("SELECT course_writer_id FROM course WHERE course_id = #{courseId}")
    String selectWriterIdByCourseId(int courseId);

    @Delete("DELETE FROM course_image WHERE course_id = #{courseId}")
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
                c.course_content AS courseContent,
                c.thumbnail
            FROM course c
                left join course_comment cc on c.course_id = cc.course_id
                left join course_like cl on c.course_id = cl.course_id
                left join user u on c.course_writer_id = u.user_id
            GROUP BY c.course_id
            LIMIT #{size} OFFSET #{offset}
            """)
    List<CourseListResponseDto> selectAllCourse(@Param("size") int size, @Param("offset") int offset); // 코스 전체 조회

    @Select("""
            SELECT u.user_profile_img AS writerProfileImg,
                   course_writer_id   AS writerId,
                   u.user_nickname    AS writerNickname,
                   course_title       AS courseTitle,
                   course_content     AS courseContent,
                    region,
                   course_created_at,
                   view_count,
                   (SELECT COUNT(*)
                    FROM course_like
                    WHERE course_id = course.course_id) AS likesCount,
                   (SELECT COUNT(*)
                    FROM course_comment
                    WHERE course_id = course.course_id AND parent_comment_id IS NULL) AS commentCount
            FROM course
                     LEFT JOIN user u
                               on course_writer_id = u.user_id
            WHERE course_id = #{courseId}
            """)
    CourseResponseDto selectCourseByCourseId(int courseId); // 상세조회

    @Select("""
            SELECT course_img
            FROM course_image WHERE course_id = #{courseId}
            """)
    List<String> selectCourseImages(int courseId);

    @Select("""
            SELECT COUNT(*) AS totalCount
            FROM course;
            """)
    int countAllCourse(); // 전체 코스 개수

    @Select("""
    <script>
    SELECT
        c.course_id                          AS courseId,
        u.user_profile_img                   AS userProfileImg,
        c.course_writer_id                   AS userId,
        u.user_nickname                      AS userNickname,
        COUNT(DISTINCT cl.user_id)           AS likeCount,
        COUNT(DISTINCT cc.course_comment_id) AS commentCount,
        c.course_title                       AS courseTitle,
        c.course_content                     AS courseContent,
        c.thumbnail
    FROM course c
        LEFT JOIN course_comment cc ON c.course_id = cc.course_id
        LEFT JOIN course_like cl ON c.course_id = cl.course_id
        LEFT JOIN user u ON c.course_writer_id = u.user_id
    WHERE 1=1
        <if test="params.keyword != null and params.keyword.trim() != ''">
            AND (c.course_title LIKE CONCAT('%', #{params.keyword}, '%')
                 OR c.course_content LIKE CONCAT('%', #{params.keyword}, '%'))
        </if>
        <if test="params.province != null and params.province.trim() != ''">
            AND c.province = #{params.province}
        </if>
        <if test="params.district != null and params.district.trim() != ''">
            AND c.district = #{params.district}
        </if>
        <if test="params.town != null and params.town.trim() != ''">
            AND c.town = #{params.town}
        </if>
        <if test="params.startDate != null and params.startDate.trim() != ''">
            AND <![CDATA[c.course_created_at >= #{params.startDate}]]>
        </if>
        <if test="params.endDate != null and params.endDate.trim() != ''">
            AND <![CDATA[c.course_created_at <= #{params.endDate}]]>
        </if>
    GROUP BY c.course_id
    <if test="params.orderBy != null and params.orderBy.trim() != ''">
        ORDER BY ${params.orderBy}
    </if>
    LIMIT #{size} OFFSET #{offset}
    </script>
    """)
    List<CourseListResponseDto> searchCourses(@Param("params") CourseSearchRequestDto searchRequest,
                                              @Param("size") int size,
                                              @Param("offset") int offset);

    @Select("""
        <script>
        SELECT COUNT(DISTINCT c.course_id) AS totalCount
        FROM course c
                LEFT JOIN course_comment cc ON c.course_id = cc.course_id
                LEFT JOIN course_like cl ON c.course_id = cl.course_id
                LEFT JOIN user u ON c.course_writer_id = u.user_id
            WHERE 1=1
                <if test="searchRequest.keyword != null and searchRequest.keyword.trim() != ''">
                    AND (c.course_title LIKE CONCAT('%', #{searchRequest.keyword}, '%')
                         OR c.course_content LIKE CONCAT('%', #{searchRequest.keyword}, '%'))
                </if>
                <if test="searchRequest.province != null and searchRequest.province.trim() != ''">
                    AND c.province = #{searchRequest.province}
                </if>
                <if test="searchRequest.district != null and searchRequest.district.trim() != ''">
                    AND c.district = #{searchRequest.district}
                </if>
                <if test="searchRequest.town != null and searchRequest.town.trim() != ''">
                    AND c.town = #{searchRequest.town}
                </if>
                <if test="searchRequest.startDate != null and searchRequest.startDate.trim() != ''">
                    AND <![CDATA[c.course_created_at >= #{searchRequest.startDate}]]>
                </if>
                <if test="searchRequest.endDate != null and searchRequest.endDate.trim() != ''">
                    AND <![CDATA[c.course_created_at <= #{searchRequest.endDate}]]>
                </if>
            GROUP BY c.course_id
        </script>
        """)
    int countSearchCourse(@Param("searchRequest") CourseSearchRequestDto searchRequest);

    @Update("""
            UPDATE course
            SET thumbnail = #{thumbnail}
            WHERE course_id = #{courseId}
            """)
    void insertThumbnail(String thumbnail, int courseId);

    @Select("""
            SELECT COUNT(*)
            FROM course
            WHERE course_id = #{courseId}
            """)
    int countCourseByCourseId(int courseId);

    @Update("""
            UPDATE course
            SET view_count = view_count + 1
            WHERE course_id = #{courseId}
            """)
    void updateViewCountByCourseId(int courseId);

    @Select("""
SELECT *
FROM spot
WHERE spot_name like CONCAT('%', #{keyWord}, '%') OR
      spot_address like CONCAT('%', #{keyWord}, '%')
""")
    List<SpotDto> searchSpot(String keyWord);
}
