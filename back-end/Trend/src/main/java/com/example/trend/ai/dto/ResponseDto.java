package com.example.trend.ai.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseDto {
    private int itemId;
    private String itemName;
    private String reason;
}
