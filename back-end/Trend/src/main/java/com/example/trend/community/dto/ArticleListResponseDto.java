package com.example.trend.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListResponseDto {
    private int articleId;
    private String writerProfileImg;
    private String writerId;
    private String userNickname;
    private String articleTitle;
    private String articleContent;
    private int likeCount;
    private int commentCount;
}
