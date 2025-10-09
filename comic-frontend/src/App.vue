<template>
  <div id="app">
    <!-- 根据路由meta信息决定是否显示导航 -->
    <template v-if="!hideNavigation">
      <!-- 侧边导航栏 -->
      <SidebarNav
          :is-open="isSidebarOpen"
          @close="isSidebarOpen = false"
      />

      <!-- 顶部导航 -->
      <TopNav
          @open-menu="isSidebarOpen = true"
          :sidebar-open="isSidebarOpen"
      />

      <!-- 主内容区域 - 普通页面 -->
      <main class="main-outer" :class="{ 'has-sidebar': isSidebarOpen }">
        <div class="main-inner">
          <router-view />
        </div>
      </main>
    </template>

    <!-- 登录和注册页面使用全屏布局 -->
    <template v-else>
      <main class="main-outer auth-page">
        <div class="main-inner auth-inner">
          <router-view />
        </div>
      </main>
    </template>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import TopNav from './components/TopNav.vue';
import SidebarNav from './components/SidebarNav.vue';

const route = useRoute();
const isSidebarOpen = ref(false);

// 直接从路由meta信息获取是否隐藏导航
const hideNavigation = computed(() => {
  return route.meta.hideNav === true;
});
</script>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #fff; /* 白色背景 */
  color: #333; /* 深灰色文字 */
  overflow-x: hidden;
  transition: background-color 0.3s ease, color 0.3s ease;
}

#app {
  position: relative;
  min-height: 100vh;
}

/* 主内容区域样式 */
.main-outer {
  width: 100%;
  padding-top: 64px;
  padding-left: 0;
  transition: padding-left 0.3s ease-in-out, background-color 0.3s ease;
}

/* 登录页面不需要顶部内边距 */
.main-outer.auth-page {
  padding-top: 0;
}

.main-outer.has-sidebar {
  padding-left: 280px;
}

/* 扩大内容区域宽度，减少留白 */
.main-inner {
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
  padding: 20px 15px;
  transition: background-color 0.3s ease;
}

/* 确保顶部栏内容不随侧边栏移动 */
.top-nav-inner {
  position: relative;
  z-index: 90;
}

/* 登录和注册页面的特殊样式 */
.auth-page {
  padding-top: 0 !important;
  height: 100vh;
  background-color: #fff; /* 默认白色背景 */
}

/* 暗色主题下的登录/注册页面样式 - 必须在这里定义以确保优先级 */
html.el-theme-dark .auth-page {
  background-color: var(--el-bg-color-page) !important;
  color: var(--el-text-color-primary) !important;
}

/* 暗色主题下的卡片和表单容器 */
html.el-theme-dark .auth-page .el-card {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
}

/* 暗色主题下的输入框 - 整个输入框保持边框，可输入部分无边框 */
html.el-theme-dark .auth-page .el-input__wrapper {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important; /* 整个输入框保持边框 */
  color: var(--el-text-color-primary) !important;
  box-shadow: none !important; /* 去除阴影 */
}

/* 聚焦状态保持边框 */
html.el-theme-dark .auth-page .el-input__wrapper:focus-within {
  border-color: var(--el-color-primary) !important; /* 聚焦时边框变色 */
  box-shadow: none !important;
}

/* 悬停状态保持边框 */
html.el-theme-dark .auth-page .el-input__wrapper:hover {
  border-color: var(--el-color-primary) !important; /* 悬停时边框变色 */
}

