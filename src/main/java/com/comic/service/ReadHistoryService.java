package com.comic.service;

import com.comic.vo.ResultVO;

public interface ReadHistoryService {
    // 记录阅读历史
    ResultVO recordReadHistory(Long comicId, Long chapterId, String username);

    // 获取用户阅读历史
    ResultVO getUserReadHistories(String username);
}