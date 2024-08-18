package com.datien.lms.controller;

import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
