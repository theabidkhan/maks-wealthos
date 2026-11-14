package com.maks.wealthos.security.service;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.user.entity.RoleEntity;
import com.maks.wealthos.user.entity.UserEntity;
import com.maks.wealthos.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        log.debug(
                "{} Loading user by email: {}",
                LogConstants.SECURITY_MODULE,
                email
        );

        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> {

                    log.warn(
                            "{} User not found with email: {}",
                            LogConstants.SECURITY_MODULE,
                            email
                    );

                    return new UsernameNotFoundException(
                            "User not found with email: " + email
                    );
                });

        Set<SimpleGrantedAuthority> authorities = user.getRoles() != null
                ? user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(
                            "ROLE_" + role.getRoleName()))
                    .toList()
                    .stream()
                    .map(SimpleGrantedAuthority.class::cast)
                    .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        log.info(
                "{} User loaded successfully. Email={}, Roles={}",
                LogConstants.SECURITY_MODULE,
                user.getEmail(),
                authorities
        );

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                true,
                true,
                true,
                authorities
        );
    }
}