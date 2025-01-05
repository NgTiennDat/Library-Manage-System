package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.User;
import com.datien.lms.dao.UserSession;
import com.datien.lms.dto.request.model.AuthRequest;
import com.datien.lms.dto.response.AuthResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.UserRepository;
import com.datien.lms.repository.UserSessionRepository;
import com.datien.lms.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;

    private final UserSessionRepository userSessionRepository;
    private final RedisService redisService;

    public Map<Object, Object> doLogin(AuthRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("00");
        AuthResponse data = new AuthResponse();
        String notification = "";

        try {
            var user = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);

            if (ObjectUtils.isEmpty(user)) {
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
            if (!isMatchesPassword) {
                result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                notification = "Login failed.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            // Xử lý session: Xoá các session cũ
            List<UserSession> oldSession = userSessionRepository.findByUserIdAndStatus(user.getId(), 0);
            if (oldSession != null) {
                for (UserSession userSession : oldSession) {
                    userSession.setStatus(1); // Đánh dấu session cũ là đã kết thúc
                    userSessionRepository.save(userSession);
                }
            }

            // Tạo mới session
            String sessionId = UUID.randomUUID().toString();
            data.setSessionId(sessionId);

            Calendar expiryTime = Calendar.getInstance();
            expiryTime.setTime(new Date());
            expiryTime.add(Calendar.MINUTE, 5); // Thời gian hết hạn session
            userSessionRepository.insert(UUID.randomUUID().toString(), user.getId(), sessionId, request.getDeviceId(), expiryTime.getTime(), 0);

            var claims = new HashMap<String, Object>();
            claims.put("user", user.getFullName());

            String jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

            userTokenService.revokeAllUserTokens(user);
            userTokenService.saveUserToken(user, jwtToken);

            // Save token vào Redis (hoặc dịch vụ Redis đã tích hợp)
            redisService.saveSession(sessionId, jwtToken, expiryTime.getTime());

            // Set kết quả trả về
            data.setAccessToken(jwtToken);
            notification = "Successfully login.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, data);

        } catch (Exception ex) {
            result = new Result(ResponseCode.BAD_CREDENTIALS.getCode(), false, ResponseCode.BAD_CREDENTIALS.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Login failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }

        return resultExecuted;
    }

}
