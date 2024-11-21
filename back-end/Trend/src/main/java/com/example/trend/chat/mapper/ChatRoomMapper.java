package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.ChatRoomDto;
import com.example.trend.chat.dto.ChatRoomResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    @Insert("""
                INSERT INTO chat_room (item_id, lessor_id, lessee_id)
                VALUES (#{itemId}, #{lessorId}, #{lesseeId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void createChatRoom(ChatRoomDto chatRoom);


    ChatRoomDto getChatRoomById(int roomId);


    @Delete("""
                DELETE FROM chat_room
                WHERE room_id = #{roomId}
            """)
    void deleteChatRoom(int roomId);

    @Select("""
                SELECT 
                    cr.room_id AS roomId,
                    u.user_nickname AS userNickname,
                    u.user_profile_img AS userProfileImg,
                    cm_last.message_content AS lastMessage,
                    cm_last.chat_created_at AS lastMessageTime
                FROM 
                    chat_room cr
                JOIN 
                    user u
                ON 
                    (cr.lessor_id = u.user_id AND cr.lessee_id = #{userId})
                    OR (cr.lessee_id = u.user_id AND cr.lessor_id = #{userId})
                LEFT JOIN 
                    (
                        SELECT 
                            room_id, 
                            message_content, 
                            chat_created_at
                        FROM 
                            chat_message
                        WHERE 
                            chat_created_at IN (
                                SELECT MAX(chat_created_at) 
                                FROM chat_message 
                                GROUP BY room_id
                            )
                    ) cm_last
                ON 
                    cr.room_id = cm_last.room_id
                WHERE 
                    cr.lessor_id = #{userId}
                    OR cr.lessee_id = #{userId}
                ORDER BY 
                    cm_last.chat_created_at DESC
                LIMIT #{size} OFFSET #{offset}
            """)
    List<ChatRoomResponseDto> getChatRoomsByUserId(@Param("userId") String userId,
                                                   @Param("size") int size,
                                                   @Param("offset") int offset);

    @Select("""
                SELECT 
                    COUNT(*) AS totalCount
                FROM 
                    chat_room cr
                JOIN 
                    user u
                ON 
                    (cr.lessor_id = u.userId AND cr.lessee_id = #{userId})
                    OR (cr.lessee_id = u.userId AND cr.lessor_id = #{userId})
                WHERE 
                    cr.lessor_id = #{userId}
                    OR cr.lessee_id = #{userId};
            """)
    int countChatRoomsByUserId(String userId);
}
