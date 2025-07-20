package com.zmbdp.captcha.api.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 生成无参构造
@AllArgsConstructor // 生成有参构造
public class CaptchaResponse {
    private String email;
    private String captcha;
}