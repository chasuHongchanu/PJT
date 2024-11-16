package com.example.trend.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSimpleInfo {
    private int articleId;
    private String articleTitle;
    private List<String> articleImages;
}
