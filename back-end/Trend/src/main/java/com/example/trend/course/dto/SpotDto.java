package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotDto {
    private String spotId;
    private String spotName;
    private String spotAddress;
    private double longitude;
    private double latitude;
}
