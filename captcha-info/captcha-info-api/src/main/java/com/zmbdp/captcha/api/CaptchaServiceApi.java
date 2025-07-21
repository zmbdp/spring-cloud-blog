package com.zmbdp.captcha.api;

import com.zmbdp.captcha.api.pojo.request.CheckCaptchaRequest;
import com.zmbdp.captcha.api.pojo.request.GetCaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.GetCaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckCaptchaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "captcha-service", path = "/captcha")
public interface CaptchaServiceApi {
    // 获取验证码
    @RequestMapping("/getCaptchaCode")
    GetCaptchaResponse getCaptchaCode(@RequestBody GetCaptchaRequest captchaRequest);
    // 校验验证码
    @RequestMapping("/checkCaptcha")
    CheckCaptchaResponse checkCaptcha(@RequestBody CheckCaptchaRequest checkCaptchaRequest);

}
