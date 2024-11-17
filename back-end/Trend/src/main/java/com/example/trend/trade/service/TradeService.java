package com.example.trend.trade.service;

import com.example.trend.trade.dto.*;

public interface TradeService {
    TradeReservationResponseDto getReservationInfo(TradeReservationRequestDto tradeReservationRequestDto);

    int registReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto);

    int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto);

    TradeDetailResponseDto getTradeDetailInfo(int tradeId);
}
