package com.datien.lms.service;

import com.datien.lms.dto.auth.AuthRequest;
import com.datien.lms.dto.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public AuthResponse doLogin(AuthRequest request) {
        return null;
    }
}
