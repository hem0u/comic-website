package com.comic.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChapterVO {
    private Long id;
    private Long comicId;
    private String title;
    private Integer sort;
    private LocalDateTime createTime;
}