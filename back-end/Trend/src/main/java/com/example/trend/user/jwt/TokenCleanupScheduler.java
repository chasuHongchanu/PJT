package com.example.trend.user.jwt;

import com.example.trend.user.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    private final RefreshTokenMapper refreshTokenMapper;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        refreshTokenMapper.deleteExpiredTokens();
    }
}
