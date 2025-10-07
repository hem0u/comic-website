import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../stores/userStore'

// 路由规则
const routes = [
    {
        path: '/',
        redirect: '/home'  // 根路径重定向到首页
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { requireAuth: false, hideNav: true }  // 不需要登录，隐藏导航
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'),
        meta: { requireAuth: false, hideNav: true }  // 隐藏导航
    },
    {
        path: '/comic-list',
        name: 'ComicList',
        component: () => import('../views/ComicList.vue'),
        meta: { requireAuth: false }  // 漫画列表不需要登录
    },
    // 在现有路由规则中添加
    {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { requireAuth: false }  // 首页不需要登录
    },
    {
        path: '/comic-read/:id',
        name: 'ComicRead',
        component: () => import('../views/ComicRead.vue'),
        meta: { requireAuth: false }  // 阅读页不需要登录
    },
    {
        path: '/user-center',
        name: 'UserCenter',
        component: () => import('../views/UserCenter.vue'),
        meta: { requireAuth: true }  // 个人中心需要登录
    }
];

// 创建路由实例
const router = createRouter({
    history: createWebHistory(),  // 使用HTML5历史模式（无#）
    routes
});

// 路由守卫：验证需要登录的页面
router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    // 需要登录且无token：跳转登录页
    if (to.meta.requireAuth && !userStore.token) {
        next('/login');
    } else {
        next();
    }
});

export default router;