package com.comic.mapper;

import com.comic.entity.Comic;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ComicMapper {
    // 新增漫画
    int insert(Comic comic);

    // 根据ID查询漫画
    Comic selectById(Long id);

    // 根据作者ID查询漫画列表
    List<Comic> selectByAuthorId(Long authorId);

    // 根据分类ID查询漫画列表
    List<Comic> selectByCategoryId(Integer categoryId);
}