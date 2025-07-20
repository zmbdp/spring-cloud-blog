package com.zmbdp.captch.service.service;

import com.zmbdp.captcha.api.pojo.response.CaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckResponse;

public interface CaptchaService {
    // 获取验证码
    CaptchaResponse getCaptchaCode(String email);
    // 校验验证码
    CheckResponse checkCaptcha(String email, String inputCode);

}
