package com.comic.controller;

import com.comic.dto.LikeCreateDTO;
import com.comic.service.LikeService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    // 点赞：POST /api/like
    @PostMapping
    public ResultVO like(@Valid @RequestBody LikeCreateDTO likeDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return likeService.like(likeDTO, username);
    }

    // 取消点赞：DELETE /api/like
    @DeleteMapping
    public ResultVO cancelLike(@Valid @RequestBody LikeCreateDTO likeDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return likeService.cancelLike(likeDTO, username);
    }

    // 检查是否点赞：GET /api/like/check
    @PostMapping("/check")
    public ResultVO checkLike(@Valid @RequestBody LikeCreateDTO likeDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return likeService.checkLike(likeDTO, username);
    }
}