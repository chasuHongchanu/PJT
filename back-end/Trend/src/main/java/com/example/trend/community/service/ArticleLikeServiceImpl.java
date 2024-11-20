package com.example.trend.community.service;

import com.example.trend.community.mapper.ArticleLikeMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleLikeServiceImpl implements ArticleLikeService {
    private final ArticleLikeMapper articleLikeMapper;

    /*
    좋아요(게시물)
     */
    @Override
    public void likeAticle(int articleId, String userId) {
        try {
            articleLikeMapper.insertLikeArticle(articleId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_LIKE, e);
        }
    }

    @Override
    public void unLikeArticle(int articleId, String userId) {
        try {
            articleLikeMapper.deleteLikeArticle(articleId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_UNLIKE, e);
        }
    }

    @Override
    public boolean isLikeArticle(int articleId, String userId) {
        try {
            int result = articleLikeMapper.selectArticleLikeByArticleIdAndUserId(articleId, userId);
            if (result == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_LIKE, e);
        }
    }

    /*
    좋아요(댓글)
     */

    @Override
    public void likeArticleComment(int articleCommentId, String userId) {
        try{
            articleLikeMapper.insertLikeArticleComment(articleCommentId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_LIKE, e);
        }
    }

    @Override
    public void unLikeArticleComment(int articleCommentId, String userId) {
        try{
            articleLikeMapper.deleteLikeArticleComment(articleCommentId, userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_LIKE, e);
        }
    }

    @Override
    public boolean isLikeArticleComment(int articleCommentId, String userId) {
        try {
            int result = articleLikeMapper.selectArticleCommentLikeByArticleIdAndUserId(articleCommentId, userId);
            if (result == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_LIKE, e);
        }
        return false;
    }
}
