package com.comic.vo;

import java.time.LocalDateTime;

public class CollectVO {
    private Long collectId;       // 收藏记录ID
    private Long id;              // 漫画ID（与前端comic.id对应）
    private String title;         // 漫画标题
    private String cover;         // 封面URL
    private String authorName;    // 作者名
    private String categoryName;  // 分类名
    private Integer status;       // 状态
    private Integer viewCount;    // 阅读量
    private LocalDateTime collectTime; // 收藏时间

    // getter和setter
}
