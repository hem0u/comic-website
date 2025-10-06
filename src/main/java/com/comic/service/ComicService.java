package com.comic.service;

import com.comic.dto.ComicCreateDTO;
import com.comic.dto.ComicQueryDTO;
import com.comic.vo.ComicVO;
import com.comic.vo.PageVO;
import com.comic.vo.ResultVO;

public interface ComicService {
    // 创建漫画（需要作者权限）
    ResultVO<ComicVO> createComic(ComicCreateDTO comicDTO, String username);

    // 根据ID查询漫画详情
    ResultVO<ComicVO> getComicById(Long id);

    // 新增：获取漫画详情（含章节和图片）
    ResultVO<ComicVO> getComicDetail(Long id);

    // 新增：分页查询漫画列表
    ResultVO<PageVO<ComicVO>> listComics(ComicQueryDTO queryDTO);
}