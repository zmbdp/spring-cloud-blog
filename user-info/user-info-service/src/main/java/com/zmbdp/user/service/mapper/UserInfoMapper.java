package com.zmbdp.user.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmbdp.user.service.dataobject.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
