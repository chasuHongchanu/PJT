package com.example.trend.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CourseUpdateRequestDto {
    @NotNull(message = "코스 id 입력은 필수입니다.")
    private int courseId;
    private String courseWriterId;
    @NotBlank(message = "제목은 필수입니다.")
    private String courseTitle;
    @NotBlank(message = "내용은 필수입니다.")
    private String courseContent;
    @NotBlank(message = "시/도 입력은 필수입니다.")
    private String province;
    @NotBlank(message = "시/군/구 입력은 필수입니다.")
    private String district;
    @NotBlank(message = "읍/면/동 입력은 필수입니다.")
    private String town;
    private String spotListJson;
    private List<SpotDto> spotList;
    private List<MultipartFile> imageList;

}
