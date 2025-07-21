package com.zmbdp.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class RegexUtil {

    public static final String PASSWORD_REGEX =
            "^(?:(?=.*[a-z])(?=.*[A-Z])|" +          // 小写 + 大写
                    "(?=.*[a-z])(?=.*\\d)|" +                // 小写 + 数字
                    "(?=.*[a-z])(?=.*[!@#$%^&*])|" +         // 小写 + 特殊符号
                    "(?=.*[A-Z])(?=.*\\d)|" +                // 大写 + 数字
                    "(?=.*[A-Z])(?=.*[!@#$%^&*])|" +        // 大写 + 特殊符号
                    "(?=.*\\d)(?=.*[!@#$%^&*]))" +          // 数字 + 特殊符号
                    "[a-zA-Z\\d!@#$%^&*]{6,20}$";

    public static final String EMAIL_REGEX =
            "^[a-zA-Z0-9]+(?:[._-][a-zA-Z0-9]+)*@" +            // 本地部分
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";               // 域名部分

    public static final String GITHUB_URL = "^(https?):\\/\\/([a-zA-Z0-9.-]+)(:\\d+)?(\\/[^\\s]*)?(\\?[^\\s]*)?$";

    public static boolean checkPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    public static boolean checkMail(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, content);
    }

    public static boolean checkGithubUrl(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        return Pattern.matches(GITHUB_URL, content);
    }
}
