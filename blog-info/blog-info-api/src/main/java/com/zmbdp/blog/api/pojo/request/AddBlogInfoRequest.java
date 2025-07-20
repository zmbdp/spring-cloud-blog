package com.zmbdp.blog.api.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddBlogInfoRequest {
    @NotNull(message = "用户ID空空如也...小博不知道是谁写的啦 (´･ω･`)")
    private Integer userId;

    @NotBlank(message = "标题不能空着哦~小博会不知道这篇博客叫什么的 (＞﹏＜)")
    @Length(max = 100, message = "标题太长啦！小博的标题栏要装不下惹 (；′⌒`)")
    private String title;

    @NotBlank(message = "内容呢内容呢？小博不能变出一篇博客呀 (╯‵□′)╯︵┻━┻")
    private String content;
}