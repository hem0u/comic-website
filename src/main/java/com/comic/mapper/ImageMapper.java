package com.comic.mapper;

import com.comic.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ImageMapper {
    // 新增图片记录
    int insert(Image image);

    // 根据章节ID查询图片列表（按顺序）
    List<Image> selectByChapterId(Long chapterId);
}