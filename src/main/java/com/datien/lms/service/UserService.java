package com.datien.lms.service;

import com.datien.lms.dto.user.UserRequest;
import com.datien.lms.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void register(UserRequest request) {
    }
}

