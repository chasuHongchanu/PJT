package com.example.trend.community.mapper;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import com.example.trend.community.dto.comment.ArticleCommentResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleCommentMapper {
    @Insert("""
            INSERT INTO article_comment(article_id,comment_writer_id, article_comment_content)
            VALUES (#{articleId},#{userId}, #{content})
            """)
    int insertComment(ArticleCommentRequestDto articleCommentRequestDto);

    @Insert("""
            INSERT INTO article_comment(article_id,comment_writer_id, parent_comment_id, article_comment_content)
            VALUES (#{articleId},#{userId}, #{parentsCommentId}, #{content})
            """)
    int insertReply(ArticleCommentRequestDto articleCommentRequestDto);

    @Update("""
            UPDATE article_comment
            SET article_comment_content = #{content}
            WHERE article_comment_id = #{commentId} AND comment_writer_id = #{userId}
            """)
    int updateComment(ArticleCommentRequestDto articleCommentRequestDto);

    @Delete("""
            DELETE FROM article_comment
            WHERE article_comment_id = #{commentId} AND comment_writer_id = #{userId}
            """)
    int deleteComment(int commentId, String userId);

    @Select("""
            SELECT article_comment_id    AS commentId,
                comment_writer_id   AS writerId,
                u.user_nickname     AS writerNickname,
                u.user_profile_img  AS writerProfileImg,
                parent_comment_id  AS parentsCommentId,
                article_comment_content  AS content,
                article_comment_created_at   AS createdAt,
                (SELECT COUNT(*)
                 FROM article_comment_like
                 WHERE article_comment_id = commentId) AS likesCount
            FROM article_comment a
                  LEFT JOIN user u
                            on a.comment_writer_id = u.user_id
            WHERE article_id = #{articleId} AND parent_comment_id IS NULL
            LIMIT #{size} OFFSET #{offset}
            """)
    List<ArticleCommentResponseDto> selectCommentsByArticleId(int articleId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM article_comment
            WHERE article_id = #{articleId} AND parent_comment_id IS NULL
            """)
    int countCommentsByArticleId(int articleId);

    @Select("""
            SELECT article_comment_id    AS commentId,
                comment_writer_id   AS writerId,
                u.user_nickname     AS writerNickname,
                u.user_profile_img  AS writerProfileImg,
                parent_comment_id  AS parentsCommentId,
                article_comment_content  AS content,
                article_comment_created_at   AS createdAt,
                (SELECT COUNT(*)
                 FROM article_comment_like
                 WHERE article_comment_id = commentId) AS likesCount
            FROM article_comment a
                  LEFT JOIN user u
                            on a.comment_writer_id = u.user_id
            WHERE parent_comment_id = #{commentId}
            LIMIT #{size} OFFSET #{offset}
            """)
    List<ArticleCommentResponseDto> selectCommentRepliesByArticleId(int commentId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM article_comment
            WHERE parent_comment_id = #{commentId}
            """)
    int countCommentRepliesByArticleId(int commentId);

    @Select("""
            SELECT article_id
            FROM article_comment
            WHERE article_comment_id = #{parentsCommentId}
            """)
    int selectArticleIdByParentsCommentId(int parentsCommentId);

    @Select("""
            SELECT article_id
            FROM article_comment
            WHERE article_comment_id = #{commentId}
            """)
    int selectArticleIdByCommentId(int commentId);
}
