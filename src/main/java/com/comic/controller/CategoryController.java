package com.comic.controller;

import com.comic.service.CategoryService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    // 获取所有分类：GET /api/category
    @GetMapping
    public ResultVO getAllCategories() {
        return categoryService.getAllCategories();
    }
}