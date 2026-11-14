package com.maks.wealthos.security.service;

import com.maks.wealthos.security.dto.LoginRequest;
import com.maks.wealthos.security.dto.LoginResponse;
import com.maks.wealthos.security.dto.RefreshTokenRequest;
import com.maks.wealthos.security.dto.RefreshTokenResponse;
import com.maks.wealthos.security.dto.RegisterRequest;
import com.maks.wealthos.security.dto.UserInfoResponse;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    UserInfoResponse getCurrentUserInfo();
}