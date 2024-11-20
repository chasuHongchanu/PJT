package com.example.trend.community.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleLikeMapper {
    /*
    게시물 좋아요 기능
     */
    @Insert("INSERT INTO article_like VALUES (#{userId}, #{articleId})")
    void insertLikeArticle(int articleId, String userId);
    @Delete("DELETE FROM article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    void deleteLikeArticle(int articleId, String userId);

    @Select("SELECT COUNT(*) FROM article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    int selectArticleLikeByArticleIdAndUserId(int articleId, String userId);

    @Select("SELECT COUNT(*) FROM article_like WHERE article_id = #{articleId}")
    int selectArticleLikeCountByArticleId(int articleId);

    /*
    게시물 댓글 좋아요 기능
     */
    @Insert("INSERT INTO article_comment_like VALUES (#{articleCommentId}, #{userId})")
    void insertLikeArticleComment(int articleCommentId, String userId);

    @Delete("DELETE FROM article_comment_like WHERE article_comment_id = #{articleCommentId} AND user_id = #{userId}")
    void deleteLikeArticleComment(int articleCommentId, String userId);

    @Select("SELECT COUNT(*) FROM article_comment_like WHERE article_comment_id = #{articleCommentId} AND user_id = #{userId}")
    int selectArticleCommentLikeByArticleIdAndUserId(int articleCommentId, String userId);

    @Select("SELECT COUNT(*) FROM article_comment_like WHERE article_comment_id = #{articleCommentId}")
    int selectArticleCommentLikeCountByArticleId(int articleCommentId);
}
