package com.example.trend.chat.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatroomDto {
    private int userChatId;
    private int roomId;
    private String userId;
    private Timestamp deletedAt;
}
