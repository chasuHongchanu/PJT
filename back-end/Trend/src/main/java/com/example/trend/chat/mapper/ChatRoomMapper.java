package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.ChatRoomDto;
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

    @Select("""
<script>
SELECT *
FROM chat_room cr 
    LEFT JOIN chat_message cm 
    ON cr.room_id = cm.room_id
    LEFT JOIN user u
    <choose>
        <when test="userId == cr.lessor_id">
            ON cr.lessee_id = u.id
        </when>
        <otherwise>
            ON cr.lessor_id = u.id
        </otherwise>
    </choose>   
</script>
""")
    List<ChatRoomDto> getChatRoomsByUserId(String userId);

    @Delete("""
                DELETE FROM chat_room
                WHERE room_id = #{roomId}
            """)
    void deleteChatRoom(int roomId);
}
