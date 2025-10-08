import { createApp } from 'vue';
import { createPinia } from 'pinia';  // 引入Pinia
import ElementPlus from 'element-plus';  // 引入Element Plus
import 'element-plus/dist/index.css';  // 引入Element Plus样式
import 'font-awesome/css/font-awesome.min.css';  // 引入Font Awesome样式
import router from './router';  // 引入路由
import App from './App.vue';

const app = createApp(App);

// 安装插件
app.use(createPinia())
    .use(router)
    .use(ElementPlus)
    .mount('#app');