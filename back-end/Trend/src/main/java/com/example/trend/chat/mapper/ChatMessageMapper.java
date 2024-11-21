package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.ChatMessageDto;
import com.example.trend.chat.dto.ChatMessageResponseDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    @Insert("""
            INSERT INTO chat_message (room_id, sender_id, message_content, message_img)
            VALUES (#{roomId}, #{senderId}, #{messageContent}, #{messageImg})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void insertMessage(ChatMessageDto message);

    @Select("SELECT chat_created_at FROM chat_message WHERE message_id = #{messageId}")
    Timestamp getCreatedAt(int messageId);

    @Select("""
                SELECT 
                    cm.message_id AS messageId,
                    cm.room_id AS roomId,
                    cm.sender_id AS senderId,
                    u.user_profile_img AS senderProfileImg,
                    cm.message_content AS messageContent,
                    cm.message_img AS messageImg,
                    cm.chat_created_at AS chatCreatedAt
                FROM chat_message cm
                JOIN user u ON cm.sender_id = u.user_id
                WHERE cm.room_id = #{roomId}
                ORDER BY cm.chat_created_at DESC
                LIMIT #{size} OFFSET #{offset}
            """)
    List<ChatMessageResponseDto> getMessagesByRoomId(int roomId, int offset, int size);

    @Select("""
                SELECT COUNT(*)
                FROM chat_message cm
                WHERE cm.room_id = #{roomId}
            """)
    int countChatMessagesByRoomId(int roomId);
}
