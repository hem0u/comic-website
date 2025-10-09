package com.comic.service.impl;

import com.comic.dto.UserLoginDTO;
import com.comic.dto.UserRegisterDTO;
import com.comic.entity.User;
import com.comic.mapper.UserMapper;
import com.comic.service.UserService;
import com.comic.utils.JwtUtils;
import com.comic.vo.LoginVO;
import com.comic.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtils jwtUtils;  // 注入JWT工具类

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResultVO register(UserRegisterDTO registerDTO) {
        // 已实现的注册逻辑（略）
        User existingUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            return ResultVO.error("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole((byte) 0);
        user.setStatus((byte) 1);
        user.setAvatar("https://picsum.photos/200");

        userMapper.insert(user);
        return ResultVO.success("注册成功");
    }

    @Override
    public ResultVO<LoginVO> login(UserLoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            return ResultVO.error("用户名或密码错误");
        }

        // 2. 验证密码（加密后对比）
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResultVO.error("用户名或密码错误");
        }

        // 3. 检查账号状态
        if (user.getStatus() == 0) {
            return ResultVO.error("账号已禁用");
        }

        // 4. 生成JWT令牌
        String token = jwtUtils.generateToken(user.getUsername());

        // 5. 封装返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUsername(user.getUsername());
        loginVO.setNickname(user.getNickname());
        // 如果用户没有设置头像，提供默认头像
        loginVO.setAvatar(user.getAvatar() != null ? user.getAvatar() : "https://picsum.photos/200"); // 添加用户头像信息

        return ResultVO.success(loginVO);
    }
}
