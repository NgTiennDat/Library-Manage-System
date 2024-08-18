package com.datien.lms.service.user;

import com.datien.lms.dao.Token;
import com.datien.lms.dao.User;
import com.datien.lms.repo.TokenRepository;
import com.datien.lms.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());

        if(validUserToken.isEmpty()) {
            return;
        }

        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            token.setExpiredAt(LocalDateTime.now());
        });
        tokenRepository.saveAll(validUserToken);
    }
}

