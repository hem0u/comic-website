package com.comic.mapper;

import com.comic.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CollectMapper {
    // 新增收藏
    int insert(Collect collect);

    // 删除收藏（根据用户和漫画ID）
    int deleteByUserAndComic(Long userId, Long comicId);

    // 检查用户是否已收藏漫画
    Integer checkCollect(Long userId, Long comicId);

    // 查询用户收藏的漫画列表
    List<Collect> selectByUserId(Long userId);
}