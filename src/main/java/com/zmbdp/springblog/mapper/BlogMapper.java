package com.zmbdp.springblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmbdp.springblog.common.pojo.dataobject.BlogInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<BlogInfo> {
}
