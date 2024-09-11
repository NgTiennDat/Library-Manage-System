package com.datien.lms.controller;

import com.datien.lms.dto.request.UserChangePasswordRequest;
import com.datien.lms.dto.request.UserForgotPasswordRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.request.UserResetPasswordRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(userService.register(request)));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(
            @RequestParam String activationCode
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(userService.activateAccount(activationCode)));
    }

    @PatchMapping("/password/change")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePasswordRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(userService.changePassword(request, connectedUser)));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(
            @RequestBody UserForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(userService.handleForgotPassword(request)));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestBody UserResetPasswordRequest request
    ) { 
        return ResponseEntity.ok(ResponseData.createResponse(userService.handleResetPassword(request)));
    }

}
