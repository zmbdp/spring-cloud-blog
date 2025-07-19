package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginUserInfoRequest {
    @NotBlank(message = "好歹告诉小博你叫什么名字嘛喂(#`O′)")
    @Length(max = 20, message = "用户名太长啦, 小博根本记不住 /(ㄒoㄒ)/~~")
    private String userName;

    @NotBlank(message = "那我问你, 不输入密码小博怎么知道你是谁呢👿")
    @Length(max = 20, message = "d=====(￣▽￣*)b 这么长的密码, 等小博长大了也记不起来呢")
    private String password;
}