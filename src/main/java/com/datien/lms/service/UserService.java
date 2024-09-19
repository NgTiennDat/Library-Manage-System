package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.UserChangePasswordRequest;
import com.datien.lms.dto.request.UserForgotPasswordRequest;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.dto.request.UserResetPasswordRequest;
import com.datien.lms.dto.response.UserResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.UserOtpRepository;
import com.datien.lms.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserOtpRepository userOtpRepository;

    public Map<Object, Object> register(UserRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var email = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);

            if(email == null) {
               result = new Result(ResponseCode.EMAIL_ALREADY_EXISTS.getCode(), false, ResponseCode.EMAIL_ALREADY_EXISTS.getMessage());
               resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
               return resultExecuted;
            }

            User newUser = new User();
            String userId = UUID.randomUUID().toString();
            newUser.setId(userId);
            newUser.setFirstname(request.getFirstname());
            newUser.setLastname(request.getLastname());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setSex(request.getSex());
            newUser.setPhone(request.getPhone());
            newUser.setIsDeleted("N");
            newUser.setRole(request.getRole());
            newUser.setEnabled(request.isEnabled());
            newUser.setLoginCount(0);

            if(request.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ROLE_NOT_VALID.getCode(), false, ResponseCode.ROLE_NOT_VALID.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            userRepository.save(newUser);
            emailService.sendValidEmail(newUser);
            notification = "Register user successfully. Get the OTP from your mail and activate it!";

        } catch (Exception ex) {
            logger.error("Some errors occurs when register a new account.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Register user failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> activateAccount(String activationCode) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";
        try {
            var savedCode = userOtpRepository.findByCode(activationCode);

            if (savedCode.getExpiredAt().isBefore(LocalDateTime.now())) {
                userOtpRepository.delete(savedCode);
                notification = "OTP has expired, another OTP has sent.";
                emailService.sendValidEmail(savedCode.getUser());
                result = new Result(ResponseCode.OTP_EXPIRED.getCode(), false, ResponseCode.OTP_EXPIRED.getMessage());
            } else {
                var user = userRepository.findById(savedCode.getUser().getId())
                        .orElseThrow(() -> new EntityNotFoundException("No user found with the Id " + savedCode.getUser().getId()));

                user.setEnabled(true);
                notification = "Successfully activated account";
                userRepository.save(user);
            }


        } catch (Exception ex) {
            logger.error("Some errors occurs when activate an account.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Activate account failed.";
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }


    public Map<Object, Object> changePassword(UserChangePasswordRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var user = (User) connectedUser.getPrincipal();

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            // check if the two new passwords are the same
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                result = new Result(ResponseCode.PASSWORD_DOES_NOT_MATCH.getCode(), false, ResponseCode.PASSWORD_DOES_NOT_MATCH.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            notification = "Successfully changed password";

        } catch (Exception ex) {
            logger.error("Some errors occurs when change password.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Change password failed.";
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }


    public Map<Object, Object> handleForgotPassword(UserForgotPasswordRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);
            if (user == null) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var oldActivateCode = userOtpRepository.findByUserId(user.getId());

            if (oldActivateCode.isEmpty()) {
                emailService.sendValidEmail(user);
            }

            userOtpRepository.delete(oldActivateCode.get());
            user.setEnabled(false);
            userRepository.save(user);
            emailService.sendValidEmail(user);
            notification = "Forgot password verified.";

        } catch (Exception ex) {
            logger.error("Some errors occurs when forgot password.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Forgot password failed.";
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> handleResetPassword(UserResetPasswordRequest userResetPassword) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var user = userRepository.findByEmailAndIsDeleted(userResetPassword.getEmail(), AppConstant.STATUS.IS_UN_DELETED);

            if(user == null) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var activeCode = userOtpRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("No activate code found."));

            if (activeCode.getExpiredAt().isBefore(LocalDateTime.now())) {
                emailService.sendValidEmail(user);
                result = new Result(ResponseCode.OTP_EXPIRED.getCode(), false, ResponseCode.OTP_EXPIRED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            user.setPassword(passwordEncoder.encode(userResetPassword.getNewPassword()));
            userRepository.save(user);
            userOtpRepository.save(activeCode);
            notification = "Successfully Reset password.";

        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Reset password failed.";
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    private UserResponse buildUserResponse(String jwtToken, String refreshToken) {
        return UserResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}

