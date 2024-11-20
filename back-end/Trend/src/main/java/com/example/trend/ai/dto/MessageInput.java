package com.example.trend.ai.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageInput {
    @NotEmpty(message = "입력 값은 필수입니다.")
    private String query;
    private String userId;
    private int roomId;
}
