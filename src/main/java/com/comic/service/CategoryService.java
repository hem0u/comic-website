package com.comic.service;

import com.comic.entity.Category;
import com.comic.vo.ResultVO;

import java.util.List;

public interface CategoryService {
    // 获取所有分类
    ResultVO<List<Category>> getAllCategories();
}