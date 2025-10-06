// src/api/history.js
import request from './request';

// 记录阅读历史（漫画ID + 章节ID）
export const recordReadHistory = (comicId, chapterId) => {
    return request({
        url: '/api/history',
        method: 'POST',
        params: {
            comicId,
            chapterId
        }
    });
};

// 获取用户阅读历史列表（后续个人中心会用到）
export const getUserReadHistory = () => {
    return request({
        url: '/api/history/user',
        method: 'GET'
    });
};