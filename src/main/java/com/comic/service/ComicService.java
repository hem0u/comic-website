package com.comic.service;

import com.comic.dto.ComicCreateDTO;
import com.comic.vo.ComicVO;
import com.comic.vo.ResultVO;

public interface ComicService {
    // 创建漫画（需要作者权限）
    ResultVO<ComicVO> createComic(ComicCreateDTO comicDTO, String username);

    // 根据ID查询漫画详情
    ResultVO<ComicVO> getComicById(Long id);
}