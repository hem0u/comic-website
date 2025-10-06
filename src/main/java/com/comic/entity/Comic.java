package com.comic.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comic {
    private Long id;
    private String title;
    private Long authorId;  // 关联用户ID
    private String cover;   // 封面URL
    private String description;
    private Integer categoryId;  // 关联分类ID
    private Byte status;    // 0-连载中，1-已完结
    private Byte isApproved;  // 0-待审核，1-通过，2-拒绝
    private Integer viewCount;  // 阅读量
    private Integer collectCount;  // 收藏数
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}