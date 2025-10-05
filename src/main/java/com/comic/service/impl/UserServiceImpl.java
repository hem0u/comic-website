package com.comic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.comic.dto.UserRegisterDTO;
import com.comic.entity.User;
import com.comic.mapper.UserMapper;
import com.comic.service.UserService;
import com.comic.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;  // Spring Boot 3.x 使用这个

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    // 密码加密器（Spring Security提供的BCrypt算法）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResultVO register(UserRegisterDTO registerDTO) {
        // 1. 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            return ResultVO.error("用户名已存在");
        }

        // 2. 转换DTO为实体类
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user); // 复制属性

        // 3. 密码加密（重要：绝不能存储明文密码）
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // 4. 设置默认值
        user.setRole((byte) 0); // 普通用户
        user.setStatus((byte) 1); // 账号正常
        user.setAvatar("https://picsum.photos/200"); // 默认头像

        // 5. 保存到数据库
        userMapper.insert(user);

        return ResultVO.success("注册成功");
    }
}