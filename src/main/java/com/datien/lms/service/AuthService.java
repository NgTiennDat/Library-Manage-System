package com.datien.lms.service;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.repo.UserRepository;
import com.datien.lms.service.user.UserTokenService;
import com.datien.lms.utils.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;

    public AuthResponse doLogin(AuthRequest request) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("user", user.getFullName());

        String jwtToken = jwtService.generateToken(claims, user);
        userTokenService.saveUserToken(user, jwtToken);
        userTokenService.revokeAllUserTokens(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .notification("Successfully login!")
                .build();
    }

    public void register(UserRequest request) {

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.STUDENT)
                .phone("")
                .build();
        userRepository.save(user);
    }
}
