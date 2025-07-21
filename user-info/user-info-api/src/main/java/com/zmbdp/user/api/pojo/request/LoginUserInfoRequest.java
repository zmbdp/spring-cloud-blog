package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginUserInfoRequest {
    @NotBlank(message = "喵~ 邮箱账号不能留空呀，不然小博怎么找到你呢？(,,•́ . •̀,,)")
    @Email(message = "这个邮箱地址看起来不太对呢~ 请检查格式哦 (๑•́ ₃ •̀๑)")
    @Length(max = 100, message = "邮箱地址太长啦！小博最多只能记住100个字符呢 (´• ω •`)")
    private String email;
    @NotBlank(message = "不设密码的话...陌生人冒充你欺负小博怎么办呀 (╬ Ò﹏Ó)")
    @Length(max = 20, message = "密码这么长...小博会记混的！(捂脸)(´-﹏-`；)")
    private String password;
    @NotBlank(message = "哎呀，验证码空空的呢~ 请填写后再试试吧 (｡•́︿•̀｡)")
    @Length(max = 6, min = 6, message = "验证码必须是6个字符哦，请检查后再输入~ (´• ω •`)")
    private String inputCaptcha;

//    @NotBlank(message = "喵~ 邮箱账号不能留空呀，不然小博怎么找到你呢？(,,•́ . •̀,,)")
//    @Email(message = "这个邮箱地址看起来不太对呢~ 请检查格式哦 (๑•́ ₃ •̀๑)")
//    @Length(min = 4, max = 20, message = "邮箱账号长度要在4~20之间哦，太长太短小博都记不住啦 (´•̥ ̯ •̥`)")
//    private String email;
//
//    @NotBlank(message = "唔…密码框空空的，坏人会趁机偷走小博的零食的！(╯°□°)╯")
//    @Pattern(
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$",
//            message = "密码要8~20位且包含字母和数字哦~ 比如『meow1234』这样的！(๑•̀ㅂ•́)و✧"
//    )
//    private String password;
//
//    @NotBlank(message = "验证码君不见啦？快喊它回来吧 (；′⌒`)")
//    @Length(min = 6, max = 6, message = "验证码是6位数字啦，不要多也不要少~ ( ͡° ͜ʖ ͡°)✧")
//    private String inputCaptcha;
//
//    @Email(message = "这邮箱怎么长得像乱码…小博看不懂啦！(⊙ˍ⊙)")
//    @Length(max = 100, message = "邮箱太长了！小博的爪子写不下啦 (捂脸)(＞﹏＜)")
//    private String email;


    /*@NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20位")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码不能超过20位")
    private String password;*/
}