/* 可输入部分（输入框内部）无边框 */
html.el-theme-dark .auth-page .el-input__inner {
  border: none !important; /* 移除可输入部分的边框 */
  outline: none !important; /* 移除轮廓 */
  background-color: transparent !important;
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .auth-page .el-input__inner {
  background-color: transparent !important;
  color: var(--el-text-color-primary) !important;
}

/* 暗色主题下的按钮 */
html.el-theme-dark .auth-page .el-button--default {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
}

.auth-inner {
  max-width: 100% !important;
  padding: 0 !important;
  margin: 0 !important;
  height: 100%;
}

/* 全局暗色主题样式 - 增强视觉效果和优先级 */
html.el-theme-dark {
  /* 直接在根元素上设置暗色主题变量 */
  --el-color-primary: #ff7eb3;
  --el-color-success: #67c23a;
  --el-color-warning: #e6a23c;
  --el-color-danger: #f56c6c;
  --el-color-error: #f56c6c;
  --el-color-info: #909399;
  
  /* 背景色 - 增加层次感 */
  --el-bg-color: #121212;
  --el-bg-color-page: #0a0a0a;
  --el-bg-color-overlay: #1e1e1e;
  --el-bg-color-soft: #252525;
  
  /* 文字颜色 - 增加对比度 */
  --el-text-color-primary: #ffffff !important;
  --el-text-color-regular: #e0e0e0;
  --el-text-color-secondary: #b0b0b0;
  --el-text-color-placeholder: #707070;
  --el-text-color-disabled: #505050;
  
  /* 边框颜色 */
  --el-border-color: #303030;
  --el-border-color-light: #252526;
  --el-border-color-lighter: #1e1e1e;
  --el-border-color-extra-light: #1a1a1a;
}

/* 暗色主题下的全局样式 - 确保高优先级 */
html.el-theme-dark body {
  background-color: var(--el-bg-color-page) !important;
  color: var(--el-text-color-primary) !important;
  transition: all 0.3s ease;
}

html.el-theme-dark #app {
  background-color: var(--el-bg-color-page) !important;
  color: var(--el-text-color-primary) !important;
  transition: all 0.3s ease;
}

/* 主内容区域样式 - 高优先级 */
html.el-theme-dark .main-outer {
  background-color: var(--el-bg-color-page) !important;
  box-shadow: none;
}

html.el-theme-dark .main-inner {
  background-color: var(--el-bg-color-page) !important;
  border-radius: 0;
  box-shadow: none;
  padding: 20px 15px;
  border: none;
}

/* 确保所有文本在暗色主题下都是亮色 */
html.el-theme-dark * {
  color: var(--el-text-color-primary) !important;
}

/* 特殊元素的颜色覆盖 */
html.el-theme-dark a {
  color: var(--el-color-primary) !important;
  transition: color 0.2s ease;
}

html.el-theme-dark a:hover {
  color: #ff5a9d !important;
}

html.el-theme-dark button {
  background-color: var(--el-bg-color-overlay) !important;
  color: var(--el-text-color-primary) !important;
  border-color: var(--el-border-color) !important;
}

html.el-theme-dark input, 
html.el-theme-dark textarea, 
html.el-theme-dark select {
  background-color: var(--el-bg-color-overlay) !important;
  border: 1px solid var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark h1, 
html.el-theme-dark h2, 
html.el-theme-dark h3, 
html.el-theme-dark h4, 
html.el-theme-dark h5, 
html.el-theme-dark h6 {
  color: var(--el-text-color-primary) !important;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* 表格样式 */
html.el-theme-dark table {
  border-collapse: separate;
  border-spacing: 0;
}

html.el-theme-dark th {
  background-color: var(--el-bg-color-overlay) !important;
  border-bottom: 2px solid var(--el-border-color) !important;
}

html.el-theme-dark td {
  border-bottom: 1px solid var(--el-border-color-light) !important;
}

html.el-theme-dark tr:hover {
  background-color: var(--el-bg-color-overlay) !important;
}

/* 响应式调整 */
@media (max-width: 1440px) {
  .main-inner {
    max-width: calc(100% - 30px);
  }
}

@media (max-width: 768px) {
  .main-outer.has-sidebar {
    padding-left: 250px;
  }

  .main-inner {
    padding: 15px 10px;
  }
}
</style>
    