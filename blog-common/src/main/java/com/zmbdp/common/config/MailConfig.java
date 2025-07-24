package com.zmbdp.common.config;

import com.zmbdp.common.utils.MailUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.mail", name = "username")
    public MailUtil mailUtil(JavaMailSender javaMailSender) {
        return new MailUtil(javaMailSender);
    }
}
