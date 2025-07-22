package com.zmbdp.captch.service.service.impl;

import com.zmbdp.captch.service.service.CaptchaService;
import com.zmbdp.captcha.api.pojo.request.GetCaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.CheckCaptchaResponse;
import com.zmbdp.captcha.api.pojo.response.GetCaptchaResponse;
import com.zmbdp.common.constant.CaptchaConstants;
import com.zmbdp.common.constant.CaptchaTypeConstants;
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
     * @param getCaptchaRequest 邮箱和验证码种类
     * @return 验证码
     */
    @Override
    public GetCaptchaResponse getCaptchaCode(GetCaptchaRequest getCaptchaRequest) {
        // 判断邮箱是否为空
        if (!StringUtils.hasText(getCaptchaRequest.getEmail())) {
            log.error("邮箱为空");
            return null;
        }
        if (captchaLevel == null || captchaLength == null || captchaTime == null) {
            throw new CaptchaException();
        }
        // 根据验证码的种类进行分类，看看是登录验证码还是注册验证码
        String email = getCaptchaRequest.getEmail();
        String captchaType = getCaptchaRequest.getCaptchaType();
        // 拼接 该用户邮箱的 key
        String captchaKey = splicingCaptchaKey(email, captchaType);
        // 拼接 该用户邮箱计数的 key
        String countKey = splicingCountKey(email, captchaType);

        // 检查是否已有未过期的验证码
        String captchaUser = redisUtil.getKey(captchaKey);
        // 如果用户已经发送验证码了，时间在 60 秒之内，就提示用户，防止恶意攻击
        if (StringUtils.hasText(captchaUser) && captchaTime * 60 - redisUtil.getExpireTime(captchaKey) < 60) {
            throw new CaptchaException("冷静冷静！验证码还在路上呢 (´• ω •`)ﾉ 别频繁点击啦~");
        }
        // 不用邮箱版
        // 先判断一下是否可以给用户发送验证码
        if (ensureCaptchaRateLimit(countKey)) {
            // 可以发送的话就开始发送，并且存储到 redis 中
            /*// 邮箱版
            String captcha = setRedisCaptcha(captchaKey, countKey);
            boolean sendFlag = false;
            // 不为空返回 true
            if (StringUtils.hasText(captcha)) {
                // 开始发送邮箱
                // 发送邮箱操作
                sendFlag = emailService.sendEmail(email, captcha);
            }
            return new GetCaptchaResponse(email, sendFlag);*/
            return new GetCaptchaResponse(email, setRedisCaptcha(captchaKey, countKey));
        }
        return new GetCaptchaResponse(email, false);
    }

    /**
     * 拼接验证码在 redis 缓存中的 key
     *
     * @param email       用户邮箱
     * @param captchaType 验证码种类
     * @return key
     */
    private String splicingCaptchaKey(String email, String captchaType) {
        return switch (captchaType) {
            case CaptchaTypeConstants.LOGIN_CAPTCHA:
                // 说明是登录接口
                // 拼装 Redis 键
                yield CaptchaConstants.CAPTCHA_PREFIX + CaptchaConstants.LOGIN_CAPTCHA_PREFIX + email;
            case CaptchaTypeConstants.REGISTER_CAPTCHA:
                // 说明是注册接口
                yield CaptchaConstants.CAPTCHA_PREFIX + CaptchaConstants.REGISTER_CAPTCHA_PREFIX + email;
            default:
                log.error("验证码种类错误");
                throw new CaptchaException();
        };
    }

    /**
     * 获取该邮箱今天是第几次发送验证码的计数 key
     *
     * @param email       用户邮箱
     * @param captchaType 验证码类型
     * @return key
     */
    private String splicingCountKey(String email, String captchaType) {
        return switch (captchaType) {
            case CaptchaTypeConstants.LOGIN_CAPTCHA:
                // 说明是登录接口
                // 拼装 Redis 键
                yield CaptchaConstants.CAPTCHA_PREFIX + CaptchaConstants.LOGIN_CAPTCHA_PREFIX + CaptchaConstants.CAPTCHA_COUNT + email;

            case CaptchaTypeConstants.REGISTER_CAPTCHA:
                // 说明是注册接口
                yield CaptchaConstants.CAPTCHA_PREFIX + CaptchaConstants.REGISTER_CAPTCHA_PREFIX + CaptchaConstants.CAPTCHA_COUNT + email;
            default:
                log.error("验证码种类错误");
                throw new CaptchaException();
        };
    }

    /**
     * 检查验证码是否能发送给用户
     *
     * @param countKey 验证码计数 key
     * @return true-可以发送; false-不可发送
     */
    private boolean ensureCaptchaRateLimit(String countKey) {
        // 先判断验证码是否已经有了
        long captchaCount = redisUtil.incr(countKey);
        if (captchaCount == 1) {
            // 因为前面已经创建并且是 1 了, 这里设置过期时间就行
            redisUtil.expire(countKey, CaptchaConstants.CAPTCHA_RATE_LIMIT_EXPIRE_SECONDS);
        }

        // 检查次数限制
        if (captchaCount > captchaMax) {
            redisUtil.decr(countKey);
            throw new CaptchaException(
                    String.format("今日你已经发了%d次验证码了~手指头都酸了，明天再帮你发吧 (；′⌒`)", captchaMax)
            );
        }
        return true;
    }

    /**
     * 存储验证码
     *
     * @param redisKey 拼装好的 Redis 键
     * @param countKey 验证码计数 key
     * @return 存储是否成功
     */
    private boolean setRedisCaptcha(String redisKey, String countKey) {
        String captcha = null;
        try {
            captcha = switch (captchaLevel) {
                case 1 -> {
                    log.info("为邮箱 {} 生成简单验证码", redisKey);
                    yield getSimpleCaptcha();
                }
                case 2 -> {
                    log.info("为邮箱 {} 生成复杂验证码", redisKey);
                    yield getDifficultyCaptcha();
                }
                case 3 -> {
                    log.info("为邮箱 {} 混合验证码", redisKey);
                    yield getMixedSecurityCaptcha();
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
        // 没生成失败就存到 redis 里面去
        redisUtil.setKey(redisKey, captcha, captchaTime * 60);
        // 返回验证码，需要发送邮件
//        return captcha;
        return true;
    }


    /**
     * 获取验证码(纯数字版)
     *
     * @return 验证码
     */
    private String getSimpleCaptcha() {
        // 生成验证码
        String code = captchaUtil.getSimpleCaptcha(captchaLength);
        if (!StringUtils.hasText(code)) {
            log.error("生成验证码失败");
            throw new CaptchaException();
        }
        return code;
    }

    /**
     * 获取验证码(字母数字结合版)
     *
     * @return 验证码
     */
    private String getDifficultyCaptcha() {
        // 生成验证码
        String code = captchaUtil.getDifficultyCaptcha(captchaLength);
        // 判空
        if (!StringUtils.hasText(code)) {
            log.error("验证码生成失败");
            throw new CaptchaException();
        }
        return code;
    }

    /**
     * 获取混合验证码
     *
     * @return 验证码
     */
    private String getMixedSecurityCaptcha() {
        String code = captchaUtil.getMixedCaptcha(captchaLength);
        // 判空
        if (!StringUtils.hasText(code)) {
            log.error("验证码生成失败");
            throw new CaptchaException();
        }
        return code;
    }

    /*=======================================     验证码校验     =======================================*/

    /**
     * 验证码校验
     *
     * @param email       邮箱
     * @param inputCode   用户输入的验证码
     * @param captchaType 验证码类型
     * @return [用户邮箱, 是否通过]
     */
    @Override
    public CheckCaptchaResponse checkCaptcha(String email, String inputCode, String captchaType) {
        // 判断验证码类别
        String captchaKey = splicingCaptchaKey(email, captchaType);
        boolean checkResult = checkCode(captchaKey, inputCode);
        log.info("验证码校验结果: {}", checkResult);
        // 存储这个邮箱的验证码，先会判断这个验证码是否存在，要是存在的话这是第几次获取验证码了，根据反馈做出下一步抉择
        if (checkResult) {
            // 从 redis 中删除验证码
            redisUtil.deleteKey(captchaKey);
        }
        return new CheckCaptchaResponse(email, checkResult);
    }


    /**
     * 验证码校验
     *
     * @param captchaKey 验证码的 key
     * @param inputCode  用户输入的验证码
     * @return 是否通过校验
     * @throws CaptchaException 如果校验失败
     */
    private boolean checkCode(String captchaKey, String inputCode) {
        // 获取存储的验证码
        String storedCode = redisUtil.getKey(captchaKey);
        if (!StringUtils.hasText(storedCode)) {
            // 说明 redis 查不到验证码
            return false;
        }
        // 验证码校验
        return captchaUtil.checkCaptcha(inputCode, storedCode);
    }
}