package com.zmbdp.blog.service.controller;

import com.zmbdp.springblog.common.pojo.request.AddBlogInfoRequest;
import com.zmbdp.springblog.common.pojo.request.UpBlogRequest;
import com.zmbdp.springblog.common.pojo.response.BlogInfoResponse;
import com.zmbdp.springblog.common.pojo.response.Result;
import com.zmbdp.springblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    /**
     * 获取博客列表
     * @return 博客列表
     */
    @RequestMapping("/getList")
    public List<BlogInfoResponse> getList(){
        return blogService.getList();
    }

    /**
     * 根据博客 id 获取博客
     * @param blogId 博客 id
     * @return 博客
     */
    @RequestMapping("/getBlogDetail")
    public BlogInfoResponse getBlogDetail(@NotNull Integer blogId, HttpServletRequest request) {
        log.info("getBlogDetail, blogId: {}", blogId);
        return blogService.getBlogDetail(blogId);
    }

    /**
     * 修改博客
     * @param upBlogRequest 修改的博客信息
     * @return 修改的行数
     */
    @RequestMapping("/update")
    public Boolean update(@Valid @RequestBody UpBlogRequest upBlogRequest) {
        log.info("updateBlog 接收参数: "+ upBlogRequest);
        return blogService.update(upBlogRequest);
    }
    /**
     * 删除博客
     * @param blogId
     * @return
     */
    @RequestMapping("/delete")
    public Boolean delete(@NotNull Integer blogId) {
        if (blogId == null || blogId < 1) {
            return false;
        }
        log.info("deleteBlog 接收参数: "+ blogId);
        return blogService.delete(blogId);
    }

    @RequestMapping("/insertBlog")
    public Result<Boolean> insertBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest) {
        log.info("addBlog 接收参数: "+ addBlogInfoRequest);
        return Result.success(blogService.addBlog(addBlogInfoRequest));
    }
}
