package com.comic.mapper;

import com.comic.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    // 查询所有分类
    List<Category> selectAll();

    // 根据ID查询分类
    Category selectById(Integer id);
}