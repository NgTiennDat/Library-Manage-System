package com.datien.lms.controller;

import com.datien.lms.dto.request.model.AuthRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.AuthService;
import com.datien.lms.utils.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Logger logger = LogManager.getLogger(AuthController.class);
    public final AuthService service;
    private final JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(service.doLogin(request)));
    }
}
