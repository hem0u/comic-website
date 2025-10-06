package com.comic.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private Integer total;    // 总记录数
    private Integer page;     // 当前页码
    private Integer size;     // 每页大小
    private List<T> list;     // 数据列表
}