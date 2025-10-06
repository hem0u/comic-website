// src/api/user.js
import request from './request';

// 登录接口
export const login = (data) => {
    return request({
        url: '/api/user/login',
        method: 'POST',
        data
    });
};

// 注册接口（完善参数，匹配后端DTO）
export const register = (data) => {
    return request({
        url: '/api/user/register',
        method: 'POST',
        data
    });
};