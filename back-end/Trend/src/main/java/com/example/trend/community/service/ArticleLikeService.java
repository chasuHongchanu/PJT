package com.example.trend.community.service;

public interface ArticleLikeService {
    void likeAticle(int articleId, String userId);

    void unLikeArticle(int articleId, String userId);

    boolean isLikeArticle(int articleId, String userId);

    void likeArticleComment(int articleCommentId, String userId);

    void unLikeArticleComment(int articleCommentId, String userId);

    boolean isLikeArticleComment(int articleCommentId, String userId);
}
