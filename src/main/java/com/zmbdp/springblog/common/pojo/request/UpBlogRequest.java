package com.zmbdp.springblog.common.pojo.request;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpBlogRequest {
    @NotNull(message = "博客ID不能为空")
    private Integer id;
    private String title;
    private String content;

}