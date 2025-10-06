import request from './request';

// 1. 获取用户收藏的漫画列表
export const getUserCollects = () => {
    return request({
        url: '/api/collect/user',
        method: 'GET'
    });
};

// 2. 取消收藏漫画
export const cancelCollect = (comicId) => {
    return request({
        url: `/api/collect/${comicId}`,
        method: 'DELETE'
    });
};

// 3. 检查是否已收藏（可选，后续阅读页可复用）
export const checkCollect = (comicId) => {
    return request({
        url: `/api/collect/check/${comicId}`,
        method: 'POST'
    });
};