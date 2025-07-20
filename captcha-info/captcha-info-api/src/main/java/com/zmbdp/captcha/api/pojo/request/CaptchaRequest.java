package com.zmbdp.captcha.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class CaptchaRequest {
    @NotBlank()
    @Length(max = 20)
    private String email;
    @NotBlank()
    @Length(max = 20)
    private String inputCaptcha;
}