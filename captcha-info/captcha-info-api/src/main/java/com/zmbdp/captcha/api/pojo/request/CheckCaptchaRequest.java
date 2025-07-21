package com.zmbdp.captcha.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class CheckCaptchaRequest {
    @NotBlank(message = "小博突然脑子短路了...请稍后再试 (´･＿･`)")
    @Length(max = 20)
    private String email;
    @NotBlank(message = "小博突然脑子短路了...请稍后再试 (´･＿･`)")
    @Length(max = 20, message = "小博突然脑子短路了...请稍后再试 (´･＿･`)")
    private String inputCaptcha;
    @NotBlank(message = "小博突然脑子短路了...请稍后再试 (´･＿･`)")
    private String captchaType;
}