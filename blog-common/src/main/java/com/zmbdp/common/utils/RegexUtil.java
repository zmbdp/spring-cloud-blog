package com.zmbdp.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class RegexUtil {

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,20}$";

    public static final String EMAIL_REGEX = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";

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
