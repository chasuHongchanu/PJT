package com.example.trend.trade.service;

import com.example.trend.trade.dto.*;
import jakarta.validation.Valid;

public interface TradeService {
    TradeReservationResponseDto getReservationInfo(TradeReservationRequestDto tradeReservationRequestDto);

    int registReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto);

    int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto);

    TradeDetailResponseDto getTradeDetailInfo(int tradeId);

    int registItemImages(@Valid TradeImageRegistRequestDto tradeImageRegistRequestDto);

    TradeDetailResponseDto getTradePaymentInfo(int tradeId);

    int updatePaymentStatus(int tradeId);

    int deleteReservation(int tradeId);

    TradeReviewResponseDto getTradeInfoForReview(int tradeId);

    int registReview(TradeReviewRequestDto tradeReviewRequestDto);

    int updatetradeState(int tradeId);
}
