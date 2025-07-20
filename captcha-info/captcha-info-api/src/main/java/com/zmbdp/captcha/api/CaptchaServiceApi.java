package com.zmbdp.captcha.api;

import com.zmbdp.captcha.api.pojo.request.CaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.CaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "captcha-service", path = "/captcha")
public interface CaptchaServiceApi {
    // 获取验证码
    @RequestMapping("/getCaptchaCode")
    CaptchaResponse getCaptchaCode(@RequestParam("email") String email);
    // 校验验证码
    @RequestMapping("/checkCaptcha")
    CheckResponse checkCaptcha(@RequestBody CaptchaRequest captchaRequest);

}
