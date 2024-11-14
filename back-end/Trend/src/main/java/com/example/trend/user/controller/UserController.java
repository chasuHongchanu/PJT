package com.example.trend.user.controller;

import com.example.trend.user.dto.TokenResponseDto;
import com.example.trend.user.dto.UserLoginRequestDto;
import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.service.UserService;
import com.example.trend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("/api/user")
@RequestMapping("/api/v1")
@Tag(name = "User API", description = "API for user operations")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        userService.signUp(userSignupRequestDto);

        return ResponseEntity.ok("Success Sign Up");
    }

    @GetMapping("/duplicateCheck/{newId}")
    @Operation(summary = "아이디 중복 체크", description = "회원가입 화면에서 중복체크 클릭 시 실행")
    public ResponseEntity<?> duplicateCheck(@PathVariable("newId") String newId) {
        boolean isDuplicated = userService.duplicateCheck(newId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디, 비밀번호로 로그인 후 JWT 반환")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        TokenResponseDto tokenResponseDto = userService.login(userLoginRequestDto);

        //refresh token 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경에서만 전송
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 1주일
                .sameSite("Strict")
                .build();

        // 토큰들을 헤더에 담아 반환
        return ResponseEntity.ok()
                .header("Authorization", "bearer " + accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Login Successful");
    }
}
