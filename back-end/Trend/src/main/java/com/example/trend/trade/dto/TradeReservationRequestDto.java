package com.example.trend.trade.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TradeReservationRequestDto {
    private int itemId;
    private String lessorId;
    private String lesseeId;
}
