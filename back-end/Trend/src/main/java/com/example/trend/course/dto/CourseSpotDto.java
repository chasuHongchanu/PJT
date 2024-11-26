package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSpotDto {
    // 관광지 정보를 담아 요청받을 DTO
    private int courseSpotId;
    private int spotId;
    private int courseId;
    private String spotName;
    private int visitOrder;
    private String address;
    private double latitude;
    private double longitude;
}
