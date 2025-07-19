package com.zmbdp.user.service;

import com.zmbdp.blog.api.BlogServiceApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {BlogServiceApi.class}) // 告诉SpringCloud这个类需要调用 blog-service 服务
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
