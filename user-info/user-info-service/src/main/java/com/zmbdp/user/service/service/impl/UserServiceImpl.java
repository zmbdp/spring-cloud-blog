package com.zmbdp.user.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmbdp.blog.api.BlogServiceApi;
import com.zmbdp.blog.api.pojo.response.BlogInfoResponse;
import com.zmbdp.common.exception.BlogException;
import com.zmbdp.common.pojo.Result;
import com.zmbdp.common.utils.JWTUtils;
import com.zmbdp.common.utils.RegexUtil;
import com.zmbdp.common.utils.SecurityUtil;
import com.zmbdp.user.api.pojo.request.LoginUserInfoRequest;
import com.zmbdp.user.api.pojo.request.UserInfoRegisterRequest;
import com.zmbdp.user.api.pojo.response.UserInfoResponse;
import com.zmbdp.user.api.pojo.response.UserLoginResponse;
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

    /**
     * 登录接口
     *
     * @param loginUserInfoRequest 用户信息
     * @return 登录结果
     */
    @Override
    public UserLoginResponse login(LoginUserInfoRequest loginUserInfoRequest) {
        // 验证验证码是否正确，gateway 里面验证
        //验证账号密码是否正确
        UserInfo userInfo = selectUserInfoByName(loginUserInfoRequest.getUserName());
        if (
                userInfo == null || userInfo.getId() == null ||
                        !SecurityUtil.verify(loginUserInfoRequest.getPassword(), userInfo.getPassword())
        ) {
            throw new BlogException("用户名或密码错误");
        }
        //账号密码正确的逻辑
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());

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
            throw new BlogException("博客不存在");
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = null;
        try {
            userInfo = selectUserInfoById(blogInfoResponseResult.getData().getUserId());
            BeanUtils.copyProperties(userInfo, userInfoResponse);
        } catch (Exception e) {
            throw new BlogException("改用户不存在");
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
    public Integer register(UserInfoRegisterRequest registerUserInfo) {
        // 参数校验: 用户名不能重复, 密码格式, 邮箱格式, github地址格式
        checkUserInfo(registerUserInfo);
        // 判断一下，如果数据库返回 0，就是没插入成功，说明用户存在
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerUserInfo, userInfo);
        Integer flag = 0;
        userInfo.setPassword(SecurityUtil.encrypt(registerUserInfo.getPassword()));
        try {
            flag = insertBlog(userInfo);
        } catch (Exception e) {
            log.error("用户注册失败, 用户名: {}, 用户id: {}, e: {}", registerUserInfo.getUserName(), userInfo.getId(), e.getMessage());
            throw new BlogException("请联系管理员哦, 小博罢工飞走噜(～￣(OO)￣)ブ");
        }
        if (flag == 0) {
            throw new BlogException("用户注册失败, 小博太没用了/(ㄒoㄒ)/~~");
        }
        log.info("用户注册成功, 用户名: {}, 用户id: {}", registerUserInfo.getUserName(), userInfo.getId());
        return userInfo.getId();
    }

    private void checkUserInfo(UserInfoRegisterRequest registerUserInfo) {
        // 用户名不能重复
        UserInfo userInfo = selectUserInfoByName(registerUserInfo.getUserName());
        if (userInfo != null) {
            throw new BlogException("当前名字重复啦, 小博会弄混掉的(T_T)");
        }
        // 密码格式
        if (!RegexUtil.checkPassword(registerUserInfo.getPassword())) {
            throw new BlogException("密码过于简单了, 坏人一下子就进去了呢, 请输入6-20位的密码哦(●ˇ∀ˇ●)");
        }
        // 邮箱格式
        if (!RegexUtil.checkMail(registerUserInfo.getEmail())) {
            throw new BlogException("小博帮你检查到邮箱格式不对呢, 请检查邮箱格式哦~");
        }
        // github地址格式
        if (!RegexUtil.checkGithubUrl(registerUserInfo.getGithubUrl())) {
            throw new BlogException("小博很生气, 这样的GitHub简直就是再逗小博(╯▔皿▔)╯");
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
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
    private Integer insertBlog(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }
}
