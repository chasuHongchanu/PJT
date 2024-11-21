package com.example.trend.chat.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRealtimeService {
    private final FirebaseDatabase firebaseDatabase;

    public ChatRealtimeService() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void sendMessageToFirebase(int roomId, Object message) {
        DatabaseReference messagesRef = firebaseDatabase.getReference("chatrooms/" + roomId + "/messages");
        messagesRef.push().setValueAsync(message);
    }

}
