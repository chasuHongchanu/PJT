package com.example.trend.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 인젝션 방지를 위한 정렬 방법 설정
@Getter
@AllArgsConstructor
public enum OrderBy {
    DATE("a.article_created_at"),
    LIKES("likeCount"),
    COMMENTS("commentCount");

    private final String column;
}
