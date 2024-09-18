package com.datien.lms.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisProperties {
    @Value("${redis.redisson.mode}")
    private String mode;
    @Value("${redis.redisson.address-single}")
    private String addressSingle;
    @Value("${redis.redisson.connection-pool-size}")
    private Integer connectionPoolSize;
    @Value("${redis.redisson.connection-minimum-idle-size}")
    private Integer connectionMinimumIdleSize;
    @Value("${redis.redisson.connect-timeout}")
    private Integer connectTimeout;
}
