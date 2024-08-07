package com.datien.lms.user.service;

import com.datien.lms.user.dao.UserRequest;
import com.datien.lms.user.repo.UserRepository;
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

