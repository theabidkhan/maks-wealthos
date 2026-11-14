package com.maks.wealthos.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration:604800000}")
    private Long refreshExpiration;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(String username, Map<String, Object> extraClaims) {

        return buildToken(username, extraClaims, expiration);
    }

    public String generateAccessToken(String username) {

        return generateAccessToken(username, new HashMap<>());
    }

    public String generateRefreshToken(String username) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "refresh");
        return buildToken(username, extraClaims, refreshExpiration);
    }

    private String buildToken(String subject, Map<String, Object> extraClaims, long expiry) {

        final Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.putIfAbsent("type", "access");

        log.debug("Generating JWT token for subject={}, expiry={}ms", subject, expiry);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token) {

        try {
            extractAllClaims(token);
            return true;
        } catch (Exception ex) {
            log.warn("JWT token validation failed: {}", ex.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public String getTokenType(String token) {

        return extractClaim(token, claims -> {
            Object type = claims.get("type");
            return type != null ? type.toString() : "access";
        });
    }
}