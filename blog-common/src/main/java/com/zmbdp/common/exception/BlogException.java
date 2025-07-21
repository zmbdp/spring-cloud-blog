package com.zmbdp.common.exception;

import lombok.Data;

@Data
public class BlogException extends RuntimeException{
    private Integer code;
    private String message;

    public BlogException() {
        this.message = "小博突然脑子短路了 (＞人＜;) 请联系管理员！";
    }

    public BlogException(String message) {
        this.message = message;
    }

    public BlogException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}