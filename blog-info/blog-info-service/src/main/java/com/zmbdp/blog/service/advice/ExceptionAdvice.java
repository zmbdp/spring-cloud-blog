package com.zmbdp.blog.service.advice;

import com.zmbdp.common.exception.BlogException;
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

    // 系统异常处理（卖萌化）
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail("小博突然崩溃了...快喊程序员哥哥快来看看呀 (;´༎ຶД༎ຶ`)");
    }

    // 业务异常处理（保持原有逻辑，可添加颜文字）
    @ExceptionHandler(BlogException.class)
    public Result handleBlogException(BlogException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(e.getMessage() + " (´；ω；｀)");
    }

    // 参数校验异常处理（重点优化！）

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result verifyHandle(MethodArgumentNotValidException e) {
        // 1. 安全获取校验注解中定义的消息
        String errorMsg = e.getBindingResult()          // 获取校验结果的BindingResult
                .getAllErrors()                         // 拿到所有错误（List<ObjectError>）
                .stream()                               // 转为Stream流
                .findFirst()                            // 取第一个错误（Optional<ObjectError>）
                .map(ObjectError::getDefaultMessage)    // 提取错误消息（Optional<String>）
                .orElse("小博突然脑子短路了...请稍后再试 (´･＿･`)"); // 如果为空用兜底消息

        // 2. 日志去颜文字（保持日志严肃性）
        log.error("参数校验失败: {}", errorMsg.replaceAll("[\\p{So}\\p{Cn}]", ""));

        // 3. 直接返回注解中的卖萌提示
        return Result.fail(errorMsg);
    }
}
