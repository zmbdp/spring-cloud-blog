package com.zmbdp.blog.api;

import com.zmbdp.blog.api.pojo.request.AddBlogInfoRequest;
import com.zmbdp.blog.api.pojo.request.UpBlogRequest;
import com.zmbdp.blog.api.pojo.response.BlogInfoResponse;
import com.zmbdp.common.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "blog-service", path = "/blog")
public interface BlogServiceApi {

    /**
     * 获取博客列表
     *
     * @return 博客列表
     */
    @RequestMapping("/getList")
    List<BlogInfoResponse> getList();

    /**
     * 根据博客 id 获取博客
     *
     * @param blogId 博客 id
     * @return 博客
     */
    @RequestMapping("/getBlogDetail")
    BlogInfoResponse getBlogDetail(Integer blogId);

    /**
     * 修改博客
     *
     * @param upBlogRequest 修改的博客信息
     * @return 修改的行数
     */
    @RequestMapping("/update")
    Boolean update(@RequestBody UpBlogRequest upBlogRequest);

    /**
     * 删除博客
     *
     * @param blogId 博客 id
     * @return 删除的行数
     */
    @RequestMapping("/delete")
    Boolean delete(Integer blogId);

    @RequestMapping("/insertBlog")
    Result<Boolean> insertBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest);
}
