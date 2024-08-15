package com.datien.lms.service.user;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.repo.UserRepository;
import com.datien.lms.service.EmailService;
import com.datien.lms.utils.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;

    public UserResponse register(UserRequest request) throws MessagingException {
        
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.STUDENT)
                .phone("")
                .build();

        var savedUser = userRepository.save(user);
        emailService.sendValidEmail(user);
        return userMapper.toUserResponse(savedUser);
    }

}

