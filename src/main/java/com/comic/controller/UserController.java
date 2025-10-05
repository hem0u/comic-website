package com.comic.controller;

import com.comic.dto.UserRegisterDTO;
import com.comic.service.UserService;
import com.comic.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.annotation.Resource;  // Spring Boot 3.x 使用这个

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    // 注册接口：POST /api/user/register
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        // @Valid用于参数校验（如用户名长度、邮箱格式）
        return userService.register(registerDTO);
    }
}