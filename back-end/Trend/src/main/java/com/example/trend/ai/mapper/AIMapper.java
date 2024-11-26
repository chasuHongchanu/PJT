package com.example.trend.ai.mapper;

import com.example.trend.ai.dto.CurrentItem;
import com.example.trend.ai.dto.PreviousConversation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AIMapper {
    @Select("""
            SELECT sender, message_content AS content
            FROM ai_chat_message
            WHERE room_id = #{roomId}
            ORDER BY chat_send_at DESC
            LIMIT 4
            """)
    List<PreviousConversation> selectPreviousConversation(int roomId);

    @Select("""
            SELECT item_id,
                   item_name,
                   CONCAT(main_category, ' > ', sub_category, ' > ', sub_subcategory) AS category,
                   item_content
            FROM item
            """)
    List<CurrentItem> selectCurrentItems();

    @Insert("""
            INSERT INTO ai_chat_message (room_id, sender, message_content)
            VALUES (#{roomId}, '#{userId}', #{query})
            """)
    void insertQuery(int roomId, String userId, String query);

    @Insert("""
            INSERT INTO ai_chat_message (room_id, sender, message_content)
            VALUES (#{roomId}, 'TrendGPT', #{convertedResponse})
            """)
    void insertResponse(int roomId, String convertedResponse);

    @Insert("""
            INSERT INTO ai_chatroom (user_id)
            VALUES (#{userId})
            """)
    int insertNewRoom(String userId);

    @Delete("""
            DELETE
            FROM ai_chat_message
            WHERE room_id = #{roomId}
            """)
    int deleteMessages(int roomId);
}
