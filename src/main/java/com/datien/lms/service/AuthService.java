package com.datien.lms.service;

import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public AuthResponse doLogin(AuthRequest request) {
        return null;
    }
}
