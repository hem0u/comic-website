import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '../stores/userStore'

// 路由规则
const routes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { requireAuth: false, hideNav: true }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'),
        meta: { requireAuth: false, hideNav: true }
    },
    {
        path: '/comic-list',
        name: 'ComicList',
        component: () => import('../views/ComicList.vue'),
        meta: { requireAuth: false }
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { requireAuth: false }
    },
    {
        path: '/comic-read/:id',
        name: 'ComicRead',
        component: () => import('../views/ComicRead.vue'),
        meta: { requireAuth: false }
    },
    {
        path: '/user-center',
        name: 'UserCenter',
        component: () => import('../views/UserCenter.vue'),
        meta: { requireAuth: true }
    },
    {
        path: '/search',
        name: 'Search',
        component: () => import('../views/SearchPage.vue'),
        meta: { requireAuth: false }
    }
];

// 创建路由实例
const router = createRouter({
    history: createWebHistory(),
    routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    if (to.meta.requireAuth && !userStore.token) {
        next('/login');
    } else {
        next();
    }
});

export default router;