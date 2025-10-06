package com.comic.mapper;

import com.comic.entity.ReadHistory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReadHistoryMapper {
    // 新增阅读历史
    int insert(ReadHistory history);

    // 查询用户阅读历史列表
    List<ReadHistory> selectByUserId(Long userId);

    // 新增：查询“用户-漫画-章节”是否已存在
    ReadHistory findByUserComicChapter(Long userId, Long comicId, Long chapterId);
}