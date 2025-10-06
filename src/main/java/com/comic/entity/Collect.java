package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Collect {
    private Long id;
    private Long userId;
    private Long comicId;
    private LocalDateTime createTime;
}