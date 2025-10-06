package com.comic.service;

import com.comic.dto.LikeCreateDTO;
import com.comic.vo.ResultVO;

public interface LikeService {
    // 点赞
    ResultVO like(LikeCreateDTO likeDTO, String username);

    // 取消点赞
    ResultVO cancelLike(LikeCreateDTO likeDTO, String username);

    // 检查是否已点赞
    ResultVO checkLike(LikeCreateDTO likeDTO, String username);
}