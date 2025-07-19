package com.zmbdp.springblog.common.pojo.response;

import com.zmbdp.springblog.common.enums.ResultCodeEnum;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;// 200 - 成功;   -1 - 失败
    private String errorMsg;
    private T data;

    public static <T> Result <T> success(T data) {
        Result result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> Result <T> fail(String errorMsg) {
        Result result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setErrorMsg(errorMsg);
        return result;
    }

    public static <T> Result <T> fail(String errorMsg, T data) {
        Result result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setErrorMsg(errorMsg);
        result.setData(data);
        return result;
    }
}
