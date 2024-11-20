package com.example.trend.community.service;

import com.example.trend.community.dto.comment.ArticleCommentRequestDto;
import com.example.trend.community.dto.comment.ArticleCommentResponseDto;
import com.example.trend.community.mapper.ArticleCommentMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
            int articleId = articleCommentMapper.selectArticleIdByParentsCommentId(articleCommentRequestDto.getParentsCommentId());
            articleCommentRequestDto.setArticleId(articleId);
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
            int articleId = articleCommentMapper.selectArticleIdByCommentId(articleCommentRequestDto.getCommentId());
            articleCommentRequestDto.setArticleId(articleId);
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

    @Override
    public Pagination<ArticleCommentResponseDto> getCommentList(int articleId, int page, int size) {
        int offset = (page - 1) * size;
        // 해당 코스의 댓글 목록 가져오기
        List<ArticleCommentResponseDto> articleCommentResponseDtos = articleCommentMapper.selectCommentsByArticleId(articleId, offset, size);
        // 전체 개수 파악
        int totalItems = articleCommentMapper.countCommentsByArticleId(articleId); // 총 데이터 수
        // 페이징 객체 반환
        return new Pagination<>(articleCommentResponseDtos, totalItems, page, size);
    }

    @Override
    public Pagination<ArticleCommentResponseDto> getCommentReplyList(int commentId, int page, int size) {
        int offset = (page - 1) * size;
        // 해당 코스의 댓글 목록 가져오기
        List<ArticleCommentResponseDto> articleCommentResponseDtos = articleCommentMapper.selectCommentRepliesByArticleId(commentId, offset, size);
        // 전체 개수 파악
        int totalItems = articleCommentMapper.countCommentRepliesByArticleId(commentId); // 총 데이터 수
        // 페이징 객체 반환
        return new Pagination<>(articleCommentResponseDtos, totalItems, page, size);
    }
}
