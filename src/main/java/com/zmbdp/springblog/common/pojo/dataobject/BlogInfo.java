package com.zmbdp.springblog.common.pojo.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class BlogInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 标题
    private String title;
    // 博客内容
    private String content;
    private Integer userId;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
}
