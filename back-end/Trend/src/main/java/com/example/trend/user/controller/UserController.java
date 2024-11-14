package com.example.trend.user.controller;

import com.example.trend.user.dto.UserSignupRequestDto;
import com.example.trend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("/api/user")
@RequestMapping("/api/v1")
@Tag(name = "User API", description = "API for user operations")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 회원가입 API
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
}
