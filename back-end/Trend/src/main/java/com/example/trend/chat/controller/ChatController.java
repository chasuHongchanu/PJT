package com.example.trend.chat.controller;

import com.example.trend.chat.dto.ChatMessageDto;
import com.example.trend.chat.dto.ChatMessageResponseDto;
import com.example.trend.chat.dto.ChatRoomDto;
import com.example.trend.chat.dto.ChatRoomResponseDto;
import com.example.trend.chat.service.ChatService;
import com.example.trend.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat API", description = "API for chat operations")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성", description = "예약을 위한 채팅방 생성")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomDto chatRoomDto, @RequestAttribute("userId") String userId) {
        chatRoomDto.setLesseeId(userId);
        ChatRoomDto chatroom = chatService.createChatRoom(chatRoomDto);
        return ResponseEntity.ok(chatroom);
    }

    // 채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    @Operation(summary = "게시글 삭제", description = "채팅방 삭제")
    public ResponseEntity<?> deleteChatRoom(@PathVariable int roomId, @RequestAttribute("userId") String userId) {
        chatService.deleteChatRoom(roomId, userId);
        return ResponseEntity.ok("delete success");
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    @Operation(summary = "채팅방 목록 조회", description = "채팅 목록 확인")
    public ResponseEntity<?> getUserChatRooms(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pagination<ChatRoomResponseDto> chatRoomDtos = chatService.getUserChatRooms(userId, page, size);
        return ResponseEntity.ok(chatRoomDtos);
    }

    // 채팅 메시지 조회
    @GetMapping("/rooms/{roomId}/messages")
    @Operation(summary = "채팅 내역 확인", description = "채팅방을 들어가 대화 내용을 볼 때 사용")
    public ResponseEntity<?> getChatMessages(
            @PathVariable int roomId,
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pagination<ChatMessageResponseDto> chatMessages = chatService.getChatMessages(roomId, userId, page, size);
        return ResponseEntity.ok(chatMessages);
    }

    // 채팅 메시지 전송
    @PostMapping("/rooms/{roomId}/messages")
    @Operation(summary = "메시지 전송", description = "텍스트 메시지 전송")
    public void sendMessage(@PathVariable int roomId, @RequestBody ChatMessageDto message, @RequestAttribute("userId") String userId) {
        message.setRoomId(roomId);
        message.setSenderId(userId);
        chatService.sendMessage(message);
    }

    // 이미지 전송
    @PostMapping("/rooms/{roomId}/images")
    @Operation(summary = "이미지 전송", description = "이미지 메시지 전송")
    public void sendImage(@PathVariable int roomId, @RequestAttribute("userId") String senderId, @RequestParam MultipartFile image) {

        ChatMessageDto message = new ChatMessageDto();
        message.setRoomId(roomId);
        message.setSenderId(senderId);
        message.setImage(image);

        chatService.sendMessage(message);
    }
}
