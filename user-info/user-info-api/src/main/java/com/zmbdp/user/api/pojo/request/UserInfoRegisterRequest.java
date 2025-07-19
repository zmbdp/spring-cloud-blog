package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserInfoRegisterRequest {
    @NotBlank(message = "好歹让小博可以知道怎么称呼你吧喂(#`O′)")
    @Length(max = 20, message = "用户名设置的过长, 小博要坏掉啦 QAQ, 建议20位以下哦~")
    private String userName;
    @NotBlank(message = "小博建议您, 还是输入一下密码叭~")
    @Length(max = 20, message = "密码设置的过长, 小博记不住怎么办 /(ㄒoㄒ)/~~, 建议20位以下哦~")
    @Length(min = 6, message = "密码设置的太短啦, 不怕小博偷偷看你隐私嘛? 建议6位以上哦~")
    private String password;
    @Length(max = 128, message = "github地址太长啦, 小博要被玩坏掉啦 QAQ, 建议128以下哦~")
    @Length(min = 11, message = "这么短的github地址, 你是不是把小博当傻子呢(瞪O.O)")
    private String githubUrl;
    @NotBlank(message = "请输入邮箱账号~")
    @Length(max = 64, message = "邮箱这么长, 你是不是在逗小博呢(・∀・(・∀・(・∀・*), 建议64以下哦~")
    @Length(min = 6, message = "这么短的邮箱, 难道你是vip用户嘛o.O?")
    private String email;
}
