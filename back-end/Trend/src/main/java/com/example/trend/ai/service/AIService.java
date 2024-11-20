package com.example.trend.ai.service;

import com.example.trend.ai.dto.MessageInput;
import com.example.trend.ai.dto.ResponseDto;

import java.util.List;

public interface AIService {
    int createNewRoom(String userId);

    List<ResponseDto> getChatResponse(MessageInput messageInput) throws Exception;

    int clearRoom(int roomId);
}
