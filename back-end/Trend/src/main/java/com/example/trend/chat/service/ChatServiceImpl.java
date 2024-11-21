package com.example.trend.chat.service;

import com.example.trend.chat.dto.ChatMessageDto;
import com.example.trend.chat.dto.ChatRoomDto;
import com.example.trend.chat.dto.UserChatroomDto;
import com.example.trend.chat.mapper.ChatMessageMapper;
import com.example.trend.chat.mapper.ChatRoomMapper;
import com.example.trend.chat.mapper.UserChatroomMapper;
import com.example.trend.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final UserChatroomMapper userChatroomMapper;
    private final ChatRealtimeService chatRealtimeService;

    @Override
    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto) {

        // 채팅방 생성 후 id 저장
        chatRoomMapper.createChatRoom(chatRoomDto);

        // 유저 채팅방 등록
        UserChatroomDto lessorChatroom = UserChatroomDto.builder()
                .roomId(chatRoomDto.getRoomId())
                .userId(chatRoomDto.getLessorId())
                .build();
        userChatroomMapper.insertUserChatroom(lessorChatroom);

        UserChatroomDto lesseeChatroom = UserChatroomDto.builder()
                .roomId(chatRoomDto.getRoomId())
                .userId(chatRoomDto.getLesseeId())
                .build();
        userChatroomMapper.insertUserChatroom(lesseeChatroom);

        return chatRoomDto;
    }

    @Override
    public void deleteChatRoom(int roomId, String userId) {
        userChatroomMapper.updateDeletedAt(roomId, userId);

        int deletedCount = userChatroomMapper.countDeletedUsers(roomId);
        if (deletedCount >= 2) {
            chatRoomMapper.deleteChatRoom(roomId);
        }
    }

    @Override
    public Pagination<ChatRoomDto> getUserChatRooms(String userId, int page, int size) {
        int offset = (page - 1) * size;
        // 유저 채팅방 목록 조회
        // 구현 필요
        return null;
    }

    @Override
    public Pagination<ChatMessageDto> getChatMessages(int roomId, int page) {
        int limit = 30;
        int offset = (page - 1) * limit;
        chatMessageMapper.getMessagesByRoomId(roomId, offset, limit);
        return null;
    }

    @Override
    public void sendMessage(ChatMessageDto message) {
        // MySQL에 메시지 저장
        chatMessageMapper.insertMessage(message);

        // FIrebase Realtime Database에 메시지 저장
        chatRealtimeService.sendMessageToFirebase(message.getRoomId(), message);
    }
}
