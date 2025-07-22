package com.zmbdp.captch.service.config;

import com.zmbdp.common.utils.CaptchaUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfig {

    @Bean
    public CaptchaUtil captchaUtil() {
        return new CaptchaUtil();
    }
}