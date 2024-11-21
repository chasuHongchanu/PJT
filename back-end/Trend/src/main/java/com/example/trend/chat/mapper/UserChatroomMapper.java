package com.example.trend.chat.mapper;

import com.example.trend.chat.dto.UserChatroomDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserChatroomMapper {
    @Insert("""
                INSERT INTO user_chatroom (room_id, user_id)
                VALUES (#{roomId},#{userId})
            """)
    void insertUserChatroom(UserChatroomDto userChatroom);

    @Update("""
                UPDATE user_chatroom
                SET deleted_at = NOW()
                WHERE room_id = #{roomId} AND user_id = #{userId}")"
            """)
    void updateDeletedAt(int roomId, String userId);


    List<UserChatroomDto> getUserChatrooms(String userId);

    int countDeletedUsers(int roomId);
}
