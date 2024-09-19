package com.datien.lms.service;

import com.datien.lms.dto.SessionData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedissonClient redissonClient;
    final int MAX_TRANSACTION_TIMEOUT = 30;
    final int MAX_USER_TRANSACTION_TIMEOUT = 30;
    final int MAX_OTP_EXPIRY = 90;

    final String SESSION_DATA_MAP = "SESSION_DATA_MAP";
    final String USER_SESSION_KEY = "USER_SESSION_KEY";
    final String CHECKSUM_MAP = "CHECK_SUM_MAP";
    final String OTP_MAP = "OTP_MAP";

    public void setSession(String sessionId, SessionData sessionData) {
        RMapCache<String, String> userSession = redissonClient.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = redissonClient.getMapCache(SESSION_DATA_MAP);

        if(userSession != null && userSession.containsKey(sessionData.getUserId())) {
            String oldSessionId = userSession.get(sessionData.getUserId());

            if(StringUtils.isEmpty(oldSessionId)) {
                sessionMap.remove(oldSessionId); // Delete old user info
            }
        }

        // Replace/ add new session
        assert userSession != null;
        userSession.put(sessionData.getUserId(), sessionId, MAX_USER_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
        sessionMap.put(sessionId, sessionData, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
    }

    public void clearSession(String sessionId, String userId) {
        RMapCache<String, String> userSession = redissonClient.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = redissonClient.getMapCache(SESSION_DATA_MAP);
        userSession.remove(sessionId);
        sessionMap.remove(sessionId);
    }

    public SessionData getSession(String sessionId) {
        if(StringUtils.isEmpty(sessionId)) {
            return null;
        }

        RMapCache<String, String> userSession = redissonClient.getMapCache(USER_SESSION_KEY);
        RMapCache<String, Object> sessionMap = redissonClient.getMapCache(SESSION_DATA_MAP);
        SessionData sessionData = (SessionData) sessionMap.get(sessionId);

        if(sessionData != null) {
            userSession.put(sessionData.getUserId(), sessionId, MAX_USER_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
            sessionMap.put(sessionId, sessionData, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
        }

        return sessionData;
    }

    public void setCheckSumData(String key, String value) {
        RMapCache<String, String> checksumMap = redissonClient.getMapCache(CHECKSUM_MAP);
        checksumMap.put(key, value, MAX_TRANSACTION_TIMEOUT, TimeUnit.MINUTES);
    }

    public void clearCheckSum(String key) {
        RMapCache<String, String> checksumMap = redissonClient.getMapCache(CHECKSUM_MAP);
        checksumMap.remove(key);
    }

    public void setOtp(String key, String value) {
        RMapCache<String, String> otpMap = redissonClient.getMapCache(OTP_MAP);
        otpMap.put(key, value, MAX_OTP_EXPIRY, TimeUnit.MINUTES);
    }

    public void clearOtp(String key) {
        RMapCache<String, String> otpMap = redissonClient.getMapCache(OTP_MAP);
        otpMap.remove(key);
    }

    public String getOTP(String key) {
        RMapCache<String, String> otpMap = redissonClient.getMapCache(OTP_MAP);
        return otpMap.get(key);
    }

}
