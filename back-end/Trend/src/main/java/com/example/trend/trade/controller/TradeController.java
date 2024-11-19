package com.example.trend.trade.controller;

import com.example.trend.trade.dto.*;
import com.example.trend.trade.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "수정에 문제가 발생했습니다.");
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
    @DeleteMapping("/reservation")
    public ResponseEntity<?> tradeReservationDelete(@RequestParam int tradeId) {
        int result = tradeService.deleteReservation(tradeId);

        Map<String, Object> response = new HashMap<>();
        if(result == 1) {
            response.put("message", "성공적으로 삭제되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "삭제에 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }


    /**
     * 거래 예약 신청 및 변경 페이지를 제외한 거래 상세 내용에 대한 api 입니다.
     * 결제 상태, 입금 상태, 대여 시점 사진까지 모두 반환합니다.
     *
     * @param: tradeId
     * @return: 거래에 대한 모든 정보
     */
    @GetMapping("/detail")
    public ResponseEntity<?> tradeDetail(@RequestParam int tradeId) {
        TradeDetailResponseDto tradeDetailResponseDto = tradeService.getTradeDetailInfo(tradeId);

        if(tradeDetailResponseDto == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "정보 조회에 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(tradeDetailResponseDto);
    }

    /**
     * 거래 상세 내역 페이지에서, 임차인이 대여 전 물품 사진을 등록하지 않았을 시 사진 등록 버튼이 활성화됩니다.
     * 사진 리스트를 받아 storage엔 파일을, 데이터베이스엔 사진 이름을 저장합니다.
     *
     * @param: tradeId
     * @return: 이미지 등록 완료 | 이미지 등록 실패 메세지
     */
    @PutMapping("/detail")
    public ResponseEntity<?> imageRegist(@ModelAttribute TradeImageRegistRequestDto tradeImageRegistRequestDto) {
        int result = tradeService.registItemImages(tradeImageRegistRequestDto);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 등록되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "사진 등록 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    /**
     * 거래 상세 내역 페이지에서, 임차인에게 예약 완료 후 나타나는 결제하기 페이지에 필요한 정보를 반환합니다.
     * tradeId를 받아 결제창에 나타나는 정보들을 보여줍니다.
     *
     * @param: tradeId
     * @return:
     */
    @GetMapping("/pay")
    public ResponseEntity<?> tradePaymentView(@RequestParam int tradeId) {
        TradeDetailResponseDto tradeDetailResponseDto = tradeService.getTradePaymentInfo(tradeId);

        if(tradeDetailResponseDto == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "존재하지 않는 결제 정보입니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tradeDetailResponseDto);
        }
    }

    /**
     * 결제 페이지에서, 임차인이 결제 금액을 입력하고 결제하기를 클릭합니다.
     * 결제하기 클릭 시 데이터베이스의 결제 상태가 변경됩니다.
     *
     * @param: tradeId
     * @return:
     */
    @PutMapping("/pay")
    public ResponseEntity<?> tradePay(@RequestParam int tradeId) {
        int result = tradeService.updatePaymentStatus(tradeId);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 결제되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "결제 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    /**
     * 임차인이 물품 거래 종료 후, 후기 등록 버튼을 클릭할 때 나타나는 창에 대한 정보를 반환합니다.
     * 후기 등록을 위한 페이지
     *
     * @param: tradeId
     * @return:
     */
    @GetMapping("/review")
    public ResponseEntity<?> tradeReview(@RequestParam int tradeId) {
        TradeReviewResponseDto tradeReviewResponseDto = tradeService.getTradeInfoForReview(tradeId);
        if(tradeReviewResponseDto == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "존재하지 않는 거래 정보입니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tradeReviewResponseDto);
        }
    }

    /**
     * 임차인이 물품 거래 종료 후, 후기 등록 페이지에서 후기를 등록합니다.
     * 별점 및 후기 본문 내용을 입력으로 받습니다.
     *
     * @param:
     * @return:
     */
    @PostMapping("/review")
    public ResponseEntity<?> tradeReviewRegist(@RequestAttribute("userId") String userId, @ModelAttribute TradeReviewRequestDto tradeReviewRequestDto) {
        tradeReviewRequestDto.setUserId(userId);
        int result = tradeService.registReview(tradeReviewRequestDto);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 등록되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "등록 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    /**
     * 대여 중 상황에서, 물품을 반납받고 임대인은 반납 완료를 클릭합니다.
     * 물품 대여 정보를 대여 중 -> 반납 완료로 변경합니다.
     * 물품 테이블의 물품 상태를 대여 중 -> 대여 가능으로 변경합니다.
     *   - 반납이 완료됐으므로 다시 대여가 가능한 상태
     *
     * @param: tradeId
     * @return:
     */
    @PutMapping("/return")
    public ResponseEntity<?> tradeFinish(@RequestParam int tradeId) {
        int result = tradeService.updatetradeState(tradeId);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 반납 처리 되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "반납 처리 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    /**
     * 로그인 한 유저가 등록한 임대 물품들을 반환합니다.
     *
     * @param userId
     * @return registItems
     */
    @GetMapping("/registItems")
    public ResponseEntity<?> tradeRegistItems(@RequestAttribute("userId") String userId) {
        List<TradeMyItemsResponseDto> registItems = tradeService.getRegistItems(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(registItems);
    }

    @GetMapping("/lendItems")
    public ResponseEntity<?> tradeLendItems(@RequestAttribute("userId") String userId) {
        List<TradeMyItemsResponseDto> lendItems = tradeService.getLendItems(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(lendItems);
    }

    @GetMapping("/leaseItems")
    public ResponseEntity<?> tradeLeaseItems(@RequestAttribute("userId") String userId) {
        List<TradeMyItemsResponseDto> leaseItems = tradeService.getLeaseItems(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(leaseItems);
    }

    @GetMapping("/returnLendItems")
    public ResponseEntity<?> tradeReturnLendItems(@RequestAttribute("userId") String userId) {
        List<TradeMyItemsResponseDto> returnLendItems = tradeService.getReturnLendItems(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(returnLendItems);
    }

    @GetMapping("/returnLeaseItems")
    public ResponseEntity<?> tradeReturnLeaseItems(@RequestAttribute("userId") String userId) {
        List<TradeMyItemsResponseDto> returnLeaseItems = tradeService.getReturnLeaseItems(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(returnLeaseItems);
    }
}
