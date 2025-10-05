package com.comic.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;  // JWT令牌
    private String username;  // 用户名
    private String nickname;  // 昵称
}