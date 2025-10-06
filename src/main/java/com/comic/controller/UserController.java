package com.comic.controller;

import com.comic.dto.UserLoginDTO;
import com.comic.dto.UserRegisterDTO;
import com.comic.service.UserService;
import com.comic.vo.ResultVO;
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

    // 测试接口：需要登录才能访问
    @GetMapping("/info")
    public ResultVO getUserInfo() {
        // 从Security上下文获取当前登录用户的用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResultVO.success("当前登录用户：" + username);
    }
}
