package com.zmbdp.captch.service.controller;

import com.zmbdp.captch.service.service.CaptchaService;
import com.zmbdp.captcha.api.CaptchaServiceApi;
import com.zmbdp.captcha.api.pojo.request.CheckCaptchaRequest;
import com.zmbdp.captcha.api.pojo.request.GetCaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.GetCaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckCaptchaResponse;
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
     * @param getCaptchaRequest 邮箱和验证码的类型
     * @return 验证码
     */
    @Override
    public GetCaptchaResponse getCaptchaCode(@NotBlank @RequestBody GetCaptchaRequest getCaptchaRequest) {
        return captchaService.getCaptchaCode(getCaptchaRequest);
    }

    /**
     * 验证码校验
     * @param checkCaptchaRequest 验证码请求
     * @return 是否通过
     */
    @Override
    public CheckCaptchaResponse checkCaptcha(@Validated @RequestBody CheckCaptchaRequest checkCaptchaRequest) {
        String email = checkCaptchaRequest.getEmail();
        String inputCode = checkCaptchaRequest.getInputCaptcha();
        String captchaType = checkCaptchaRequest.getCaptchaType();
        log.info("用户: {} 输入的验证码为：{}",email, inputCode);
        return captchaService.checkCaptcha(email, inputCode, captchaType);
    }
}
