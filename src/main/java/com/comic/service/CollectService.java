package com.comic.service;

import com.comic.vo.ResultVO;

public interface CollectService {
    // 收藏漫画
    ResultVO collectComic(Long comicId, String username);

    // 取消收藏漫画
    ResultVO cancelCollect(Long comicId, String username);

    // 检查是否已收藏
    ResultVO checkCollect(Long comicId, String username);

    // 获取用户收藏列表
    ResultVO getUserCollects(String username);

}