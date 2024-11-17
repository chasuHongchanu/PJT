package com.example.trend.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotRequestDto {
    // 관광지 정보를 담아 요청받을 DTO
    // open API를 통해 관광지 정보를 불러와 저장하는 것이 우선.
    private int courseId;
    private int spotId;
    private int visitOrder;
}
