package com.zmbdp.common.constant;

public class CaptchaConstants {

    /**
     * 验证码 redis key 前缀
     */
    public static final String CAPTCHA_PREFIX = "captcha:";

    public static final String CAPTCHA_COUNT = "count:";

    public static final long CAPTCHA_RATE_LIMIT_EXPIRE_SECONDS = 24 * 60 * 60L; // 24小时
}
