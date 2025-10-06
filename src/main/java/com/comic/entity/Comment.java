package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long userId;
    private Long comicId;
    private Long chapterId;  // 可为null
    private String content;
    private LocalDateTime createTime;
}