package com.example.trend.community.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCommentResponseDto {
    private int commentId;
    private String writerId;
    private String writerNickname;
    private String writerProfileImg;
    private String content;
    private String createdAt;
    private Integer parentsCommentId;
    private Integer likesCount;
}
