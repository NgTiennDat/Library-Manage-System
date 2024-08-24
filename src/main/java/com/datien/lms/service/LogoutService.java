package com.datien.lms.service;

import com.datien.lms.repository.TokenRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);
    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Authentication connectedUser
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization header.");
            return;
        }

        jwt = authHeader.substring(7);
        try {
            var storedToken = tokenRepository.findByToken(jwt)
                    .orElse(null);

            if (storedToken != null) {
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                SecurityContextHolder.clearContext();
                logger.info("Token successfully invalidated and security context cleared.");
            } else {
                logger.warn("Token not found in repository.");
            }
        } catch (Exception e) {
            logger.error("Error during logout process", e);
        }
    }
}
