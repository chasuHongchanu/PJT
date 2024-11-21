package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.UserChatroomDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserChatroomMapper {
    @Insert("""
                INSERT INTO user_chatroom (room_id, user_id)
                VALUES (#{roomId},#{userId})
            """)
    void insertUserChatroom(int roomId, String userId);

    @Select("""
                SELECT COUNT(*)
                FROM user_chatroom
                WHERE room_id = #{roomId}
            """)
    int countDeletedUsers(int roomId);
}
