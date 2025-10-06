package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Like {
    private Long id;
    private Long userId;
    private Byte targetType;  // 1-漫画，2-章节
    private Long targetId;
    private LocalDateTime createTime;
}