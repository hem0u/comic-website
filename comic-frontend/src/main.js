import { createApp } from 'vue';
import { createPinia } from 'pinia';  // 引入Pinia
import ElementPlus from 'element-plus';  // 引入Element Plus
import 'element-plus/dist/index.css';  // 引入Element Plus样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue';  // 引入Element Plus图标
import router from './router';  // 引入路由
import App from './App.vue';

const app = createApp(App);

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

// 安装插件
app.use(createPinia())
    .use(router)
    .use(ElementPlus)
    .mount('#app');