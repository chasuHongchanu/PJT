package com.example.trend.chat.service;

import com.example.trend.chat.dto.ChatMessageDto;
import com.example.trend.chat.dto.ChatMessageResponseDto;
import com.example.trend.chat.dto.ChatRoomDto;
import com.example.trend.chat.dto.ChatRoomResponseDto;
import com.example.trend.chat.mapper.ChatMessageMapper;
import com.example.trend.chat.mapper.ChatRoomMapper;
import com.example.trend.chat.mapper.UserChatroomMapper;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.util.FileUtil;
import com.example.trend.util.Pagination;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final UserChatroomMapper userChatroomMapper;
    private final ChatRealtimeService chatRealtimeService;
    private final FileUtil fileUtil;
    private final FirebaseDatabase firebaseDatabase;

    @Override
    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto) {

        // 채팅방 생성 후 id 저장
        chatRoomMapper.createChatRoom(chatRoomDto);

        // firebase에 채팅방 정보 저장
        DatabaseReference roomRef = firebaseDatabase.getReference("chatrooms/" + chatRoomDto.getRoomId());
        Map<String, Object> roomData = new HashMap<>();
        roomData.put("members", Map.of(chatRoomDto.getLessorId(), true, chatRoomDto.getLesseeId(), true));
        roomRef.setValueAsync(roomData);
        return chatRoomDto;
    }

    @Override
    public void deleteChatRoom(int roomId, String userId) {
        userChatroomMapper.insertUserChatroom(roomId, userId);

        int deletedCount = userChatroomMapper.countDeletedUsers(roomId);
        if (deletedCount >= 2) {
            chatRoomMapper.deleteChatRoom(roomId);
        }
    }

    @Override
    public Pagination<ChatRoomResponseDto> getUserChatRooms(String userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            // 유저 채팅방 목록 조회
            List<ChatRoomResponseDto> chatRooms = chatRoomMapper.getChatRoomsByUserId(userId, size, offset);
            // 전체 개수 파악
            int totalItems = chatRoomMapper.countChatRoomsByUserId(userId);
            // 페이징 객체 반환
            return new Pagination<>(chatRooms, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_CHATROOMS, e);
        }

    }

    @Override
    public void sendMessage(ChatMessageDto message) {
        // 채팅방 권한이 있는 유저인지 확인
        if (chatRoomMapper.isUserInChatRoom(message.getRoomId(), message.getSenderId()) == 0) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_CHATROOM);
        }

        // image 저장
        if (message.getImage() != null) {
            String imageUrl = fileUtil.saveFileIntoStorage("chat", String.valueOf(message.getRoomId()), message.getImage());
            message.setMessageImg(imageUrl);
        }

        // MySQL에 메시지 저장
        chatMessageMapper.insertMessage(message);
        message.setChatCreatedAt(chatMessageMapper.getCreatedAt(message.getMessageId()));

        // FIrebase Realtime Database에 메시지 저장
        chatRealtimeService.sendMessageToFirebase(message.getRoomId(), message);
    }

    @Override
    public Pagination<ChatMessageResponseDto> getChatMessages(int roomId, String userId, int page, int size) {
        try {
            int offset = (page - 1) * size;

            // 채팅방에 참여한 user인지 확인
            if (chatRoomMapper.isUserInChatRoom(roomId, userId) == 0) {
                throw new CustomException(ErrorCode.FAIL_TO_ACCESS_CHATROOM);
            }
            // 채팅방 메시지 조회
            List<ChatMessageResponseDto> chatMessages = chatMessageMapper.getMessagesByRoomId(roomId, offset, size);
            // 전체 개수 파악
            int totalItems = chatMessageMapper.countChatMessagesByRoomId(roomId);
            // 페이징 객체 반환
            return new Pagination<>(chatMessages, totalItems, page, size);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_SELECT_CHATROOMS, e);
        }
    }


}
