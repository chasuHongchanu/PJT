package com.example.trend.community.mapper;

import com.example.trend.community.dto.ArticleListResponseDto;
import com.example.trend.community.dto.ArticleRegistRequestDto;
import com.example.trend.community.dto.ArticleResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("""
            INSERT INTO article (writer_id, article_title, article_content) 
            VALUES (#{writerId}, #{articleTitle}, #{articleContent})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void insertArticle(ArticleRegistRequestDto requestDto);

    @Insert("INSERT INTO article_image (article_id, article_image) VALUES (#{articleId}, #{articleImageName})")
    void insertArticleImage(int articleId, String articleImageName);

    @Update("""
            UPDATE article
            SET article_title = #{requestDto.articleTitle}, article_content = #{requestDto.articleContent}
            WHERE writer_id = #{userId} AND article_id = #{requestDto.articleId}
            """)
    void updateArticle(@Param("requestDto") ArticleRegistRequestDto requestDto, @Param("userId") String userId);

    @Delete("DELETE FROM article_image WHERE article_id = #{articleId}")
    void deleteArticleImage(int articleId);

    @Delete("""
            DELETE FROM article WHERE article_id = #{articleId} AND writer_id = #{userId}
            """)
    int deleteArticle(int articleId, String userId);

    @Select("""
            SELECT
                a.article_id AS articleId,
                u.user_profile_img AS writerProfileImg,
                a.writer_id AS writerId,
                u.user_nickname AS userNickname,
                COUNT(DISTINCT al.user_id) AS likeCount,
                COUNT(DISTINCT ac.article_comment_id) AS commentCount,
                a.article_title AS articleTitle,
                a.article_content AS articleContent,
                a.thumbnail
            FROM article a
                left join article_comment ac on a.article_id = ac.article_id
                left join article_like al on a.article_id = al.article_id
                left join user u on a.writer_id = u.user_id
            GROUP BY a.article_id
            LIMIT #{size} OFFSET #{offset}
            """)
    List<ArticleListResponseDto> selectAllArticles(int size, int offset);

    @Select("SELECT COUNT(*) FROM course")
    int countAllArticles();

    @Select("""
            SELECT u.user_profile_img AS writerProfileImg,
                   writer_id   AS writerId,
                   u.user_nickname    AS writerNickname,
                   article_title       AS articleTitle,
                   article_content     AS articleContent,
                   article_created_at,
                   view_count,
                   (SELECT COUNT(*)
                    FROM article_like
                    WHERE article_id = article.article_id) AS likesCount,
                   (SELECT COUNT(*)
                    FROM article_comment
                    WHERE article_id = article.article_id AND parent_comment_id IS NULL) AS commentCount
            FROM article
                     LEFT JOIN user u
                               on writer_id = u.user_id
            WHERE article_id = #{articleId}
            """)
    ArticleResponseDto selectArticleByArticleId(int articleId);

    @Select("""
            SELECT article_image
            FROM article_image WHERE article_id = #{articleId}
            """)
    List<String> selectArticleImages(int articleId);

    @Update("""
            UPDATE article
            SET thumbnail = #{thumbnail}
            WHERE article_id = #{articleId}
            """)
    void insertThumbnail(String thumbnail, int articleId);

    @Update("""
            UPDATE article
            SET view_count = view_count + 1
            WHERE article_id = #{articleId}
            """)
    void updateViewCountByArticleId(int articleId);
}
