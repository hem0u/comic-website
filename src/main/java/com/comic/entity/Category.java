package com.comic.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private String name;
    private Integer sort;
    private Integer parentId;
    private LocalDateTime createTime;
}