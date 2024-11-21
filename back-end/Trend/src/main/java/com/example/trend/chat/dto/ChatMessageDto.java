package com.example.trend.chat.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private int messageId;
    private int roomId;
    private String senderId;
    private String messageContent;
    private String messageImg;
    private MultipartFile image;
    private Timestamp chatCreatedAt;
}
