package com.example.trend.trade.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.trade.dto.*;
import com.example.trend.trade.mapper.TradeMapper;
import com.example.trend.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TradeServiceImpl implements TradeService{
    private final TradeMapper tradeMapper;
    private final FileUtil fileUtil;

    @Autowired
    public TradeServiceImpl(TradeMapper tradeMapper, FileUtil fileUtil) {
        this.tradeMapper = tradeMapper;
        this.fileUtil = fileUtil;
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

    @Transactional
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

    @Transactional
    @Override
    public int deleteReservation(int tradeId) {
        // storage에서 삭제
        fileUtil.deleteFiles("trade", tradeId);

        // db에서 삭제
        int result = tradeMapper.deleteReservation(tradeId);
        
        return result;
    }

    @Override
    public TradeDetailResponseDto getTradeDetailInfo(int tradeId) {
        TradeDetailResponseDto tradeDetailResponseDto = tradeMapper.selectTradeDetail(tradeId);
        List<String> itemConditionImages = tradeMapper.selectItemConditionImages(tradeId);
        tradeDetailResponseDto.setItemConditionImages(itemConditionImages);

        return tradeDetailResponseDto;
    }

    @Transactional
    @Override
    public int registItemImages(TradeImageRegistRequestDto tradeImageRegistRequestDto) {
        int tradeId = tradeImageRegistRequestDto.getTradeId();
        List<String> itemConditionImageNames = tradeImageRegistRequestDto.getItemConditionImages().stream()
                .map(file -> "trade/" + tradeId + "/" + file.getOriginalFilename())
                .toList();

        // storage에 이미지 파일 저장
        List<MultipartFile> files = tradeImageRegistRequestDto.getItemConditionImages();
        if(!(files.size() == 1 && files.get(0).isEmpty())) {
            fileUtil.saveFilesIntoStorage("trade", tradeId, files);
        }
        // 이미지가 한 장도 들어오지 않은 경우
        else {
            throw new CustomException(ErrorCode.NO_ITEM_CONDITION_IMAGES);
        }

        // DB에 이미지 이름 저장
        int result = tradeMapper.insertItemConditionImages(tradeId, itemConditionImageNames);

        // 거래 상태: 대여 전 -> 대여 시작
        tradeMapper.updateTradeState(tradeId);

        return result;
    }

    @Override
    public TradeDetailResponseDto getTradePaymentInfo(int tradeId) {
        return tradeMapper.selectTradeDetail(tradeId);
    }

    @Override
    public int updatePaymentStatus(int tradeId) {
        return tradeMapper.updatePaymentStatus(tradeId);
    }

}
