package com.zmbdp.captch.service.controller;

import com.zmbdp.captch.service.service.CaptchaService;
import com.zmbdp.captcha.api.CaptchaServiceApi;
import com.zmbdp.captcha.api.pojo.request.CaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.CaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController implements CaptchaServiceApi {
    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public CaptchaResponse getCaptchaCode(@NotBlank String email) {
        return captchaService.getCaptchaCode(email);
    }

    /**
     * 验证码校验
     * @param captchaRequest 验证码请求
     * @return 是否通过
     */
    @Override
    public CheckResponse checkCaptcha(@Validated @RequestBody CaptchaRequest captchaRequest) {
        String email = captchaRequest.getEmail();
        String inputCode = captchaRequest.getInputCaptcha();
        log.info("用户: {} 输入的验证码为：{}",email, inputCode);
        return captchaService.checkCaptcha(email, inputCode);
    }
}
