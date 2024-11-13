package com.example.trend.item.controller;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.service.ItemService;
import com.example.trend.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;



//    @Autowired
//    private FileUtil fileUtil;
//
//    @PostMapping("/test")
//    public void test(@RequestParam List<MultipartFile> files) {
//        fileUtil.saveFileIntoStorage("1", "user1", "item", files);
//    }




    @PostMapping("/rent")
    public ResponseEntity<?> regist(@ModelAttribute ItemRegistRequestDto itemRegistDto) {
        // 제목이 비어있는 경우
        if(itemRegistDto.getItemName() == null) {
            throw new CustomException(ErrorCode.MISSING_ITEM_TITLE);
        }

        // 가격이 비어있는 경우
        if(itemRegistDto.getItemPrice() == 0) {
            throw new CustomException(ErrorCode.MISSING_ITEM_PRICE);
        }

        // 지역이 비어있는 경우
        if(itemRegistDto.getItemAddress() == null) {
            throw new CustomException(ErrorCode.MISSING_ITEM_REGION);
        }

        // 정상적인 데이터 DB에 등록
        int result = itemService.regist(itemRegistDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
