package com.comic.service.impl;

import com.comic.entity.Category;
import com.comic.mapper.CategoryMapper;
import com.comic.service.CategoryService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResultVO<List<Category>> getAllCategories() {
        List<Category> categories = categoryMapper.selectAll();
        return ResultVO.success(categories);
    }
}