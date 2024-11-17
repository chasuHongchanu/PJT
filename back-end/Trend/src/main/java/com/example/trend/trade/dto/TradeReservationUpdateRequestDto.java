package com.example.trend.trade.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TradeReservationUpdateRequestDto {
    private int tradeId;

    @NotNull(message = "대여료는 필수입니다.")
    @Min(value = -1, message = "대여료는 0원 이상이어야 합니다.")
    private Integer tradePrice;

    @NotNull(message = "보증금은 필수입니다.")
    @Min(value = -1, message = "보증금은 0원 이상이어야 합니다.")
    private Integer tradeDeposit;

    @NotBlank(message = "대여 시작일은 필수입니다.")
    private String tradeRentalStartDate;

    @NotBlank(message = "대여 종료일은 필수입니다.")
    private String tradeRentalEndDate;
}
