package com.comic.mapper;

import com.comic.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {
    // 新增点赞
    int insert(Like like);

    // 删除点赞（根据用户、类型、目标ID）
    int deleteByUserAndTarget(Long userId, Byte targetType, Long targetId);

    // 检查用户是否已点赞
    Integer checkLike(Long userId, Byte targetType, Long targetId);
}