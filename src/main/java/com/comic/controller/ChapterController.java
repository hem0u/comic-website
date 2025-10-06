package com.comic.controller;

import com.comic.dto.ChapterCreateDTO;
import com.comic.service.ChapterService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    @Resource
    private ChapterService chapterService;

    // 创建章节：POST /api/chapter（需要登录且为漫画作者）
    @PostMapping
    public ResultVO createChapter(@Valid @RequestBody ChapterCreateDTO chapterDTO) {
        // 获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return chapterService.createChapter(chapterDTO, username);
    }
}