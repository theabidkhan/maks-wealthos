package com.maks.wealthos.security.filter;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.security.service.CustomUserDetailsService;
import com.maks.wealthos.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.debug("{} No Bearer token found in request: {}", LogConstants.SECURITY_MODULE, request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER_PREFIX.length());

        log.debug("{} Extracting JWT token from request: {}", LogConstants.SECURITY_MODULE, request.getRequestURI());

        try {
            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                log.debug("{} Validating token for user: {}", LogConstants.SECURITY_MODULE, userEmail);

                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt) && userDetails != null) {

                    log.info(
                            "{} Authentication successful. User={}, Request={}",
                            LogConstants.SECURITY_MODULE,
                            userEmail,
                            request.getRequestURI()
                    );

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn(
                            "{} Token validation failed for user: {}",
                            LogConstants.SECURITY_MODULE,
                            userEmail
                    );
                }
            }
        } catch (Exception ex) {
            log.error(
                    "{} Failed to process JWT token: {}",
                    LogConstants.SECURITY_MODULE,
                    ex.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}
