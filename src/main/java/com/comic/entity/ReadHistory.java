package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReadHistory {
    private Long id;
    private Long userId;
    private Long comicId;
    private Long chapterId;
    private LocalDateTime readTime;
}