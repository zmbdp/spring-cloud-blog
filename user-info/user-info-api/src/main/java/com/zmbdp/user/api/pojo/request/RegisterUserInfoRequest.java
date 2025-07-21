package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterUserInfoRequest {
    @NotBlank(message = "用户名呢？！小博才不会叫你'喂'呢！(╬ Ò﹏Ó)")
    @Length(max = 20, message = "20字以内！小博的脑容量有限啊！(╯‵□′)╯︵┻━┻")
    private String userName;

    @NotBlank(message = "密码空着的话...坏人会闯进小博家里的！(╬ Ò﹏Ó)")
    @Length(max = 20, message = "密码设置的过长, 小博记不住怎么办 /(ㄒoㄒ)/~~, 建议20位以下哦~")
    @Length(min = 6, message = "密码需要至少6位才能保护好我们的秘密呢 (｀・ω・´)")
    private String password;

    @Length(max = 128, message = "GitHub地址太长啦！小博的地址簿要爆炸了 (╯°□°）╯︵ ┻━┻")
    @Length(min = 11, message = "你管这叫GitHub地址？欺负小博不懂技术？(눈_눈)")
    private String githubUrl;

    @NotBlank(message = "邮箱交出来！不然小博怎么发验证码！(〃＞目＜)")
    @Length(max = 64, message = "邮箱这么长, 你是不是在逗小博呢(・∀・(・∀・(・∀・*), 建议64以下哦~")
    @Length(min = 6, message = "这个邮箱太短了...小博怕发验证码时会迷路呢 (´･_･`)")
    @Email(message = "这个邮箱地址看起来不太对呢~ 请检查格式哦 (๑•́ ₃ •̀๑)")
    private String email;

    @NotBlank(message = "哎呀，验证码空空的呢~ 请填写后再试试吧 (｡•́︿•̀｡)")
    @Length(max = 6, min = 6, message = "验证码必须是6个字符哦，请检查后再输入~ (´• ω •`)")
    private String inputCaptcha;

    /*@NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20位")
    private String userName;
    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码不能超过20位")
    @Length(min = 6, message = "密码不能低于6位")
    private String password;
    @Length(max = 128, message = "GitHubUrl不能超过128位")
    @Length(min = 11, message = "GitHubUrl不能低于6位")
    private String githubUrl;
    @NotBlank(message = "邮箱不能为空")
    @Length(max = 64, message = "邮箱不能超过64位")
    @Length(min = 6, message = "邮箱不能低于6位")
    private String email;*/
}
