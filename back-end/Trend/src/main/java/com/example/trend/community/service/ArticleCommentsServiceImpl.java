package com.example.trend.community.service;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import com.example.trend.community.mapper.ArticleCommentMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentsServiceImpl implements ArticleCommentService{
    private final ArticleCommentMapper articleCommentMapper;

    @Override
    public void registComment(ArticleCommentRequestDto articleCommentRequestDto) {
        try {
            int result = articleCommentMapper.insertComment(articleCommentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_ARTICLE_COMMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_ARTICLE_COMMENT);
        }
    }

    @Override
    public void registReply(ArticleCommentRequestDto articleCommentRequestDto) {
        try {
            int result = articleCommentMapper.insertReply(articleCommentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_REGIST_ARTICLE_COMMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_REGIST_ARTICLE_COMMENT);
        }
    }

    @Override
    public void updateComment(ArticleCommentRequestDto articleCommentRequestDto) {
        try {
            int result = articleCommentMapper.updateComment(articleCommentRequestDto);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_UPDATE_ARTICLE_COMMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_UPDATE_ARTICLE_COMMENT);
        }
    }

    @Override
    public void deleteComment(int commentId, String userId) {
        try {
            int result = articleCommentMapper.deleteComment(commentId, userId);
            if (result != 1) {
                throw new CustomException(ErrorCode.FAIL_TO_DELETE_ARTICLE_COMMENT);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_ARTICLE_COMMENT, e);
        }
    }
}
