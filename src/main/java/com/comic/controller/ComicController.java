package com.comic.controller;

import com.comic.dto.ComicCreateDTO;
import com.comic.service.ComicService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comic")
public class ComicController {

    @Resource
    private ComicService comicService;

    // 创建漫画：POST /api/comic（需要登录）
    @PostMapping
    public ResultVO createComic(@Valid @RequestBody ComicCreateDTO comicDTO) {
        // 获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return comicService.createComic(comicDTO, username);
    }

    // 查询漫画详情：GET /api/comic/{id}
    @GetMapping("/{id}")
    public ResultVO getComicById(@PathVariable Long id) {
        return comicService.getComicById(id);
    }
}