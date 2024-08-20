package com.datien.lms.service;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.UserChangePasswordRequest;
import com.datien.lms.dto.request.UserForgotPasswordRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.request.UserResetPasswordRequest;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.repo.OtpRepository;
import com.datien.lms.repo.UserRepository;
import com.datien.lms.service.mapper.UserMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

        userRepository.save(user);
        emailService.sendValidEmail(user);
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
        userRepository.save(user);
    }

    public void changePassword(
            UserChangePasswordRequest request, Authentication connectedUser
    ) throws IllegalAccessException {
        var user = (User) connectedUser.getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalAccessException("Current password does not match");
        }

        // check if the two new passwords are the same
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalAccessException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void handleForgotPassword(UserForgotPasswordRequest request) throws MessagingException {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No user with email: " + request.getEmail()));

        var oldActivateCode = otpRepository.findByUserId(user.getId());

        if(oldActivateCode.isEmpty()) {
            throw new IllegalArgumentException("No user with id: " + user.getId());
        }
        otpRepository.delete(oldActivateCode.get());
        user.setEnabled(false);
        userRepository.save(user);
        emailService.sendValidEmail(user);
    }

    public void handleResetPassword(UserResetPasswordRequest userResetPassword) throws MessagingException {
        User user = userRepository.findByEmail(userResetPassword.getEmail())
                .orElseThrow(() -> new RuntimeException("No user with email: " + userResetPassword.getEmail()));

        var activeCode = otpRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No activate code found."));

        if(activeCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            emailService.sendValidEmail(user);
            throw new IllegalArgumentException("No user with id: " + user.getId());
        }

        user.setPassword(passwordEncoder.encode(userResetPassword.getNewPassword()));
        userRepository.save(user);
        otpRepository.save(activeCode);
    }

    private UserResponse buildUserResponse(String jwtToken, String refreshToken) {
        return UserResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}

