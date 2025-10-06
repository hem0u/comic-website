package com.comic.controller;

import com.comic.dto.UserLoginDTO;
import com.comic.dto.UserRegisterDTO;
import com.comic.entity.User;
import com.comic.mapper.UserMapper;
import com.comic.service.UserService;
import com.comic.vo.ResultVO;
import com.comic.vo.UserInfoVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    // 注册接口
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    // 登录接口：POST /api/user/login
    @PostMapping("/login")
    public ResultVO login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @GetMapping("/info")
    public ResultVO getUserInfo() {
        // 从Security上下文获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 查询用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 封装VO（过滤密码等敏感字段）
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setAvatar(user.getAvatar());  // 若未设置头像，返回null

        return ResultVO.success(userInfoVO);
    }
}
