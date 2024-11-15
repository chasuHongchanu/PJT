package com.example.trend.item.controller;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemDetailResponseDto;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/rent")
    public ResponseEntity<?> regist(@Valid @ModelAttribute("itemRegistDto") ItemRegistRequestDto itemRegistDto) {
        // 가격이 비어있는 경우
        if(itemRegistDto.getItemPrice() == 0) {
            throw new CustomException(ErrorCode.MISSING_ITEM_PRICE);
        }

        // 정상적인 데이터 DB에 등록
        int result = itemService.regist(itemRegistDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/rent/{itemId}")
    public ResponseEntity<?> detail(@PathVariable int itemId) {
        ItemDetailResponseDto itemDetailResponseDto = itemService.detail(itemId, "user3");

        return ResponseEntity.status(HttpStatus.OK)
                .body(itemDetailResponseDto);
    }
}
