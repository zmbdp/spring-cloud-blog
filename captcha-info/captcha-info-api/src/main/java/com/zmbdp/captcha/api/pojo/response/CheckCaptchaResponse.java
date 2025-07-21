package com.zmbdp.captcha.api.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCaptchaResponse {
    private String email;
    private boolean checkResult;
}