package com.zmbdp.blog.service.controller;

import com.zmbdp.blog.api.BlogServiceApi;
import com.zmbdp.blog.api.pojo.request.AddBlogInfoRequest;
import com.zmbdp.blog.api.pojo.request.UpBlogRequest;
import com.zmbdp.blog.api.pojo.response.BlogInfoResponse;
import com.zmbdp.blog.service.service.BlogService;
import com.zmbdp.common.pojo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController implements BlogServiceApi {
    @Autowired
    private BlogService blogService;

    /**
     * 获取博客列表
     * @return 博客列表
     */
    @Override
    public Result<List<BlogInfoResponse>> getList(){
        return Result.success(blogService.getList());
    }

    /**
     * 根据博客 id 获取博客
     * @param blogId 博客 id
     * @return 博客
     */
    @Override
    public Result<BlogInfoResponse> getBlogDetail(@NotNull Integer blogId) {
        log.info("getBlogDetail, blogId: {}", blogId);
        return Result.success(blogService.getBlogDetail(blogId));
    }

    /**
     * 修改博客
     * @param upBlogRequest 修改的博客信息
     * @return 修改的行数
     */
    @Override
    public Result<Boolean> update(@Valid @RequestBody UpBlogRequest upBlogRequest) {
        log.info("updateBlog 接收参数: "+ upBlogRequest);
        return Result.success(blogService.update(upBlogRequest));
    }
    /**
     * 删除博客
     * @param blogId 博客 id
     * @return 删除的行数
     */
    @Override
    public Result<Boolean> delete(@NotNull Integer blogId) {
        if (blogId == null || blogId < 1) {
            throw new RuntimeException("请联系管理员哦, 小博罢工飞走噜(～￣(OO)￣)ブ");
        }
        log.info("deleteBlog 接收参数: "+ blogId);
        return Result.success(blogService.delete(blogId));
    }

    /**
     * 添加博客
     * @param addBlogInfoRequest 添加的博客信息
     * @return 添加的行数
     */
    @Override
    public Result<Boolean> insertBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest) {
        log.info("addBlog 接收参数: "+ addBlogInfoRequest);
        return Result.success(blogService.addBlog(addBlogInfoRequest));
    }
}
