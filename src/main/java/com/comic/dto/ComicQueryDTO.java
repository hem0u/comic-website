package com.comic.dto;

import lombok.Data;

@Data
public class ComicQueryDTO {
    private Integer page = 1;      // 页码，默认第1页
    private Integer size = 10;     // 每页数量，默认10条
    private String keyword;        // 搜索关键词（标题、作者）
    private Integer categoryId;    // 分类ID，用于筛选
}