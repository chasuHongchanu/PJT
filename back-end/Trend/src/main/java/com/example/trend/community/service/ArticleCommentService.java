package com.example.trend.community.service;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import com.example.trend.community.dto.comment.ArticleCommentResponseDto;
import com.example.trend.course.dto.comment.CourseCommentResponseDto;
import com.example.trend.util.Pagination;

public interface ArticleCommentService {
    void registComment(ArticleCommentRequestDto articleCommentRequestDto);

    void registReply(ArticleCommentRequestDto articleCommentRequestDto);

    void updateComment(ArticleCommentRequestDto articleCommentRequestDto);

    void deleteComment(int commentId, String userId);

    Pagination<ArticleCommentResponseDto> getCommentList(int articleId, int page, int size);

    Pagination<ArticleCommentResponseDto> getCommentReplyList(int commentId, int page, int size);
}
