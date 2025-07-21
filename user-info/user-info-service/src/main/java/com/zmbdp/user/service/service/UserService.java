package com.zmbdp.user.service.service;

import com.zmbdp.user.api.pojo.request.RegisterUserInfoRequest;
import com.zmbdp.user.api.pojo.request.LoginUserInfoRequest;
import com.zmbdp.user.api.pojo.response.UserInfoResponse;
import com.zmbdp.user.api.pojo.response.UserLoginResponse;

public interface UserService {
    // 根据博客 id 获取作者信息
    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);
    // 登录
    UserLoginResponse login(LoginUserInfoRequest user);
    // 根据用户 id 获取用户信息
    UserInfoResponse getUserInfo(Integer userId);
    // 注册
    Integer register(RegisterUserInfoRequest registerUserInfoRequest);
}
