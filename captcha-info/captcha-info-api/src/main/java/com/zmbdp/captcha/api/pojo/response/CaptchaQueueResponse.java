package com.zmbdp.captcha.api.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaQueueResponse {
    private String captcha;
    private String email;
}
