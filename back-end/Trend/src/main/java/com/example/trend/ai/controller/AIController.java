package com.example.trend.ai.controller;

import com.example.trend.ai.service.AIService;
import com.example.trend.config.SkipJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/ai")
@RestController
public class AIController {
    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @SkipJwt
    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@ModelAttribute String userMessage) {
        System.out.println(userMessage);
        String response = aiService.getChatResponse(userMessage);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
