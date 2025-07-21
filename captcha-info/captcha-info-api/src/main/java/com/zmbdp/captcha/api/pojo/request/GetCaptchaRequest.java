package com.zmbdp.captcha.api.pojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GetCaptchaRequest {
    @NotBlank()
    @Length(max = 20)
    @Email(message = "这个邮箱地址看起来不太对呢~ 请检查格式哦 (๑•́ ₃ •̀๑)")
    private String email;
    /**
     * 判断验证码的类型， 是登录获取还是注册获取
     */
    @NotBlank()
    private String captchaType;
}
