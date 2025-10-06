package com.comic.controller;

import com.comic.dto.CommentCreateDTO;
import com.comic.service.CommentService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    // 提交评论：POST /api/comment
    @PostMapping
    public ResultVO submitComment(@Valid @RequestBody CommentCreateDTO commentDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return commentService.submitComment(commentDTO, username);
    }

    // 查询漫画评论：GET /api/comment/comic/{comicId}?page=1&size=10
    @GetMapping("/comic/{comicId}")
    public ResultVO getComicComments(
            @PathVariable Long comicId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getComicComments(comicId, page, size);
    }

    // 查询章节评论：GET /api/comment/chapter/{chapterId}?page=1&size=10
    @GetMapping("/chapter/{chapterId}")
    public ResultVO getChapterComments(
            @PathVariable Long chapterId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getChapterComments(chapterId, page, size);
    }
}