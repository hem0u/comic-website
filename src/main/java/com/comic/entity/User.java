package com.comic.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data  // Lombok注解，自动生成get/set/toString等方法
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private Byte role;  // 0-普通用户，1-作者，2-管理员
    private Byte status;  // 1-正常，0-禁用
    private LocalDateTime createTime;  // Java 17支持的时间类型
    private LocalDateTime updateTime;
}