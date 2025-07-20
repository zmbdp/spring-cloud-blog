package com.zmbdp.captch.service.service.impl;

import com.zmbdp.captch.service.service.CaptchaService;
import com.zmbdp.captcha.api.pojo.response.CaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.CheckResponse;
import com.zmbdp.common.constant.CaptchaConstants;
import com.zmbdp.common.exception.CaptchaException;
import com.zmbdp.common.utils.CaptchaUtil;
import com.zmbdp.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RefreshScope
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CaptchaUtil captchaUtil;

    // 验证码级别 1-> 纯数字; 2-> 数字+字母
    @Value("${captcha.level}")
    private Integer captchaLevel;

    // 验证码长度
    @Value("${captcha.length}")
    private Integer captchaLength;

    // 验证码过期时间
    @Value("${captcha.time}")
    private Long captchaTime;

    // 一天中获取验证码最大的次数
    @Value("${captcha.max}")
    private Integer captchaMax;

    /**
     * 获取验证码
     *
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public CaptchaResponse getCaptchaCode(String email) {
        // 判断邮箱是否为空
        if (!StringUtils.hasText(email)) {
            log.error("邮箱为空");
            return null;
        }
        if (captchaLevel == null || captchaLength == null || captchaTime == null) {
            throw new CaptchaException();
        }

        // 检查是否已有未过期的验证码
        String captchaUser = redisUtil.getRedis(CaptchaConstants.CAPTCHA_PREFIX + email);
        if (StringUtils.hasText(captchaUser)) {
            throw new RuntimeException("冷静冷静！验证码还在路上呢 (´• ω •`)ﾉ 别频繁点击啦~");
        }

        // 原子递增验证码计数
        String countKey = CaptchaConstants.CAPTCHA_PREFIX + email + CaptchaConstants.CAPTCHA_COUNT;
        // 如果说这个值不存在, 那么也会自动创建, 初始值为 1
        long currentCount = redisUtil.incr(countKey);
        if (currentCount == 1) {
            // 因为前面已经创建并且是 1 了, 这里设置过期时间就像
            redisUtil.expire(countKey, CaptchaConstants.CAPTCHA_RATE_LIMIT_EXPIRE_SECONDS);
        }

        // 检查次数限制
        if (currentCount > captchaMax) {
            throw new RuntimeException(
                    String.format("今日你已经发了第%d次验证码了~手指头都酸了，明天再帮你发吧 (；′⌒`)", captchaMax)
            );
        }

        // 生成验证码
        String captcha;
        try {
            captcha = switch (captchaLevel) {
                case 1 -> {
                    log.info("为邮箱 {} 生成简单验证码", email);
                    yield getSimpleCaptcha(email);
                }
                case 2 -> {
                    log.info("为邮箱 {} 生成复杂验证码", email);
                    yield getDifficultyCaptcha(email);
                }
                default -> throw new CaptchaException(
                        "哎呀，小博突然卡壳了 (´• ω •`)\n" +
                                "快联系客服小姐姐帮我检查一下吧~"
                );
            };
        } catch (Exception e) {
            // 生成失败时回滚计数器
            redisUtil.decr(countKey);
            throw new CaptchaException();
        }
        // 返回给前端
        return new CaptchaResponse(email, captcha);
    }

    /**
     * 验证码校验
     *
     * @param email     邮箱
     * @param inputCode 用户输入的验证码
     * @return [用户邮箱, 是否通过]
     */
    @Override
    public CheckResponse checkCaptcha(String email, String inputCode) {
        // 获取存储的验证码
        String storedCode = redisUtil.getRedis(CaptchaConstants.CAPTCHA_PREFIX + email);
        if (!StringUtils.hasText(storedCode)) {
            // 说明此时用户还没发送验证码, 或者验证码过期了
            return new CheckResponse(email, false);
        }
        // 验证码校验
        boolean checkResult = captchaUtil.checkCaptcha(inputCode, storedCode);
        return new CheckResponse(email, checkResult);
    }

    /**
     * 获取验证码(纯数字版)
     *
     * @param email 邮箱
     * @return 验证码
     */
    private String getSimpleCaptcha(String email) {
        // 生成验证码
        String code = captchaUtil.getSimpleCaptcha(captchaLength);
        if (!StringUtils.hasText(code)) {
            log.error("生成验证码失败");
            throw new CaptchaException();
        }
        // 存储验证码到 redis 中
        return storeCaptcha(email, code);
    }

    /**
     * 获取验证码(字母数字结合版)
     *
     * @param email 邮箱
     * @return 验证码
     */
    private String getDifficultyCaptcha(String email) {
        // 生成验证码
        String code = captchaUtil.getDifficultyCaptcha(captchaLength);
        // 判空
        if (!StringUtils.hasText(code)) {
            log.error("验证码生成失败");
            throw new CaptchaException();
        }
        // 存储验证码到 redis 中
        return storeCaptcha(email, code);
    }

    /**
     * 存储验证码
     *
     * @param email 用户邮箱
     * @param code  生成的验证码
     * @return 存储成功返回验证码, 否则返回空
     */
    private String storeCaptcha(String email, String code) {
        boolean success = redisUtil.setKey(CaptchaConstants.CAPTCHA_PREFIX + email, code, captchaTime * 60);
        if (!success) {
            log.error("存储验证码失败");
            throw new CaptchaException();
        }
        return code;
    }
}