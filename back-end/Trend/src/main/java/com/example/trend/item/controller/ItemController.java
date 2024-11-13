package com.example.trend.item.controller;

import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/rent")
    public ResponseEntity<?> regist(@RequestBody ItemRegistRequestDto itemRegistDto) {
        System.out.println(itemRegistDto);
        System.out.println(itemRegistDto.getItemName());
        int result = itemService.regist(itemRegistDto);

        return ResponseEntity.ok(result);
    }
}
