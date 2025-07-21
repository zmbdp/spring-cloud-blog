package com.zmbdp.captcha.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GetCaptchaRequest {
    @NotBlank()
    @Length(max = 20)
    private String email;
    /**
     * 判断验证码的类型， 是登录获取还是注册获取
     */
    @NotBlank()
    private String captchaType;
}
