package com.example.trend.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User API", description = "API for user operations")
public class UserController {
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Fetches a list of all users in the system")
    public int getAllUsers() {
        // 로직 구현
        return 1;
    }
}
