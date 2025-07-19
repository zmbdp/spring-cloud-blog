package com.zmbdp.user.service.controller;

import com.zmbdp.common.pojo.Result;
import com.zmbdp.user.api.UserServiceApi;
import com.zmbdp.user.api.pojo.request.UserInfoRegisterRequest;
import com.zmbdp.user.api.pojo.request.LoginUserInfoRequest;
import com.zmbdp.user.api.pojo.response.UserInfoResponse;
import com.zmbdp.user.api.pojo.response.UserLoginResponse;
import com.zmbdp.user.service.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController implements UserServiceApi {

    @Autowired
    private UserService userService;

    /**
     * 用户登录，返回 token
     *
     * @param user    用户信息
     * @return token
     */
    @Override
    public Result<UserLoginResponse> login(@Validated @RequestBody LoginUserInfoRequest user) {
        log.info("用户登录, userName: {}", user.getUserName());
        return Result.success(userService.login(user));
    }

    /**
     * 根据用户 id 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @Override
    public Result<UserInfoResponse> getUserInfo(@NotNull Integer userId) {
        return Result.success(userService.getUserInfo(userId));
    }

    /**
     * 通过博客 id 获取用户信息
     *
     * @param blogId 博客 id
     * @return 用户信息
     */
    @Override
    public Result<UserInfoResponse> getAuthorInfo(@NotNull Integer blogId) {
        if (blogId < 1) {
            return Result.fail("服务器繁忙, 请稍后重试");
        }
        return Result.success(userService.selectAuthorInfoByBlogId(blogId));
    }

    /**
     * 注册
     *
     * @param userInfoRegisterRequest 用户信息
     * @return 注册结果
     */
    @Override
    public Result<Integer> register(@Validated @RequestBody UserInfoRegisterRequest userInfoRegisterRequest) {
        // 判断验证码是否正确，然后直接把用户的信息存到文件里面
        if (
                !StringUtils.hasLength(userInfoRegisterRequest.getUserName()) ||
                        !StringUtils.hasLength(userInfoRegisterRequest.getPassword())
        ) {
            return Result.fail("注册信息不完整, 请仔细检查");
        }
        return Result.success(userService.register(userInfoRegisterRequest));
    }
}
