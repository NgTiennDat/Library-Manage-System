package com.datien.lms.user.controller;

import com.datien.lms.user.dao.User;
import com.datien.lms.user.dao.UserRequest;
import com.datien.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserRequest request
    ) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

}
