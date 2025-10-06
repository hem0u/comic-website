package com.comic.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 阅读历史展示对象，用于前端展示阅读历史
 */
@Data
public class HistoryVO {
    private Long historyId;          // 历史记录ID
    private Long comicId;            // 漫画ID
    private String comicTitle;       // 漫画标题
    private String cover;            // 封面图片URL
    private String authorName;       // 作者名称
    private String categoryName;     // 分类名称
    private Long chapterId;          // 章节ID
    private String chapterTitle;     // 章节标题
    private Integer readPosition;    // 阅读位置
    private LocalDateTime readTime;  // 阅读时间
}
