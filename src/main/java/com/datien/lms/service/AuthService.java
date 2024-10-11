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
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

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
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;


//    public Map<Object, Object> doLogin(AuthRequest request) {
//        Map<Object, Object> resultExecuted = new HashMap<>();
//        Result result = Result.OK("00");
//        AuthResponse data = new AuthResponse();
//        String notification = "";
//
//        try {
//            User user = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);
//
//            if(ObjectUtils.isEmpty(user)) {
//                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
//                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//                return resultExecuted;
//            }
//
//            var auth = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(), request.getPassword()
//                    )
//            );
//            boolean isMatchesPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
//
//            if(!isMatchesPassword) {
//                result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
//                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//                notification = "Login failed.";
//                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
//            }
//            Calendar expiryTime = Calendar.getInstance();
//            expiryTime.setTime(new Date());
//            expiryTime.add(Calendar.MINUTE, 5); // TODO chuyển sang constant/ lưu vào redis
//
//            List<UserSession> oldSession = userSessionRepository.findByUserIdAndStatus(user.getId(), 0);
//            if (oldSession != null) {
//                for(UserSession userSession : oldSession) {
//                    userSession.setStatus(1);
//                    userSessionRepository.save(userSession);
//                }
//            }
//
//            // Thực hiện xử lí generate thông tin
//            String sessionId = UUID.randomUUID().toString();
//            data.setSessionId(sessionId);
//            userSessionRepository.insert(UUID.randomUUID().toString(), user.getId(), sessionId, request.getDeviceId(), expiryTime.getTime());
//            String jwt = jwtService.generateToken()
//            if(user.getLoginCount() < MAX_LOGIN_FAIL) {
//                boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());
//
//                if(!isMatched) {
//                    updateCount(user);
//                    if(user.getLoginCount() == MAX_LOGIN_FAIL) {
//                        user.setEnabled(false);
//                        result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
//                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//                        notification = "Login failed.";
//                        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
//                    } else {
//                        result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
//                        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//                        notification = "Login failed.";
//                        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
//                    }
//                }
//            } else {
//                user.setEnabled(false);
//                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
//                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//                notification = "Login failed.";
//                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
//            }
//
//            var claims = new HashMap<String, Object>();
//            claims.put("user", user.getFullName());
//
//            String jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
//
//            userTokenService.revokeAllUserTokens(user);
//            userTokenService.saveUserToken(user, jwtToken);
//
//            data.setAccessToken(jwtToken);
//            notification = "Successfully login.";


//        } catch (Exception ex) {
//            result = new Result(ResponseCode.BAD_CREDENTIALS.getCode(), false, ResponseCode.BAD_CREDENTIALS.getMessage());
//            resultExecuted.put("result", result);
//            notification = "Login failed.";
//        }
//
//        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
//        resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, data);
//        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
//        return resultExecuted;
//    }

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

            // Xác thực người dùng
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


    public Map<Object, Object> doLogin1(AuthRequest request) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("00");
        AuthResponse data = new AuthResponse();
        String notification = "";

        try {
            // Tìm kiếm user dựa trên email
            User user = userRepository.findByEmailAndIsDeleted(request.getEmail(), AppConstant.STATUS.IS_UN_DELETED);

            if (ObjectUtils.isEmpty(user)) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            // Lấy số lần đăng nhập thất bại từ Redis
            String loginAttemptsKey = LOGIN_ATTEMPTS_KEY_PREFIX + user.getId();
            RBucket<Integer> loginAttemptsBucket = redissonClient.getBucket(loginAttemptsKey);
            Integer loginAttempts = loginAttemptsBucket.get();
            if (loginAttempts == null) {
                loginAttempts = 0;
            }

            // Kiểm tra nếu user đã vượt quá số lần đăng nhập sai cho phép
            if (loginAttempts >= MAX_LOGIN_FAIL) {
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                notification = "Account is locked due to too many failed login attempts.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            // Thực hiện xác thực với AuthenticationManager
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Kiểm tra mật khẩu
            boolean isPasswordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (!isPasswordMatches) {
                loginAttempts++; // Tăng số lần đăng nhập thất bại
                loginAttemptsBucket.set(loginAttempts); // Lưu lại số lần đăng nhập thất bại vào Redis

                if (loginAttempts >= MAX_LOGIN_FAIL) {
                    result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                    notification = "Account is locked after reaching max login attempts.";
                } else {
                    result = new Result(ResponseCode.INCORRECT_CURRENT_PASSWORD.getCode(), false, ResponseCode.INCORRECT_CURRENT_PASSWORD.getMessage());
                    notification = "Incorrect password.";
                }
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            // Nếu đăng nhập thành công, xóa số lần đăng nhập thất bại khỏi Redis
            loginAttemptsBucket.delete();

            // Tạo JWT token
            var claims = new HashMap<String, Object>();
            claims.put("user", user.getFullName());
            String jwtToken = jwtService.generateToken(claims, user);

            // Lưu token cho user
            userTokenService.revokeAllUserTokens(user);
            userTokenService.saveUserToken(user, jwtToken);

            data.setAccessToken(jwtToken);
            notification = "Successfully login.";

        } catch (Exception ex) {
            result = new Result(ResponseCode.BAD_CREDENTIALS.getCode(), false, ResponseCode.BAD_CREDENTIALS.getMessage());
            notification = "Login failed due to incorrect credentials.";
        }

        // Hoàn thiện kết quả trả về
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
