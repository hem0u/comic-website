package com.comic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 测试接口：访问 http://localhost:8080/hello 会返回这个字符串
    @GetMapping("/hello")
    public String hello() {
        return "漫画网站后端启动成功！";
    }
}