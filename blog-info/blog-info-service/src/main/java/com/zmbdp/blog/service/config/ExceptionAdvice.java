package com.zmbdp.blog.service.config;

import com.zmbdp.common.enums.ResultCodeEnum;
import com.zmbdp.common.exception.BlogException;
import com.zmbdp.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public Result handler(Exception e){
        log.error("发生异常, e: {}", e);
        Result result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setErrorMsg("内部错误，请联系管理员");
        return result;
    }
    @ExceptionHandler
    public Result handler(BlogException e){
        log.error("发生异常, e: {}", e);
        return Result.fail(e.getMessage());
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result verifyHandle(Exception e){
        log.error("参数校验失败: {}", e.getMessage());
        return Result.fail("参数校验失败 ");
    }
}
