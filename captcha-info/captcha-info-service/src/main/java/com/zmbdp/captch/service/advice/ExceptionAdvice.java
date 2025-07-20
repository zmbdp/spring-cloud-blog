package com.zmbdp.captch.service.advice;

import com.zmbdp.common.exception.BlogException;
import com.zmbdp.common.exception.CaptchaException;
import com.zmbdp.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail("小博突然脑子短路了 (＞人＜;) 请联系管理员！");
    }

    @ExceptionHandler(BlogException.class)
    public Result handleBlogException(BlogException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(CaptchaException.class)
    public String handleCaptchaException(CaptchaException e) {
        // 这个是专门处理没发送成功的验证码的
        return e.getMessage();
    }

    // 处理参数校验异常（重点修改这里！）
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result verifyHandle(MethodArgumentNotValidException e) {
        // 1. 安全获取校验注解中定义的消息
        String errorMsg = e.getBindingResult()          // 获取校验结果的 BindingResult
                .getAllErrors()                         // 拿到所有错误（List<ObjectError>）
                .stream()                               // 转为 Stream 流
                .findFirst()                            // 取第一个错误（Optional<ObjectError>）
                .map(ObjectError::getDefaultMessage)    // 提取错误消息（Optional<String>）
                .orElse("小博突然脑子短路了...请稍后再试 (´･＿･`)"); // 如果为空用兜底消息

        // 2. 日志去颜文字（保持日志严肃性）
        log.error("参数校验失败: {}", errorMsg.replaceAll("[\\p{So}\\p{Cn}]", ""));

        // 3. 直接返回注解中的卖萌提示
        return Result.fail(errorMsg);
    }

}
