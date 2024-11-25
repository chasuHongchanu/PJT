package com.example.trend.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseUserService {

    private final FirebaseAuth firebaseAuth;

    public void createFirebaseUser(String userId) {
        try {
            // Firebase 사용자가 없으면 생성
            try {
                firebaseAuth.getUserByEmail(userId + "@trend.com");
            } catch (FirebaseAuthException e) {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(userId + "@trend.com")
                        .setPassword(userId)  // 실제 운영에서는 더 안전한 비밀번호 사용
                        .setUid(userId);

                UserRecord userRecord = firebaseAuth.createUser(request);
                log.info("Successfully created Firebase user: {}", userRecord.getUid());
            }
        } catch (FirebaseAuthException e) {
            log.error("Failed to create Firebase user: {}", userId, e);
            throw new RuntimeException("Firebase user creation failed: " + e.getMessage(), e);
        }
    }
}
