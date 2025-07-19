package com.zmbdp.blog.service.service;

import com.zmbdp.blog.api.pojo.request.AddBlogInfoRequest;
import com.zmbdp.blog.api.pojo.request.UpBlogRequest;
import com.zmbdp.blog.api.pojo.response.BlogInfoResponse;

import java.util.List;

public interface BlogService {
    // 获取博客列表
    List<BlogInfoResponse> getList();
    // 获取博客详情
    BlogInfoResponse getBlogDetail(Integer id);
    // 删除博客
    Boolean delete(Integer blogId);
    // 插入博客
    Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest);
    // 修改博客
    Boolean update(UpBlogRequest upBlogRequest);
}
