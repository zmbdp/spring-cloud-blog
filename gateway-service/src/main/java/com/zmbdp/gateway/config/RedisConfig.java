package com.zmbdp.gateway.config;

import com.zmbdp.common.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    public RedisUtil redisUtil(StringRedisTemplate redisTemplate) {
        return new RedisUtil(redisTemplate);
    }
}
