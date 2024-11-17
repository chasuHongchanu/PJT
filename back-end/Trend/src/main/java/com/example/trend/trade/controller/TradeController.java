package com.example.trend.trade.controller;

import com.example.trend.trade.dto.TradeRegistDto;
import com.example.trend.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> regist(@ModelAttribute TradeRegistDto tradeRegistDto) {


        return ResponseEntity.status(HttpStatus.CREATED)
                .body("");
    }
}
