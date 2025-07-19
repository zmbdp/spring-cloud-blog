package com.zmbdp.blog.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmbdp.springblog.common.pojo.dataobject.BlogInfo;
import com.zmbdp.springblog.common.pojo.request.AddBlogInfoRequest;
import com.zmbdp.springblog.common.pojo.request.UpBlogRequest;
import com.zmbdp.springblog.common.pojo.response.BlogInfoResponse;
import com.zmbdp.springblog.mapper.BlogMapper;
import com.zmbdp.springblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    /**
     * 获取博客列表
     *
     * @return 博客列表
     */
    public List<BlogInfoResponse> getList() {
        //从数据库查询数据
        List<BlogInfo> blogInfos = blogMapper.selectList(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag, 0).orderByDesc(BlogInfo::getId));
        //转换数据格式
        List<BlogInfoResponse> blogInfoResponses = blogInfos.stream().map(blogInfo -> {
            BlogInfoResponse response = new BlogInfoResponse();
            BeanUtils.copyProperties(blogInfo, response);
            return response;
        }).collect(Collectors.toList());
        return blogInfoResponses;
    }

    /**
     * 获取博客详情
     *
     * @param blogId 博客 id
     * @return 博客详情
     */
    public BlogInfoResponse getBlogDetail(Integer blogId) {
        BlogInfoResponse blogInfoResponse = new BlogInfoResponse();
        BlogInfo blogInfo = getBlogInfo(blogId);
        BeanUtils.copyProperties(blogInfo, blogInfoResponse);

        return blogInfoResponse;
    }

    /**
     * 删除博客
     *
     * @param blogId 删除的博客 id
     * @return 是否删除成功
     */
    public Boolean delete(Integer blogId) {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setId(blogId);
        blogInfo.setDeleteFlag(1);
        Integer result = null;
        try {
            result = this.updateBlog(blogInfo);
            if (result >= 1) {
                return true;
            }

        } catch (Exception e) {
            log.error("updateBlog 发生异常, e: ", e);
        }
        return false;
    }

    /**
     * 插入博客
     *
     * @param addBlogInfoRequest 博客信息
     * @return 插入的行数
     */
    public Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest) {
        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(addBlogInfoRequest, blogInfo);
        Integer result = null;
        try {
            result = insertBlog(blogInfo);
            if (result == 1) {
                return true;
            }
        } catch (Exception e) {
            log.error("发布博客失败, e:", e);
        }
        return false;
    }

    /**
     * 更新博客
     *
     * @param upBlogRequest 更新博客参数
     * @return 是否更新成功
     */
    public Boolean update(UpBlogRequest upBlogRequest) {
        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(upBlogRequest, blogInfo);
        Integer result = null;
        try {
            result = this.updateBlog(blogInfo);
            if (result >= 1) {
                return true;
            }

        } catch (Exception e) {
            log.error("updateBlog 发生异常, e: ", e);
        }
        return false;
    }

    private BlogInfo getBlogInfo(Integer blogId) {
        return blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag, 0).eq(BlogInfo::getId, blogId));
    }

    private Integer insertBlog(BlogInfo blogInfo) {
        return blogMapper.insert(blogInfo);
    }

    public Integer updateBlog(BlogInfo blogInfo) {
        return blogMapper.updateById(blogInfo);
    }
}
