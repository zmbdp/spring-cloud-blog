package com.zmbdp.user.api;

import com.zmbdp.common.pojo.Result;
import com.zmbdp.user.api.pojo.request.LoginUserInfoRequest;
import com.zmbdp.user.api.pojo.request.RegisterUserInfoRequest;
import com.zmbdp.user.api.pojo.response.UserInfoResponse;
import com.zmbdp.user.api.pojo.response.UserLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/user")
public interface UserServiceApi {

    /**
     * 用户登录，返回 token
     *
     * @param user 用户信息
     * @return token
     */
    @RequestMapping("/login")
    Result<UserLoginResponse> login(@RequestBody LoginUserInfoRequest user);

    /**
     * 根据用户 id 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);

    /**
     * 通过博客 id 获取用户信息
     *
     * @param blogId 博客 id
     * @return 用户信息
     */
    @RequestMapping("/getAuthorInfo")
    Result<UserInfoResponse> getAuthorInfo(@RequestParam("blogId") Integer blogId);

    /**
     * 注册
     *
     * @param registerUserInfoRequest 用户信息
     * @return 注册结果
     */
    @RequestMapping("/register")
    Result<Integer> register(@RequestBody RegisterUserInfoRequest registerUserInfoRequest);
}
