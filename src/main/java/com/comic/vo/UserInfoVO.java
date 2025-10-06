package com.comic.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private String username;  // 用户名（不可修改）
    private String nickname;  // 昵称（展示用）
    private String email;     // 邮箱
    private String avatar;    // 头像URL（默认可空）
}