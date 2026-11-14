package com.maks.wealthos.security.controller;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.response.ApiResponse;
import com.maks.wealthos.security.dto.LoginRequest;
import com.maks.wealthos.security.dto.LoginResponse;
import com.maks.wealthos.security.dto.RefreshTokenRequest;
import com.maks.wealthos.security.dto.RefreshTokenResponse;
import com.maks.wealthos.security.dto.RegisterRequest;
import com.maks.wealthos.security.dto.UserInfoResponse;
import com.maks.wealthos.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and authorization endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        log.info(
                "{} Register request received. Email={}, FirstName={}",
                LogConstants.SECURITY_MODULE,
                request.getEmail(),
                request.getFirstName()
        );

        LoginResponse response = authService.register(request);

        log.info(
                "{} User registered successfully. Email={}",
                LogConstants.SECURITY_MODULE,
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "User registered successfully",
                        response
                ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info(
                "{} Login request received. Email={}",
                LogConstants.SECURITY_MODULE,
                request.getEmail()
        );

        LoginResponse response = authService.login(request);

        log.info(
                "{} Login successful. Email={}",
                LogConstants.SECURITY_MODULE,
                request.getEmail()
        );

        return ResponseEntity.ok(new ApiResponse<>(
                "Login successful",
                response
        ));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        log.info("{} Refresh token request received", LogConstants.SECURITY_MODULE);

        RefreshTokenResponse response = authService.refreshToken(request);

        log.info("{} Token refreshed successfully", LogConstants.SECURITY_MODULE);

        return ResponseEntity.ok(new ApiResponse<>(
                "Token refreshed successfully",
                response
        ));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser() {

        log.info("{} Get current user info request", LogConstants.SECURITY_MODULE);

        UserInfoResponse response = authService.getCurrentUserInfo();

        log.info(
                "{} Current user info retrieved. Email={}",
                LogConstants.SECURITY_MODULE,
                response.getEmail()
        );

        return ResponseEntity.ok(new ApiResponse<>(
                "User info retrieved successfully",
                response
        ));
    }
}