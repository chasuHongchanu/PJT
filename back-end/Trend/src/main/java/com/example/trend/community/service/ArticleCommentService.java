package com.example.trend.community.service;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;

public interface ArticleCommentService {
    void registComment(ArticleCommentRequestDto articleCommentRequestDto);

    void registReply(ArticleCommentRequestDto articleCommentRequestDto);

    void updateComment(ArticleCommentRequestDto articleCommentRequestDto);

    void deleteComment(int commentId, String userId);
}
