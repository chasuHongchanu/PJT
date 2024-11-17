package com.example.trend.trade.service;

import com.example.trend.trade.dto.TradeReservationRegistRequestDto;
import com.example.trend.trade.dto.TradeReservationRequestDto;
import com.example.trend.trade.dto.TradeReservationResponseDto;
import com.example.trend.trade.dto.TradeReservationUpdateRequestDto;

public interface TradeService {
    TradeReservationResponseDto getReservationInfo(TradeReservationRequestDto tradeReservationRequestDto);

    int registReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto);

    int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto);
}
