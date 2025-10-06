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
}