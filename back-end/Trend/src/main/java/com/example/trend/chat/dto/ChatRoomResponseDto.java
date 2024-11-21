package com.example.trend.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDto {
    private int roomId;
    private String userId;
    private String userNickname;
    private String userProfileImg;
    private String lastMessage;
    private String lastMessageTime;
}
