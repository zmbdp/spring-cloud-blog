package com.zmbdp.user.service.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String githubUrl;
    private String email;
    private Byte deleteFlag;
    private Date createTime;
    private Date updateTime;
}
