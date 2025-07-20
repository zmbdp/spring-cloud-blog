package com.zmbdp.user.service.config;

import com.zmbdp.captcha.api.CaptchaServiceApi;
import com.zmbdp.captcha.api.pojo.request.CaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.CheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CaptchaConfig {
    @Autowired
    private CaptchaServiceApi captchaServiceApi;

    // 对比验证码
    public boolean checkCaptcha(String email, String inputCode) {
        CaptchaRequest captchaRequest = new CaptchaRequest();
        captchaRequest.setEmail(email);
        captchaRequest.setInputCaptcha(inputCode);
        CheckResponse checkResponse = captchaServiceApi.checkCaptcha(captchaRequest);
        // 返回对比结果
        log.info("用户: {} 验证码对比结果为：{}",email, checkResponse.isCheckResult());
        return checkResponse.isCheckResult();
    }
}
