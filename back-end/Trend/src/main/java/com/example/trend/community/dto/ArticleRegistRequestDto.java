package com.example.trend.community.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ArticleRegistRequestDto {
    private Integer articleId;
    private String writerId;
    @NotBlank(message = "제목은 필수입니다.")
    private String articleTitle;
    @NotBlank(message = "내용은 필수입니다.")
    private String articleContent;
    private List<MultipartFile> imageList;
}
