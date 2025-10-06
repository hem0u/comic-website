package com.comic.vo;

import com.comic.entity.Image;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChapterVO {
    private Long id;
    private Long comicId;
    private String title;
    private Integer sort;
    private LocalDateTime createTime;
    // 添加存储图片的列表
    private List<Image> images;
}