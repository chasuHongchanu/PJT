package com.example.trend.ai.controller;

import com.example.trend.ai.dto.MessageInput;
import com.example.trend.ai.dto.ResponseDto;
import com.example.trend.ai.service.AIService;
import com.example.trend.config.SkipJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/ai")
@RestController
public class AIController {
    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/newChat")
    public ResponseEntity<?> makeNewRoom(@RequestAttribute("userId") String userId) {
        int result = aiService.createNewRoom(userId);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 채팅방이 생성되었습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "채팅방 생성 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PostMapping("/query")
    public ResponseEntity<?> askQuestion(@RequestBody MessageInput messageInput,
                                         @RequestAttribute("userId") String userId) throws Exception{

        messageInput.setUserId(userId);
        List<ResponseDto> response = aiService.getChatResponse(messageInput);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<?> clear(@RequestParam("roomId") int roomId) {
        int result = aiService.clearRoom(roomId);

        Map<String, Object> response = new HashMap<>();
        if(result != 0) {
            response.put("message", "성공적으로 메세지가 삭제됐습니다.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
        else {
            response.put("message", "메세지 삭제 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
