package com.comic.controller;

import com.comic.service.ReadHistoryService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class ReadHistoryController {

    @Resource
    private ReadHistoryService historyService;

    // 记录阅读历史：POST /api/history?comicId={}&chapterId={}
    @PostMapping
    public ResultVO recordReadHistory(
            @RequestParam Long comicId,
            @RequestParam Long chapterId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return historyService.recordReadHistory(comicId, chapterId, username);
    }

    // 获取用户阅读历史：GET /api/history/user
    @GetMapping("/user")
    public ResultVO getUserReadHistories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return historyService.getUserReadHistories(username);
    }
}