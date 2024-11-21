package com.example.trend.chat.service;

import com.example.trend.chat.dto.ChatMessageDto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRealtimeService {
    private final FirebaseDatabase firebaseDatabase;


    public void sendMessageToFirebase(int roomId, ChatMessageDto message) {
        DatabaseReference messagesRef = firebaseDatabase.getReference("chatrooms/" + roomId + "/messages");
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("messageId", message.getMessageId());
        messageData.put("roomId", message.getRoomId());
        messageData.put("senderId", message.getSenderId());
        messageData.put("messageContent", message.getMessageContent());
        messageData.put("messageImg", message.getMessageImg());
        messageData.put("chatCreatedAt", message.getChatCreatedAt().toString());
        messagesRef.push().setValueAsync(messageData);
    }

}
