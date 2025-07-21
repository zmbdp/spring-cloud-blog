package com.zmbdp.user.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmbdp.blog.api.BlogServiceApi;
import com.zmbdp.blog.api.pojo.response.BlogInfoResponse;
import com.zmbdp.captcha.api.pojo.request.CheckCaptchaRequest;
import com.zmbdp.common.constant.CaptchaTypeConstants;
import com.zmbdp.common.exception.BlogException;
import com.zmbdp.common.pojo.Result;
import com.zmbdp.common.utils.JWTUtils;
import com.zmbdp.common.utils.RegexUtil;
import com.zmbdp.common.utils.SecurityUtil;
import com.zmbdp.user.api.pojo.request.LoginUserInfoRequest;
import com.zmbdp.user.api.pojo.request.RegisterUserInfoRequest;
import com.zmbdp.user.api.pojo.response.UserInfoResponse;
import com.zmbdp.user.api.pojo.response.UserLoginResponse;
import com.zmbdp.user.service.config.CaptchaConfig;
import com.zmbdp.user.service.dataobject.UserInfo;
import com.zmbdp.user.service.mapper.UserInfoMapper;
import com.zmbdp.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    // 想要找到 bean, 一定要在启动类加上 @EnableFeignClients(clients = {BlogServiceApi.class})
    @Autowired
    private BlogServiceApi blogServiceApi;

    @Autowired
    private CaptchaConfig CaptchaConfig;

    /**
     * 登录接口
     *
     * @param loginUserInfoRequest 用户信息
     * @return 登录结果
     */
    @Override
    public UserLoginResponse login(LoginUserInfoRequest loginUserInfoRequest) {
        CheckCaptchaRequest checkCaptchaRequest =
                splicingCheckCaptchaRequest(
                        loginUserInfoRequest.getEmail(),
                        loginUserInfoRequest.getInputCaptcha(),
                        CaptchaTypeConstants.LOGIN_CAPTCHA
                );
        // 验证验证码是否正确
        boolean b = CaptchaConfig.checkCaptcha(checkCaptchaRequest);
        if (!b) {
            throw new BlogException("小博提醒~验证码好像写错了哦, 嘿嘿(*^_^*)");
        }
        //验证账号密码是否正确
        UserInfo userInfo = selectUserInfoByEmail(loginUserInfoRequest.getEmail());
        if (
                userInfo == null || userInfo.getId() == null ||
                        !SecurityUtil.verify(loginUserInfoRequest.getPassword(), userInfo.getPassword())
        ) {
            throw new BlogException("小博提醒~用户名或者密码好像写错了哦, 嘿嘿(*^_^*)");
        }
        //账号密码正确的逻辑
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("email", userInfo.getEmail());

        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }

    /**
     * 根据博客 id 查询作者 id，然后再根据作者 id 查询作者信息
     *
     * @param blogId 博客 id
     * @return 作者信息
     */
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        // 远程调用
        //1. 根据博客 ID, 获取作者 ID
        Result<BlogInfoResponse> blogInfoResponseResult = blogServiceApi.getBlogDetail(blogId);
        //2. 根据作者ID, 获取作者信息
        if (blogInfoResponseResult == null || blogInfoResponseResult.getData() == null) {
            throw new BlogException("小博好像想不起来这篇博客呢(；′⌒`)");
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = null;
        try {
            userInfo = selectUserInfoById(blogInfoResponseResult.getData().getUserId());
            BeanUtils.copyProperties(userInfo, userInfoResponse);
        } catch (Exception e) {
            throw new BlogException("小博找不到这篇博客的作者了QAQ");
        }
        return userInfoResponse;
    }


    /**
     * 根据用户 id 获取用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    /**
     * 注册账号
     *
     * @param registerUserInfo 想注册用户的注册信息
     * @return 注册成功返回 true，否则返回 false
     */
    public Integer register(RegisterUserInfoRequest registerUserInfo) {
        // 参数校验: 用户名可以重复, 密码格式, 邮箱格式, 邮箱不能重复, github地址格式
        checkUserInfo(registerUserInfo);
        // 然后判断验证码是否正确
        CheckCaptchaRequest checkCaptchaRequest = splicingCheckCaptchaRequest(
                registerUserInfo.getEmail(),
                registerUserInfo.getInputCaptcha(),
                CaptchaTypeConstants.REGISTER_CAPTCHA
        );
        // 验证验证码是否正确
        boolean b = CaptchaConfig.checkCaptcha(checkCaptchaRequest);
        if (!b) {
            throw new BlogException("小博提醒~验证码好像写错了哦, 嘿嘿(*^_^*)");
        }
        // 判断一下，如果数据库返回 0，就是没插入成功，说明用户存在
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerUserInfo, userInfo);
        Integer flag = 0;
        userInfo.setPassword(SecurityUtil.encrypt(registerUserInfo.getPassword()));
        try {
            flag = insertUser(userInfo);
        } catch (Exception e) {
            log.error("用户注册失败, 用户email: {}, 用户id: {}, e: {}", registerUserInfo.getEmail(), userInfo.getId(), e.getMessage());
            throw new BlogException("请联系管理员哦, 小博罢工飞走噜(～￣(OO)￣)ブ");
        }
        if (flag == 0) {
            throw new BlogException("用户注册失败, 小博太没用了/(ㄒoㄒ)/~~");
        }
        log.info("用户注册成功, 用户email: {}, 用户id: {}", registerUserInfo.getEmail(), userInfo.getId());
        return userInfo.getId();
    }

    /**
     * 拼接 CheckCaptchaRequest
     *
     * @param email       邮箱
     * @param inputCaptcha   输入的验证码
     * @param captchaType 验证码类型
     * @return CheckCaptchaRequest
     */
    private CheckCaptchaRequest splicingCheckCaptchaRequest(String email, String inputCaptcha, String captchaType) {
        CheckCaptchaRequest checkCaptchaRequest = new CheckCaptchaRequest();
        checkCaptchaRequest.setEmail(email);
        checkCaptchaRequest.setInputCaptcha(inputCaptcha);
        checkCaptchaRequest.setCaptchaType(captchaType);
        return checkCaptchaRequest;
    }

    private void checkUserInfo(RegisterUserInfoRequest registerUserInfo) {
        // 用户名可以重复
        // 邮箱不能个重复
        UserInfo userInfo = selectUserInfoByEmail(registerUserInfo.getEmail());
        if (userInfo != null) {
            log.error("用户邮箱重复检测触发｜用户邮箱：{}", registerUserInfo.getEmail()); // 日志保持专业
            // 前端展示版, 不会前端的话可以试试这个
//            throw new BlogException(
//                    "<div style='font-family: Arial, sans-serif; color: #333;'>" +
//                            "  <p>✨ 哎呀~ 邮箱「" + registerUserInfo.getEmail() + "」的主人已经入驻小博的星球啦！(✧ω✧)</p>" +
//                            "  <p>试试用其他邮箱注册，或者检查是否已经注册过？</p>" +
//                            "</div>"
//            );
            throw new BlogException(
                    "✨ 哎呀~ 邮箱「" + registerUserInfo.getEmail() + "」的主人已经入驻小博的星球啦！(✧ω✧)\n" +
                            "试试用其他邮箱注册，或者检查是否已经注册过？"
            );
        }
        // 邮箱格式
        if (!RegexUtil.checkMail(registerUserInfo.getEmail())) {
            log.error("邮箱格式异常｜输入值：{}", registerUserInfo.getEmail());
            throw new BlogException(
                    "邮箱「" + registerUserInfo.getEmail() + "」格式让小博困惑了 (⊙_⊙)?\n" +
                            "正确结构示例：\n" +
                            "• 英文邮箱： username@example.com\n" +
                            "• QQ邮箱： 123456@qq.com\n" +
                            "• 注意别多空格哦~"
            );
        }
        // 密码格式
        if (!RegexUtil.checkPassword(registerUserInfo.getPassword())) {
            log.error("弱密码拦截｜用户名：{}", registerUserInfo.getEmail());
            throw new BlogException(
                    "⚠️ 密码安全感不足！小博担心你的账号被坏人撬开 (´；ω；`)\n" +
                            "安全密码配方：\n" +
                            "• 大小写字母混合（如 BoKe123）\n" +
                            "• 加个特殊符号更保险（!@#）\n" +
                            "• 别用生日/手机号呀"
            );
        }
        // github地址格式
        if (!RegexUtil.checkGithubUrl(registerUserInfo.getGithubUrl())) {
            log.warn("GitHub URL无效｜输入值：{}", registerUserInfo.getGithubUrl());
            throw new BlogException(
                    "HTTP 400：URL格式异常！(╯°□°）╯︵ ┻━┻\n" +
                            "小博检测到以下问题：\n" +
                            "• 缺少协议头（https://）\n" +
                            "• 或包含非法字符\n" +
                            "正确示例看这里 => https://github.com/yourname"
            );
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param email 用户 邮箱
     * @return 用户信息
     */
    public UserInfo selectUserInfoByEmail(String email) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getEmail, email).eq(UserInfo::getDeleteFlag, 0));
    }

    /**
     * 根据用户 id 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }

    /**
     * 插入用户信息
     *
     * @param userInfo 用户信息
     * @return 插入的行数
     */
    private Integer insertUser(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }
}
