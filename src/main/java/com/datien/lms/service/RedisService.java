package com.datien.lms.service;

import com.datien.lms.dto.SessionData;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedissonClient redissonClient;

    public void setSession(String sessionId, SessionData sessionData) {

    }
}
