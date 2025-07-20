package com.zmbdp.common.exception;

import lombok.Data;

@Data
public class CaptchaException extends RuntimeException{
    private Integer code;
    private String message;

    public CaptchaException() {
        this.message = "小博突然脑子短路了...请稍后再试 (´･＿･`)";
    }

    public CaptchaException(String message) {
        this.message = message;
    }

    public CaptchaException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}