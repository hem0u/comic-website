import request from './request';

// 1. 记录阅读历史（阅读页用）
export const recordReadHistory = (comicId, chapterId) => {
    return request({
        url: '/api/history',
        method: 'POST',
        params: { comicId, chapterId }
    });
};

// 2. 获取用户阅读历史列表（个人中心用）
export const getUserReadHistory = () => {
    return request({
        url: '/api/history/user',
        method: 'GET'
    });
};