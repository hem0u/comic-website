package com.comic.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Image {
    private Long id;
    private Long chapterId;  // 所属章节ID
    private String url;      // 图片URL
    private Integer sort;    // 排序序号
    private LocalDateTime createTime;
}