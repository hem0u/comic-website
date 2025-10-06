package com.comic.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Chapter {
    private Long id;
    private Long comicId;  // 关联漫画ID
    private String title;
    private Integer sort;  // 排序序号
    private LocalDateTime createTime;
}