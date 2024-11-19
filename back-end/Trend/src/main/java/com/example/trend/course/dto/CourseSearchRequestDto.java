package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSearchRequestDto {
    private String keyword;
    private String province;
    private String district;
    private String town;
    private String startDate;
    private String endDate;
    private String orderBy;
}
