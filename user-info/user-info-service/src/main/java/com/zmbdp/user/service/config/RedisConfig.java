package com.zmbdp.user.service.config;

import com.zmbdp.common.utils.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host") // 表示，只有检测到 redis 的配置了，才会加载这个 bean
    public RedisUtil redisUtil(StringRedisTemplate redisTemplate) {
        return new RedisUtil(redisTemplate);
    }
}
