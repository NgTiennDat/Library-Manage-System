package com.datien.lms.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    final String SINGLE_MODE = "SINGLE";
    private final RedisProperties properties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        if(SINGLE_MODE.equals(properties.getMode())) {
            config.useSingleServer().setAddress(properties.getAddressSingle())
                    .setConnectionPoolSize(properties.getConnectionPoolSize())
                    .setConnectionMinimumIdleSize(properties.getConnectionMinimumIdleSize())
                    .setConnectTimeout(properties.getConnectTimeout());
        }
        return Redisson.create(config);
    }
}
