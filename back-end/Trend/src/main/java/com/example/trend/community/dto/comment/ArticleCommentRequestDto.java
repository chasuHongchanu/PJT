package com.example.trend.community.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCommentRequestDto {
    private int articleId;
    private int commentId;
    private String userId;
    private int parentsCommentId;
    private String content;
}
