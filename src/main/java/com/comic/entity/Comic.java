package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comic {
    private Long id;
    private String title;
    private Long authorId;  // 确保该字段存在且类型为Long
    private String cover;
    private String description;
    private Integer categoryId;
    private Byte status;
    private Byte isApproved;
    private Integer viewCount;
    private Integer collectCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}