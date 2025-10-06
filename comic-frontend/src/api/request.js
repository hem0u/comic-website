import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useUserStore } from '../stores/userStore';

// 创建axios实例
const request = axios.create({
    baseURL: 'http://localhost:8080',  // 后端接口基础地址
    timeout: 5000  // 请求超时时间
});

// 请求拦截器：添加token到请求头
request.interceptors.request.use(
    (config) => {
        // 在这里调用 useUserStore
        const userStore = useUserStore();
        if (userStore.token) {
            config.headers.Authorization = `Bearer ${userStore.token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 响应拦截器：统一处理错误
request.interceptors.response.use(
    (response) => {
        // 后端返回格式：{ code: 200, msg: "成功", data: {} }
        const res = response.data;
        // 非200状态码视为错误（如401未登录、403无权限）
        if (res.code !== 200) {
            ElMessage.error(res.msg || '请求失败');
            // 401未登录：清除token并跳转登录页
            if (res.code === 401) {
                const userStore = useUserStore();
                userStore.clearToken();
                window.location.href = '/login';
            }
            return Promise.reject(res);
        }
        return res;
    },
    (error) => {
        ElMessage.error('网络错误，请稍后重试');
        return Promise.reject(error);
    }
);

export default request;