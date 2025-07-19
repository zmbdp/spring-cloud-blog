package com.zmbdp.springblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmbdp.springblog.common.utils.JWTUtils;
import com.zmbdp.springblog.common.exception.BlogException;
import com.zmbdp.springblog.common.pojo.dataobject.BlogInfo;
import com.zmbdp.springblog.common.pojo.dataobject.UserInfo;
import com.zmbdp.springblog.common.pojo.request.UserInfoRequest;
import com.zmbdp.springblog.common.pojo.response.UserInfoResponse;
import com.zmbdp.springblog.common.pojo.response.UserLoginResponse;
import com.zmbdp.springblog.common.utils.SecurityUtil;
import com.zmbdp.springblog.mapper.BlogMapper;
import com.zmbdp.springblog.mapper.UserInfoMapper;
import com.zmbdp.springblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String KAPTCHA_SESSION_KEY = "ADMIN_KAPTCHA_SESSION_KEY";
    private static final String KAPTCHA_SESSION_DATE = "ADMIN_KAPTCHA_SESSION_DATE";
    private static final Long SESSION_TIMEOUT = 60 * 1000L;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BlogMapper blogMapper;

    /**
     * 根据博客 id 查询作者 id，然后再根据作者 id 查询作者信息
     *
     * @param blogId 博客 id
     * @return 作者信息
     */
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {

        //1. 根据博客ID, 获取作者ID
        BlogInfo blogInfo = getBlogInfo(blogId);
        //2. 根据作者ID, 获取作者信息
        if (blogInfo == null) {
            throw new BlogException("博客不存在");
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = null;
        try {
            userInfo = selectUserInfoById(blogInfo.getUserId());
            BeanUtils.copyProperties(userInfo, userInfoResponse);
        } catch (Exception e) {
            throw new BlogException("改用户不存在");
        }
        return userInfoResponse;
    }

    /**
     * 登录接口
     *
     * @param user    用户信息
     * @param captcha 验证码
     * @param session session
     * @return 登录结果
     */
    @Override
    public UserLoginResponse login(UserInfoRequest user, String captcha, HttpSession session) {
        // 验证验证码是否正确
        if (!check(captcha, session)) {
            throw new BlogException("验证码错误，请重试");
        }
        //验证账号密码是否正确
        UserInfo userInfo = selectUserInfoByName(user.getUserName());
        if (userInfo == null || userInfo.getId() == null || !SecurityUtil.verify(user.getPassword(), userInfo.getPassword())) {
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
     * @param userInfo 想注册用户
     * @param captcha  验证码
     * @param session  session
     * @return 注册成功返回 true，否则返回 false
     */
    public Boolean register(UserInfo userInfo, String captcha, HttpSession session) {
        // 判断验证码是否正确，然后直接把用户的信息存到文件里面
        if (!check(captcha, session)) {
            return false;
        }
        // 判断一下，如果数据库返回 0，就是没插入成功，说明用户存在
        int flag = 0;
        userInfo.setPassword(SecurityUtil.encrypt(userInfo.getPassword()));
        try {
            flag = userInfoMapper.insert(userInfo);
        } catch (NullPointerException e) {
            log.error("用户存在");
            return false;
        }
        return flag >= 0;
    }

    /**
     * 判断验证码是否正确
     *
     * @param captcha 用户输入的验证码
     * @param session 当前会话
     * @return 验证是否通过
     */
    public Boolean check(String captcha, HttpSession session) {
        // 先判断输入过来的验证码是否为空
        if (!StringUtils.hasLength(captcha)) {
            return false;
        }
        // 再判断是否相等
        if (!captcha.equals(session.getAttribute(KAPTCHA_SESSION_KEY))) {
            return false;
        }
        // 再看时间
        Date saveDate = (Date) session.getAttribute(KAPTCHA_SESSION_DATE);
        // 如果等于空，说明没刷新，就不能进去
        if (
                null != saveDate &&
                        System.currentTimeMillis() - saveDate.getTime() < SESSION_TIMEOUT
        ) {
            return true;
        }
        return false;
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
     * 根据博客 id 查询博客信息
     *
     * @param blogId 博客 id
     * @return 博客信息
     */
    private BlogInfo getBlogInfo(Integer blogId) {
        return blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag, 0).eq(BlogInfo::getId, blogId));
    }
}
