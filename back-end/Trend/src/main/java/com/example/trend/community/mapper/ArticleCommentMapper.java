package com.example.trend.community.mapper;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
}
