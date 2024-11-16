package com.example.trend.user.controller;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.user.dto.TokenDto;
import com.example.trend.user.dto.UserInfoResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "User API", description = "API for user operations")
public class UserController {
    private final UserService userService;
    // jwt.refreshToken-validity=604800000
    @Value("${jwt.refreshToken-validity}")
    private long refreshTokenValidity;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 API
     *
     * @param userSignupRequestDto
     * @return
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "신규 회원 가입 서비스")
    @Transactional
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) throws Exception {
        userService.signUp(userSignupRequestDto);

        return ResponseEntity.ok("Success Sign Up");
    }

    @GetMapping("/duplicate-check/{newId}")
    @Operation(summary = "아이디 중복 체크", description = "회원가입 화면에서 중복체크 클릭 시 실행")
    public ResponseEntity<?> duplicateCheck(@PathVariable("newId") String newId) {
        boolean isDuplicated = userService.duplicateCheck(newId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디, 비밀번호로 로그인 후 JWT 반환")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) throws Exception {
        log.info("로그인 메서드 실행");
        // 사용자 로그인 처리
        TokenDto tokenDto = userService.login(userLoginRequestDto);

        // 액세스 토큰을 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenDto.getAccessToken());

        // 리프레시 토큰을 HttpOnly 쿠키에 추가
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .path("/api/user/refresh-token") // 리프레시 토큰을 사용할 엔드포인트로 경로 제한
                .maxAge(refreshTokenValidity) // 7일 (초 단위)
                .secure(true) // HTTPS에서만 전송
                .sameSite("Strict") // CSRF 방지
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 성공 응답 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body("Login successful");
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "AccessToken 재발급", description = "refresh token을 이용해 access token 재발급")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) throws Exception {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.MISSING_REFRESH_TOKEN);
        }
        // 액세스 토큰 갱신
        String newAccessToken = userService.refreshAccessToken(refreshToken);

        // 새 액세스 토큰을 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + newAccessToken);

        // 성공 응답 반환
        log.info("accesstoken 재발급 성공");
        return ResponseEntity.ok()
                .headers(headers)
                .body("Access token refreshed");

    }

    @GetMapping("/userinfo")
    @Operation(summary = "유저 정보 조회", description = "accesstoken으로 userId 확인 후 해당 유저 정보 반환")
    public ResponseEntity<?> userInfo(HttpServletRequest request) throws Exception {
        String userId = request.getAttribute("userId").toString();
        log.info(userId);
        UserInfoResponseDto userInfoResponseDto = userService.findUserInfo(userId);
        return ResponseEntity.ok(userInfoResponseDto);
    }
}
