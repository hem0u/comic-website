package com.comic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChapterCreateDTO {
    @NotNull(message = "漫画ID不能为空")
    private Long comicId;  // 所属漫画ID

    @NotBlank(message = "章节标题不能为空")
    private String title;  // 章节标题

    @NotNull(message = "排序序号不能为空")
    private Integer sort;  // 排序序号（决定章节顺序）
}