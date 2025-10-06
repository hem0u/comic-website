package com.comic.controller;

import com.comic.service.CollectService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    // 收藏漫画：POST /api/collect/{comicId}
    @PostMapping("/{comicId}")
    public ResultVO collectComic(@PathVariable Long comicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return collectService.collectComic(comicId, username);
    }

    // 取消收藏：DELETE /api/collect/{comicId}
    @DeleteMapping("/{comicId}")
    public ResultVO cancelCollect(@PathVariable Long comicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return collectService.cancelCollect(comicId, username);
    }

    // 检查是否收藏：GET /api/collect/check/{comicId}
    @GetMapping("/check/{comicId}")
    public ResultVO checkCollect(@PathVariable Long comicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return collectService.checkCollect(comicId, username);
    }

    // 获取用户收藏列表：GET /api/collect/user
    @GetMapping("/user")
    public ResultVO getUserCollects() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return collectService.getUserCollects(username);
    }
}