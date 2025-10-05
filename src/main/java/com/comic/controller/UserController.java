package com.comic.controller;

import com.comic.dto.UserLoginDTO;
import com.comic.dto.UserRegisterDTO;
import com.comic.service.UserService;
import com.comic.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
