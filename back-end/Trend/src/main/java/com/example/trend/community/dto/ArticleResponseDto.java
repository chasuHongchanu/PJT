package com.example.trend.community.dto;

import com.example.trend.course.dto.CourseSpotDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class ArticleResponseDto {
    private String writerProfileImg;
    private String writerId;
    private String writerNickname;
    private String articleTitle;
    private String articleContent;
    private Timestamp articleCreatedAt;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private List<String> articleImages;
}
