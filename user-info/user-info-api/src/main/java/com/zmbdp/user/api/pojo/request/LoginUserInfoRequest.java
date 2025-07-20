package com.zmbdp.user.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginUserInfoRequest {
    @NotBlank(message = "喂喂~用户名空着的话...小博该怎么称呼你呢 (´･ω･`)?")
    @Length(max = 20, message = "用户名太长啦！小博的脑容量只能记住20个字啦 (＞﹏＜)")
    private String userName;
    @NotBlank(message = "不设密码的话...陌生人冒充你欺负小博怎么办呀 (╬ Ò﹏Ó)")
    @Length(max = 20, message = "密码这么长...小博会记混的！(捂脸)(´-﹏-`；)")
    private String password;
    /*@NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20位")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码不能超过20位")
    private String password;*/
}