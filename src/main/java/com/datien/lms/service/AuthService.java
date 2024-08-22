package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.UserRepository;
import com.datien.lms.utils.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;


    public Map<Object, Object> doLogin(AuthRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("00");
        int MAX_LOGIN_FAIL = 3;
        AuthResponse data = new AuthResponse();

        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("No user found with the email: " + request.getEmail()));

            var auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
            if(user.getLoginCount() < MAX_LOGIN_FAIL) {
                boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());

                if(!isMatched) {
                    updateCount(user);
                    if(user.getLoginCount() == MAX_LOGIN_FAIL) {
                        user.setEnabled(false);
                        result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                    } else {
                        result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                    }
                }
            } else {
                user.setEnabled(false);
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            var claims = new HashMap<String, Object>();
            claims.put("user", user.getFullName());

            String jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

            userTokenService.revokeAllUserTokens(user);
            userTokenService.saveUserToken(user, jwtToken);

            data.setAccessToken(jwtToken);
            data.setNotification("Successfully logged in.");
            user.setLoginCount(0);

        } catch (Exception ex) {
            result = new Result(ResponseCode.BAD_CREDENTIALS.getCode(), false, ResponseCode.BAD_CREDENTIALS.getMessage());
            resultExecuted.put("result", result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, data);
        return resultExecuted;
    }

    private void updateCount(User user) {
        int loginCount = user.getLoginCount() + 1;
        user.setLoginCount(loginCount);
        userRepository.save(user);
    }
}
