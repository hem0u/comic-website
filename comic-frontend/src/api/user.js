import request from './request';

// 登录
export const login = (data) => {
    return request({
        url: '/api/user/login',
        method: 'POST',
        data
    });
};

// 注册
export const register = (data) => {
    return request({
        url: '/api/user/register',
        method: 'POST',
        data
    });
};

// 新增：获取当前登录用户信息（个人中心用）
export const getUserInfo = () => {
    return request({
        url: '/api/user/info',
        method: 'GET'
    });
};

// 更新用户信息（支持昵称、邮箱、头像、个人简介）
export const updateUserInfo = (data) => {
    return request({
        url: '/api/user/info',
        method: 'PUT',
        data
    });
};
