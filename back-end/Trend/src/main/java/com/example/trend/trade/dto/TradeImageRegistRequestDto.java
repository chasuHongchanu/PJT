package com.example.trend.trade.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TradeImageRegistRequestDto {
    private int tradeId;
    private List<MultipartFile> itemConditionImages;
}
