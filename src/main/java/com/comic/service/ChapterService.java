package com.comic.service;

import com.comic.dto.ChapterCreateDTO;
import com.comic.vo.ChapterVO;
import com.comic.vo.ResultVO;

import java.util.List;

public interface ChapterService {
    // 创建章节
    ResultVO createChapter(ChapterCreateDTO chapterDTO, String username);

    // 检查用户是否有章节操作权限
    boolean checkChapterPermission(Long chapterId, String username);

    // 添加章节图片
    void addChapterImage(Long chapterId, String imageUrl, Integer sort);

    List<ChapterVO> getChaptersByComicId(Long id);
}