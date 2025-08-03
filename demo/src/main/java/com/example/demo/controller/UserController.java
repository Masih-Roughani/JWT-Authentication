package com.example.demo.controller;

import com.example.demo.model.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        return ResponseEntity.ok(Map.of(
                "message", "Profile accessed successfully!",
                "user", username,
                "email", username + "@example.com"
        ));
    }
}
