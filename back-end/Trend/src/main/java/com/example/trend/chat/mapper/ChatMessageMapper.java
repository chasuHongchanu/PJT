package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    void insertMessage(ChatMessageDto message);
    List<ChatMessageDto> getMessagesByRoomId(int roomId, int offset, int limit);
}
