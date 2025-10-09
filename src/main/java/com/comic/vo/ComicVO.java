package com.comic.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComicVO {
    private Long id;
    private String title;
    private String authorName;  // 作者昵称（不是ID）
    private String cover;
    private String description;
    private String categoryName;  // 分类名称
    private Integer categoryId;   // 分类ID，用于前端筛选
    private Byte status;
    private Integer viewCount;
    private Integer collectCount;
    private LocalDateTime createTime;
    private List<ChapterVO> chapters;  // 包含的章节列表
}