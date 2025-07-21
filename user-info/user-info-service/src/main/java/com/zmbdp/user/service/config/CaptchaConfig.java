package com.zmbdp.user.service.config;

import com.zmbdp.captcha.api.CaptchaServiceApi;
import com.zmbdp.captcha.api.pojo.request.CheckCaptchaRequest;
import com.zmbdp.captcha.api.pojo.response.CheckCaptchaResponse;
import com.zmbdp.common.exception.BlogException;
import com.zmbdp.common.exception.CaptchaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CaptchaConfig {
    @Autowired
    private CaptchaServiceApi captchaServiceApi;

    // 对比验证码
    public boolean checkCaptcha(CheckCaptchaRequest checkCaptchaRequest) {
        try {
            CheckCaptchaResponse response = captchaServiceApi.checkCaptcha(checkCaptchaRequest);
            if (response.getCheckResult() == null) {
                throw new CaptchaException("验证码偷偷溜走啦~ 请重新获取一个吧 (｡•́︿•̀｡)");
            }
            // 说明用户没有发验证码
            return response.getCheckResult();
        } catch (CaptchaException e) {
            // 明确转译验证码相关异常
            throw new BlogException(e.getMessage());
        } catch (Exception e) {
            throw new BlogException("小博验证时遇到未知错误 (´•̥ ̯ •̥`)");
        }
    }
}
