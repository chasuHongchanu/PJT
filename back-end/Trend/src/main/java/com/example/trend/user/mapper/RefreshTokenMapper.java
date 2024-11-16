package com.example.trend.user.mapper;

import com.example.trend.user.entity.RefreshToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    // refresh token 저장
    @Insert("INSERT INTO refresh_token (user_id, refresh_token, expires_at) VALUES (#{userId}, #{refreshToken}, #{expiresAt})")
    int insertRefreshToken(RefreshToken refreshToken);

    // 요청받은 id와 token으로 refresh token 조회
    @Select("SELECT * FROM refresh_token WHERE user_id = #{userId} AND refresh_token = #{refreshToken} AND expires_at > NOW()")
    RefreshToken selectRefreshToken(String userId, String refreshToken);

    // 사용자 id로 모든 refresh token 삭제 (로그아웃)
    @Delete("DELETE FROM refresh_token WHERE user_id = #{userId}")
    int deleteRefreshToken(String userId);

    // 만료된 RefreshToken 제거
    @Delete("DELETE FROM refresh_tokens WHERE expires_at <= NOW()")
    void deleteExpiredTokens();
}
