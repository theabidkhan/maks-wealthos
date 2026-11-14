package com.maks.wealthos.security.service.impl;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.exception.ResourceNotFoundException;
import com.maks.wealthos.security.dto.LoginRequest;
import com.maks.wealthos.security.dto.LoginResponse;
import com.maks.wealthos.security.dto.RefreshTokenRequest;
import com.maks.wealthos.security.dto.RefreshTokenResponse;
import com.maks.wealthos.security.dto.RegisterRequest;
import com.maks.wealthos.security.dto.UserInfoResponse;
import com.maks.wealthos.security.service.AuthService;
import com.maks.wealthos.security.service.JwtService;
import com.maks.wealthos.user.entity.RoleEntity;
import com.maks.wealthos.user.entity.UserEntity;
import com.maks.wealthos.user.repository.RoleRepository;
import com.maks.wealthos.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {

        log.info(
                "{} Registering new user. Email={}, FirstName={}, LastName={}",
                LogConstants.SECURITY_MODULE,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
        );

        if (userRepository.existsByEmail(request.getEmail())) {

            log.warn(
                    "{} Registration failed - email already exists: {}",
                    LogConstants.SECURITY_MODULE,
                    request.getEmail()
            );

            throw new IllegalArgumentException(
                    "Email is already in use: " + request.getEmail()
            );
        }

        RoleEntity userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {

                    log.info(
                            "{} Role USER not found, creating default role",
                            LogConstants.SECURITY_MODULE
                    );

                    RoleEntity newRole = RoleEntity.builder()
                            .roleName("USER")
                            .build();

                    return roleRepository.save(newRole);
                });

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .active(true)
                .roles(Set.of(userRole))
                .build();

        UserEntity savedUser = userRepository.save(user);

        log.info(
                "{} User registered successfully. UserId={}, Email={}",
                LogConstants.SECURITY_MODULE,
                savedUser.getId(),
                savedUser.getEmail()
        );

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", Set.of("ROLE_USER"));
        String accessToken = jwtService.generateAccessToken(
                savedUser.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(savedUser.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        log.info(
                "{} Login attempt for email: {}",
                LogConstants.SECURITY_MODULE,
                request.getEmail()
        );

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {

                    log.error(
                            "{} User not found during login: {}",
                            LogConstants.SECURITY_MODULE,
                            request.getEmail()
                    );

                    return new ResourceNotFoundException(
                            "User not found: " + request.getEmail()
                    );
                });

        Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getRoleName())
                    .toList()
                    .stream()
                    .map(String.class::cast)
                    .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);

        String accessToken = jwtService.generateAccessToken(
                user.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        log.info(
                "{} Login successful. Email={}, Roles={}",
                LogConstants.SECURITY_MODULE,
                user.getEmail(),
                roleNames
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        log.info(
                "{} Refresh token request received",
                LogConstants.SECURITY_MODULE
        );

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {

            log.warn(
                    "{} Invalid refresh token provided",
                    LogConstants.SECURITY_MODULE
            );

            throw new IllegalArgumentException("Invalid refresh token");
        }

        String tokenType = jwtService.getTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {

            log.warn(
                    "{} Provided token is not a refresh token, type={}",
                    LogConstants.SECURITY_MODULE,
                    tokenType
            );

            throw new IllegalArgumentException("Token is not a refresh token");
        }

        String userEmail = jwtService.extractUsername(refreshToken);

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {

                    log.error(
                            "{} User not found during token refresh: {}",
                            LogConstants.SECURITY_MODULE,
                            userEmail
                    );

                    return new ResourceNotFoundException(
                            "User not found: " + userEmail
                    );
                });

        Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getRoleName())
                    .toList()
                    .stream()
                    .map(String.class::cast)
                    .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);

        String newAccessToken = jwtService.generateAccessToken(
                user.getEmail(), claims);
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());

        log.info(
                "{} Token refreshed successfully. Email={}",
                LogConstants.SECURITY_MODULE,
                user.getEmail()
        );

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUserInfo() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {

            log.warn("{} No authenticated user found in SecurityContext",
                    LogConstants.SECURITY_MODULE);

            throw new IllegalStateException("No authenticated user found");
        }

        String userEmail = authentication.getName();

        log.info(
                "{} Fetching current user info. Email={}",
                LogConstants.SECURITY_MODULE,
                userEmail
        );

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {

                    log.error(
                            "{} Authenticated user not found in database: {}",
                            LogConstants.SECURITY_MODULE,
                            userEmail
                    );

                    return new ResourceNotFoundException(
                            "User not found: " + userEmail
                    );
                });

        Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .toList()
                    .stream()
                    .map(String.class::cast)
                    .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        UserInfoResponse response = UserInfoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .active(user.getActive())
                .roles(roleNames)
                .build();

        log.info(
                "{} Current user info retrieved. UserId={}, Email={}",
                LogConstants.SECURITY_MODULE,
                user.getId(),
                user.getEmail()
        );

        return response;
    }
}