package com.datien.lms.controller;

import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService service;
//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @Valid @RequestBody UserRequest request
//    ) {
//        service.register(request);
//        return ResponseEntity.ok().build();
//    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.doLogin(request));
    }
}
