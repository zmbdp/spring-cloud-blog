package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginUserInfoRequest {
    @NotBlank(message = "咦？邮箱空空的呢~ 请填写后再试试吧 (｡•́︿•̀｡)")
    @Email(message = "这个邮箱地址看起来不太对呢~ 请检查格式哦 (๑•́ ₃ •̀๑)")
    @Length(max = 100, message = "邮箱地址太长啦！小博最多只能记住100个字符呢 (´• ω •`)")
    private String email;
    @NotBlank(message = "不设密码的话...陌生人冒充你欺负小博怎么办呀 (╬ Ò﹏Ó)")
    @Length(max = 20, message = "密码这么长...小博会记混的！(捂脸)(´-﹏-`；)")
    private String password;
    @NotBlank(message = "哎呀，验证码空空的呢~ 请填写后再试试吧 (｡•́︿•̀｡)")
    @Length(max = 6, min = 6, message = "验证码必须是6个字符哦，请检查后再输入~ (´• ω •`)")
    private String inputCaptcha;


    /*@NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20位")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码不能超过20位")
    private String password;*/
}