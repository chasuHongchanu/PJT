package com.example.trend.item.controller;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemDetailResponseDto;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.dto.ItemRetrieveResponseDto;
import com.example.trend.item.dto.ItemSearchCriteria;
import com.example.trend.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;
    private final double DEFAULT_LATITUDE = 37.5074;
    private final double DEFAULT_LONGITUDE = 126.7218;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/rent/list")
    public ResponseEntity<?> list() {
        ItemSearchCriteria itemSearchCriteria = new ItemSearchCriteria();
        // 필터링하지 않았을 시 기본 위/경도 지정 후 검색
        itemSearchCriteria.setLatitude(DEFAULT_LATITUDE);
        itemSearchCriteria.setLongitude(DEFAULT_LONGITUDE);
        List<ItemRetrieveResponseDto> itemList = itemService.searchItems(itemSearchCriteria);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemList);
    }

    @PostMapping("/rent")
    public ResponseEntity<?> regist(@Valid @ModelAttribute("itemRegistDto") ItemRegistRequestDto itemRegistDto, HttpServletRequest request) {
        itemRegistDto.setUserId(request.getAttribute("userId").toString());
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

    @GetMapping("/rent?mainCategory={mainCategory}&subCategory={subCategory}&subSubCategory={subSubCategory}&maxPrice={maxPrice}&minPrice={minPrice}")
    public ResponseEntity<?> filter(@ModelAttribute ItemSearchCriteria itemSearchCriteria) {
        // 최대 가격이 최소 가격보다 작으면 예외 발생

        List<ItemRetrieveResponseDto> itemList = itemService.searchItems(itemSearchCriteria);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemList);
    }

    @GetMapping("/rent?keyword={keyword}")
    public ResponseEntity<?> searchByKeyword(@ModelAttribute ItemSearchCriteria itemSearchCriteria) {
        List<ItemRetrieveResponseDto> itemList = itemService.searchItems(itemSearchCriteria);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemList);
    }
}
