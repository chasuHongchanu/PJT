package com.example.trend.trade.controller;

import com.example.trend.trade.dto.*;
import com.example.trend.trade.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * 채팅방에서 거래 예약 버튼을 누르면 요청되는 api 입니다.
     * itemId를 받아 관련된 정보를 responseDTO에 저장하고, frontend로 전달합니다.
     *
     * @param: itemId, lessorId, lesseeId
     * @return: itemId, itemAvailableRentalStartDate, itemAvailableRentalEndDate, itemName, itemPrice, itemAddress
     */
    @GetMapping("/reservation")
    public ResponseEntity<?> tradeReservation(@ModelAttribute TradeReservationRequestDto tradeReservationRequestDto) {
        TradeReservationResponseDto tradeReservationResponseDto = tradeService.getReservationInfo(tradeReservationRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tradeReservationResponseDto);
    }

    /**
     * 거래 예약 신청 페이지에서 예약 신청을 누르면 요청되는 api 입니다.
     * 대여료,보증금, 대여 기간을 입력받아 새로운 거래를 생성합니다.
     *
     * @param:
     * @return: 거래 생성 완료 message | 거래 생성 실패 message
     */
    @PostMapping("/reservation")
    public ResponseEntity<?> tradeReservationRegist(@Valid @ModelAttribute("tradeReservationRegistRequestDto") TradeReservationRegistRequestDto tradeReservationRegistRequestDto) {
        int result = tradeService.registReservation(tradeReservationRegistRequestDto);

        Map<String, Object> response = new HashMap<>();
        if(result == 1) {
            response.put("message", "성공적으로 등록되었습니다.");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        }
        else {
            response.put("message", "등록에 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    /**
     * 거래 예약 상세 페이지에서 예약 변경을 누르면 요청되는 api 입니다.
     * 대여료,보증금, 대여 기간을 다시 입력받아 기존 거래 예약을 수정합니다.
     *
     * @param: 대여료, 보증금, 대여 시작 기간, 대여 종료 기간
     * @return: 거래 수정 완료 message | 거래 수정 실패 message
     */
    @PutMapping("/reservation")
    public ResponseEntity<?> tradeReservationUpdate(@Valid @ModelAttribute("tradeReservationUpdateRequestDto") TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto) {
        int result = tradeService.updateReservation(tradeReservationUpdateRequestDto);

        Map<String, Object> response = new HashMap<>();
        if(result == 1) {
            response.put("message", "성공적으로 수정되었습니다.");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        }
        else {
            response.put("message", "수정에 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
