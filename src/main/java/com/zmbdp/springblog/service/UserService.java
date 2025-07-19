package com.zmbdp.springblog.service;

import com.zmbdp.springblog.common.pojo.dataobject.UserInfo;
import com.zmbdp.springblog.common.pojo.request.UserInfoRequest;
import com.zmbdp.springblog.common.pojo.response.UserInfoResponse;
import com.zmbdp.springblog.common.pojo.response.UserLoginResponse;

import javax.servlet.http.HttpSession;

public interface UserService {
    // 根据博客 id 获取作者信息
    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);
    // 登录
    UserLoginResponse login(UserInfoRequest user, String captcha, HttpSession session);
    // 验证码校验
    Boolean check(String captcha, HttpSession session);
    // 根据用户 id 获取用户信息
    UserInfoResponse getUserInfo(Integer userId);
    // 注册
    Boolean register(UserInfo userInfo, String captcha, HttpSession session);
}
