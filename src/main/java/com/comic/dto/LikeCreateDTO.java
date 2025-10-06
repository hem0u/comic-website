package com.comic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeCreateDTO {
    @NotNull(message = "点赞目标类型不能为空")
    private Byte targetType;  // 1-漫画，2-章节

    @NotNull(message = "目标ID不能为空")
    private Long targetId;    // 漫画ID或章节ID
}