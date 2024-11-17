package com.example.trend.user.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class RefreshToken {
    private int tokenId;
    private String userId;
    private String refreshToken;
    private Timestamp createdAt;
    private Timestamp expiresAt;
}
