package com.zmbdp.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码生成与校验工具
 */
@Slf4j
public class CaptchaUtil {

    /**
     * 生成指定长度的数字验证码
     *
     * @param length 验证码长度（建议4~8位）
     * @return 数字验证码字符串
     */
    public String getSimpleCaptcha(int length) {
        // 参数校验
        if (length <= 0) {
            log.error("验证码长度必须大于0");
            throw new IllegalArgumentException();
        }

        // 计算范围（避免整数溢出）
        int min = 0;
        int max = (int) Math.pow(10, length) - 1; // 如length=6 → 999999

        // 生成并格式化
        return String.format("%0" + length + "d",
                ThreadLocalRandom.current().nextInt(min, max + 1));
    }

    /**
     * 生成指定长度的复杂验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public String getDifficultyCaptcha(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去掉了易混淆字符
        StringBuilder code = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
    // 使用：generateComplexCode(6) → "A3B9XY"

    /**
     * 校验验证码（仅逻辑校验）
     *
     * @param inputCaptcha     用户输入
     * @param redisCaptcha    存储的验证码
     * @return 是否通过
     */
    public boolean checkCaptcha(String inputCaptcha, String redisCaptcha) {
        if (inputCaptcha == null || redisCaptcha == null) {
            return false;
        }
        // 可在此添加更多校验逻辑（如过期时间判断）
        return inputCaptcha.equals(redisCaptcha);
    }
}