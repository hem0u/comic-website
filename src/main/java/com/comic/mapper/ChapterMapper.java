package com.comic.mapper;

import com.comic.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChapterMapper {
    // 新增章节
    int insert(Chapter chapter);

    // 根据漫画ID查询章节列表（按顺序）
    List<Chapter> selectByComicId(Long comicId);
}