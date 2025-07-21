package com.zmbdp.common.constant;

public class CaptchaConstants {

    /**
     * 验证码 redis key 前缀
     */
    public static final String CAPTCHA_PREFIX = "captcha:";

    /**
     * 登录验证码 redis key 前缀
     */
    public static final String LOGIN_CAPTCHA_PREFIX = "login:";

    /**
     * 注册验证码 redis key 前缀
     */
    public static final String REGISTER_CAPTCHA_PREFIX = "register:";

    /**
     * 验证码计数 redis key 前缀
     */
    public static final String CAPTCHA_COUNT = "count:";

    /**
     * 验证码过期时间
     */
    public static final long CAPTCHA_RATE_LIMIT_EXPIRE_SECONDS = 24 * 60 * 60L; // 24小时
}
