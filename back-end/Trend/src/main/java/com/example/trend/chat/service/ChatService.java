package com.example.trend.chat.service;

import com.example.trend.chat.dto.ChatMessageDto;
import com.example.trend.chat.dto.ChatRoomDto;
import com.example.trend.chat.dto.ChatRoomResponseDto;
import com.example.trend.util.Pagination;

import java.util.List;

public interface ChatService {
    ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto);
    void deleteChatRoom(int roomId, String userId);
    Pagination<ChatRoomResponseDto> getUserChatRooms(String userId, int page, int size);
    Pagination<ChatMessageDto> getChatMessages(int roomId, String userId, int page, int size);
    void sendMessage(ChatMessageDto message);
}
