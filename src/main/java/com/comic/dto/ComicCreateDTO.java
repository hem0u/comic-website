package com.comic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComicCreateDTO {
    @NotBlank(message = "漫画标题不能为空")
    private String title;  // 漫画标题

    private String cover;  // 封面URL（后续会实现上传功能，这里先手动输入）

    private String description;  // 漫画简介

    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;  // 所属分类ID

    private Byte status = 0;  // 状态：0-连载中（默认），1-已完结
}