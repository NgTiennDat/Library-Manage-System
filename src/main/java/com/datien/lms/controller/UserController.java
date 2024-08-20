package com.datien.lms.controller;

import com.datien.lms.dto.request.UserChangePasswordRequest;
import com.datien.lms.dto.request.UserForgotPasswordRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.request.UserResetPasswordRequest;
import com.datien.lms.service.UserService;
import jakarta.mail.MessagingException;
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
    ) throws MessagingException {
        userService.register(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/activate-account")
    public void activateAccount(
            @RequestParam String activationCode
    ) throws MessagingException {
        userService.activateAccount(activationCode);
    }

    @PatchMapping("/password/change")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePasswordRequest request,
            Authentication connectedUser
    ) throws IllegalAccessException {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(
            @RequestBody UserForgotPasswordRequest request
    ) throws MessagingException {
        userService.handleForgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestBody UserResetPasswordRequest request
    ) throws MessagingException {
        userService.handleResetPassword(request);
        return ResponseEntity.ok().build();
    }

}
