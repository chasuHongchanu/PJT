package com.example.trend.community.service;

import com.example.trend.community.dto.ArticleRegistRequestDto;
import com.example.trend.community.dto.ArticleListResponseDto;
import com.example.trend.community.dto.ArticleResponseDto;
import com.example.trend.util.Pagination;
import jakarta.validation.Valid;

public interface ArticleService {
    void registArticle(@Valid ArticleRegistRequestDto requestDto);

    void updateArticle(@Valid ArticleRegistRequestDto requestDto, String userId);

    void deleteArticle(int articleId, String userId);

    Pagination<ArticleListResponseDto> getAllList(int page, int size);

    ArticleResponseDto getArticle(int articleId);
}
