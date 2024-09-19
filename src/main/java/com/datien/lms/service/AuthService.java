package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.User;
import com.datien.lms.dao.UserSession;
import com.datien.lms.dto.request.AuthRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.UserRepository;
import com.datien.lms.repository.UserSessionRepository;
import com.datien.lms.utils.JwtService;
import io.netty.util.internal.ObjectUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;
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
    private final RedissonClient redissonClient;

    private final String LOGIN_ATTEMPTS_KEY_PREFIX = "login_attempts:";
    private final int MAX_LOGIN_FAIL = 3;
    private final UserSessionRepository userSessionRepository;


    public Map<Object, Object> doLogin(AuthRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("00");
        AuthResponse data = new AuthResponse();
        String notification = "";

        try {
            User user = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);

            if(ObjectUtils.isEmpty(user)) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
            boolean isMatchesPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

            if(!isMatchesPassword) {
                result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                notification = "Login failed.";
            }
            Calendar expiryTime = Calendar.getInstance();
            expiryTime.setTime(new Date());
            expiryTime.add(Calendar.MINUTE, 5); // TODO chuyển sang constant/ lưu vào redis

            UserSession oldSession = userSessionRepository.findByUserIdAndStatus(user.getId(), 0);

            if(user.getLoginCount() < MAX_LOGIN_FAIL) {
                boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());

                if(!isMatched) {
                    updateCount(user);
                    if(user.getLoginCount() == MAX_LOGIN_FAIL) {
                        user.setEnabled(false);
                        result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                        notification = "Login failed.";
                    } else {
                        result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                        notification = "Login failed.";
                    }
                }
            } else {
                user.setEnabled(false);
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                notification = "Login failed.";
            }

            var claims = new HashMap<String, Object>();
            claims.put("user", user.getFullName());

            String jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

            userTokenService.revokeAllUserTokens(user);
            userTokenService.saveUserToken(user, jwtToken);

            data.setAccessToken(jwtToken);
            notification = "Successfully login.";


        } catch (Exception ex) {
            result = new Result(ResponseCode.BAD_CREDENTIALS.getCode(), false, ResponseCode.BAD_CREDENTIALS.getMessage());
            resultExecuted.put("result", result);
            notification = "Login failed.";
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, data);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    private void updateCount(User user) {
        int loginCount = user.getLoginCount() + 1;
        user.setLoginCount(loginCount);
        userRepository.save(user);
    }

}
