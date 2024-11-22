package com.example.trend.chat.dto;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private int roomId;
    private int itemId;
    private String lessorId;
    private String lesseeId;
    private Timestamp roomCreatedAt;
}
