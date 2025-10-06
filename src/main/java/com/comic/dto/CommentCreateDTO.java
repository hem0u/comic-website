package com.comic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDTO {
    @NotNull(message = "漫画ID不能为空")
    private Long comicId;  // 必选：所属漫画ID

    private Long chapterId;  // 可选：所属章节ID（null表示评论漫画）

    @NotBlank(message = "评论内容不能为空")
    private String content;  // 必选：评论内容（限500字内）
}