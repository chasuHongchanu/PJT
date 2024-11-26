package com.example.trend.course.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CourseRegistRequestDto {
    private Integer courseId;
    private String courseWriterId;
    @NotBlank(message = "제목은 필수입니다.")
    private String courseTitle;
    @NotBlank(message = "내용은 필수입니다.")
    private String courseContent;
    @NotBlank(message = "주소 입력은 필수입니다.")
    private String region;
    private String spotListJson;
    private List<CourseSpotDto> spotList;
}
