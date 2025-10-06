package com.comic.service.impl;

import com.comic.dto.ChapterCreateDTO;
import com.comic.entity.Chapter;
import com.comic.entity.Comic;
import com.comic.entity.User;
import com.comic.mapper.ChapterMapper;
import com.comic.mapper.ComicMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.ChapterService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    @Resource
    private ComicMapper comicMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public ResultVO createChapter(ChapterCreateDTO chapterDTO, String username) {
        // 1. 查询当前用户ID
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 2. 查询漫画信息，验证是否为当前用户创建
        Comic comic = comicMapper.selectById(chapterDTO.getComicId());
        if (comic == null) {
            return ResultVO.error("漫画不存在");
        }
        if (!comic.getAuthorId().equals(user.getId())) {
            return ResultVO.error("没有权限创建该漫画的章节");
        }

        // 3. 保存章节
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDTO, chapter);
        chapterMapper.insert(chapter);

        return ResultVO.success("章节创建成功");
    }
}