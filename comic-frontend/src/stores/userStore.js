import { defineStore } from 'pinia';

// 定义用户状态存储
export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',  // 从本地存储获取token
        username: localStorage.getItem('username') || '',  // 用户名
        avatar: localStorage.getItem('avatar') || ''  // 用户头像
    }),
    actions: {
        // 保存用户信息到状态和本地存储
        setUserInfo(token, username, avatar = '') {
            this.token = token;
            this.username = username;
            this.avatar = avatar;
            localStorage.setItem('token', token);
            localStorage.setItem('username', username);
            if (avatar) {
                localStorage.setItem('avatar', avatar);
            } else {
                localStorage.removeItem('avatar');
            }
        },
        // 清除用户信息（退出登录）
        clearToken() {
            this.token = '';
            this.username = '';
            this.avatar = '';
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            localStorage.removeItem('avatar');
        }
    }
});