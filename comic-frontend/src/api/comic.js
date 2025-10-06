// src/api/comic.js
import request from './request';

// 1. 获取所有分类（用于筛选下拉框）
export const getCategoryList = () => {
    return request({
        url: '/api/category',
        method: 'GET'
    });
};

// 2. 获取漫画列表（支持分页、关键词、分类筛选）
export const getComicList = (params) => {
    return request({
        url: '/api/comic/list',
        method: 'GET',
        params  // params包含page、size、keyword、categoryId
    });
};



// 3. 获取漫画详情（后续阅读页会用到）
export const getComicDetail = (comicId) => {
    return request({
        url: `/api/comic/detail/${comicId}`,
        method: 'GET'
    });
};