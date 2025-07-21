package com.zmbdp.captch.service.service;

import com.zmbdp.captcha.api.pojo.request.GetCaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.GetCaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckCaptchaResponse;

public interface CaptchaService {
    // 获取验证码
    GetCaptchaResponse getCaptchaCode(GetCaptchaRequest getCaptchaRequest);
    // 校验验证码
    CheckCaptchaResponse checkCaptcha(String email, String inputCode, String captchaType);

}
