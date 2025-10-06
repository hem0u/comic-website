import { defineStore } from 'pinia';

// 定义用户状态存储
export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',  // 从本地存储获取token
        username: localStorage.getItem('username') || ''  // 用户名
    }),
    actions: {
        // 保存用户信息到状态和本地存储
        setUserInfo(token, username) {
            this.token = token;
            this.username = username;
            localStorage.setItem('token', token);
            localStorage.setItem('username', username);
        },
        // 清除用户信息（退出登录）
        clearToken() {
            this.token = '';
            this.username = '';
            localStorage.removeItem('token');
            localStorage.removeItem('username');
        }
    }
});