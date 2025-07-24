package com.zmbdp.captch.service.listener;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@RefreshScope // 配置文件动态刷新
@Configuration
@ConfigurationProperties(prefix = "email.captcha")
public class EmailCaptchaProperties {
    private List<String> subject;
    private List<String> content;
}