package com.comic.service;

import com.comic.dto.ChapterCreateDTO;
import com.comic.vo.ResultVO;

public interface ChapterService {
    // 创建章节（只有漫画作者可操作）
    ResultVO createChapter(ChapterCreateDTO chapterDTO, String username);
}