package com.zmbdp.springblog.controller;

import com.zmbdp.springblog.common.pojo.request.UserInfoRequest;
import com.zmbdp.springblog.common.pojo.dataobject.UserInfo;
import com.zmbdp.springblog.common.pojo.response.UserInfoResponse;
import com.zmbdp.springblog.common.pojo.response.UserLoginResponse;
import com.zmbdp.springblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录，返回 token
     * @param user 用户信息
     * @param captcha 验证码
     * @param session  session
     * @return  token
     */
    @RequestMapping("/login")
    public UserLoginResponse login(@Validated @RequestBody UserInfoRequest user, String captcha, HttpSession session) {
        log.info("用户登录, userName: {}", user.getUserName());
        return userService.login(user, captcha, session);
    }

    /**
     * 根据用户 id 查询用户信息
     * @param userId 用户 id
     * @return 用户信息
     */
    @RequestMapping("/getUserInfo")
    public UserInfoResponse getUserInfo(@NotNull Integer userId){
        return userService.getUserInfo(userId);
    }

    /**
     * 通过博客 id 获取用户信息
     * @param blogId 博客 id
     * @return 用户信息
     */
    @RequestMapping("/getAuthorInfo")
    public UserInfoResponse getAuthorInfo(@NotNull Integer blogId){
        if (blogId < 1) {
            return null;
        }
        return userService.selectAuthorInfoByBlogId(blogId);
    }

    /**
     * 注册
     * @param userInfo 用户信息
     * @param captcha 验证码
     * @param session  session
     * @return 注册结果
     */
    @RequestMapping("/register")
    public Boolean register(UserInfo userInfo, String captcha, HttpSession session) {
        // 判断验证码是否正确，然后直接把用户的信息存到文件里面
        if (
                !StringUtils.hasLength(userInfo.getUserName()) ||
                !StringUtils.hasLength(userInfo.getPassword())
        ) {
            return false;
        }
        return userService.register(userInfo, captcha, session);
    }
}
