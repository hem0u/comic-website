package com.comic.service;

import com.comic.dto.CommentCreateDTO;
import com.comic.vo.CommentVO;
import com.comic.vo.PageVO;
import com.comic.vo.ResultVO;

public interface CommentService {
    // 提交评论
    ResultVO submitComment(CommentCreateDTO commentDTO, String username);

    // 分页查询漫画的评论
    ResultVO<PageVO<CommentVO>> getComicComments(Long comicId, Integer page, Integer size);

    // 分页查询章节的评论
    ResultVO<PageVO<CommentVO>> getChapterComments(Long chapterId, Integer page, Integer size);
}