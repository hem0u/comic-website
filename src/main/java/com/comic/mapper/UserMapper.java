package com.comic.mapper;

import com.comic.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper  // 标记为MyBatis映射接口
public interface UserMapper {
    // 根据用户名查询用户（登录时用）
    User selectByUsername(String username);

    // 新增用户（注册时用）
    int insert(User user);
}