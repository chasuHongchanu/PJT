package com.example.trend.trade.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.trade.dto.*;
import com.example.trend.trade.mapper.TradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TradeServiceImpl implements TradeService{
    private final TradeMapper tradeMapper;

    @Autowired
    public TradeServiceImpl(TradeMapper tradeMapper) {
        this.tradeMapper = tradeMapper;
    }

    @Override
    public TradeReservationResponseDto getReservationInfo(TradeReservationRequestDto tradeReservationRequestDto) {
        return tradeMapper.selectReservationInfo(tradeReservationRequestDto);
    }

    @Transactional
    @Override
    public int registReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto) {
        // 시작일이 종료일보다 늦은 경우
        String rentalStartDate = tradeReservationRegistRequestDto.getTradeRentalStartDate();
        String rentalEndDate = tradeReservationRegistRequestDto.getTradeRentalEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(rentalStartDate, formatter);
        LocalDate endDate = LocalDate.parse(rentalEndDate, formatter);

        if(startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_RENTAL_PERIOD);
        }

        // 가상계좌 임의 생성
        Random random = new Random();
        String accountNumber = Integer.toString(random.nextInt(1000000000));
        tradeReservationRegistRequestDto.setPaymentAccountNumber(accountNumber);

        return tradeMapper.insertReservation(tradeReservationRegistRequestDto);
    }

    @Override
    public int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto) {
        // 시작일이 종료일보다 늦은 경우
        String rentalStartDate = tradeReservationUpdateRequestDto.getTradeRentalStartDate();
        String rentalEndDate = tradeReservationUpdateRequestDto.getTradeRentalEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(rentalStartDate, formatter);
        LocalDate endDate = LocalDate.parse(rentalEndDate, formatter);

        if(startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_RENTAL_PERIOD);
        }

        return tradeMapper.updateReservation(tradeReservationUpdateRequestDto);
    }

    @Override
    public TradeDetailResponseDto getTradeDetailInfo(int tradeId) {
        TradeDetailResponseDto tradeDetailResponseDto = tradeMapper.selectTradeDetail(tradeId);
        List<String> itemConditionImages = tradeMapper.selectItemConditionImages(tradeId);
        tradeDetailResponseDto.setItemConditionImages(itemConditionImages);

        return tradeDetailResponseDto;
    }
}
