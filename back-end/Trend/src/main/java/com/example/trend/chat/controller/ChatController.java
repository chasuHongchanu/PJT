package com.example.trend.chat.controller;

import com.example.trend.chat.dto.ChatMessageDto;
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
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 기능")
    public ResponseEntity<?> deleteChatRoom(@PathVariable int roomId, @RequestAttribute("userId") String userId) {
        chatService.deleteChatRoom(roomId, userId);
        return ResponseEntity.ok("delete success");
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<?> getUserChatRooms(
            @RequestParam String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pagination<ChatRoomResponseDto> chatRoomDtos = chatService.getUserChatRooms(userId, page, size);
        return ResponseEntity.ok(chatRoomDtos);
    }

    // 채팅 메시지 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(
            @PathVariable int roomId,
            @RequestAttribute("userId") String userId,
            @RequestParam int page,
            @RequestParam int size) {
        Pagination<ChatMessageDto> chatMessages = chatService.getChatMessages(roomId, userId, page, size);
        return ResponseEntity.ok(chatMessages);
    }

    // 채팅 메시지 전송
    @PostMapping("/rooms/{roomId}/messages")
    public void sendMessage(@PathVariable int roomId, @RequestBody ChatMessageDto message) {
        message.setRoomId(roomId);
        chatService.sendMessage(message);
    }

    // 이미지 전송
    @PostMapping("/rooms/{roomId}/images")
    public void sendImage(@PathVariable int roomId, @RequestParam String senderId, @RequestParam MultipartFile image) {
//        String imageUrl = firebaseStorageService.uploadImage(image);

        ChatMessageDto message = new ChatMessageDto();
        message.setRoomId(roomId);
        message.setSenderId(senderId);
//        message.setMessageImg(imageUrl);

        chatService.sendMessage(message);
    }
}
