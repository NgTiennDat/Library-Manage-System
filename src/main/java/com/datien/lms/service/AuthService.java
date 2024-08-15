package com.datien.lms.service;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse doLogin(AuthRequest request) {
        return null;
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
