package com.datien.lms.service.user;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.repo.OtpRepository;
import com.datien.lms.repo.UserRepository;
import com.datien.lms.service.EmailService;
import com.datien.lms.utils.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;
    private final OtpRepository otpRepository;

    public void register(UserRequest request) throws MessagingException {

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.STUDENT)
                .phone("")
                .build();

        var savedUser = userRepository.save(user);
        emailService.sendValidEmail(user);
        userMapper.toUserResponse(savedUser);
    }

    public void activateAccount(String activationCode) throws MessagingException {
        var savedCode = otpRepository.findByCode(activationCode);

        if (savedCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            emailService.sendValidEmail(savedCode.getUser());
            throw new RuntimeException("No code found. Maybe it have not sent yet or expired!");
        }

        var user = userRepository.findById(savedCode.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("No user found with the Id " + savedCode.getUser().getId()));

        user.setEnabled(true);
    }
}

