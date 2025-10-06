package com.comic.service.impl;

import com.comic.dto.ChapterCreateDTO;
import com.comic.entity.Chapter;
import com.comic.entity.Comic;
import com.comic.entity.Image;
import com.comic.entity.User;
import com.comic.mapper.ChapterMapper;
import com.comic.mapper.ComicMapper;
import com.comic.mapper.ImageMapper;
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

    @Resource
    private ImageMapper imageMapper;

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

        if (comic.getAuthorId() == null) {
            return ResultVO.error("漫画的作者信息不完整");
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

    @Override
    public boolean checkChapterPermission(Long chapterId, String username) {
        // 1. 查询章节是否存在
        Chapter chapter = chapterMapper.selectById(chapterId);  // 需要在ChapterMapper添加selectById方法
        if (chapter == null) {
            return false;
        }

        // 2. 查询漫画作者
        Comic comic = comicMapper.selectById(chapter.getComicId());
        User author = userMapper.selectById(comic.getAuthorId());

        // 3. 验证当前用户是否为作者
        return author.getUsername().equals(username);
    }

    @Override
    public void addChapterImage(Long chapterId, String imageUrl, Integer sort) {
        Image image = new Image();
        image.setChapterId(chapterId);
        image.setUrl(imageUrl);
        image.setSort(sort);
        imageMapper.insert(image);
    }
}