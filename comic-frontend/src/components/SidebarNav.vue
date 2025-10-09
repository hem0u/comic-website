<template>
  <div class="sidebar" :class="{ 'sidebar-open': isOpen }">
    <div class="sidebar-header">
      <div class="logo" style="display: flex; align-items: center;">
        <img src="../assets/vue.svg" alt="ComicPondLogo" style="width: 2rem; height: 2rem; margin-right: 0.5rem;">
        <span style="font-size: 1.5rem; font-weight: 700; color: #333;">ComicPond</span>
      </div>
      <button class="close-btn" @click="closeSidebar">
        <span class="close-symbol">×</span>
      </button>
    </div>
    <nav class="sidebar-nav">
      <ul>
        <li class="nav-item" :class="{ active: $route.path === '/home' }">
          <router-link to="/home">
            <span class="nav-icon">🏠</span>
            <span class="nav-text">首页</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/category' }">
          <router-link to="/category">
            <span class="nav-icon">📚</span>
            <span class="nav-text">全部分类</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/rank' }">
          <router-link to="/rank">
            <span class="nav-icon">🏆</span>
            <span class="nav-text">排行榜</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/update' }">
          <router-link to="/update">
            <span class="nav-icon">🔄</span>
            <span class="nav-text">最新更新</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/favorite' }">
          <router-link to="/favorite">
            <span class="nav-icon">❤️</span>
            <span class="nav-text">我的收藏</span>
          </router-link>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close']);

const closeSidebar = () => {
  emit('close');
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 260px; /* 适当缩小侧边栏宽度 */
  background-color: #fff; /* 白色背景 */
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transform: translateX(-100%);
  transition: transform 0.3s ease-in-out, background-color 0.3s ease, box-shadow 0.3s ease;
  z-index: 100;
  overflow-y: auto;
  /* 隐藏滚动条但保留滚动功能 */
  scrollbar-width: none; /* Firefox */
}

/* Chrome, Safari 和 Opera */
.sidebar::-webkit-scrollbar {
  display: none;
}

.sidebar-open {
  transform: translateX(0);
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #f0f0f0; /* 浅灰色边框 */
}

.sidebar-title {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: #333; /* 深灰色文字 */
}

.close-btn {
  background: none;
  border: none;
  color: #999; /* 中灰色图标 */
  cursor: pointer;
  font-size: 1.5rem;
  padding: 0.3rem;
  border-radius: 4px;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #ff7eb3; /* 粉色悬停效果 */
}

.sidebar-nav ul {
  list-style: none;
}

.nav-item {
  border-bottom: 1px solid #f5f5f5; /* 浅灰色边框 */
}

.nav-item a {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  color: #666; /* 中灰色文字 */
  text-decoration: none;
  transition: all 0.2s;
}

.nav-item a:hover {
  background-color: #f9f9f9; /* 浅灰色背景 */
  color: #ff7eb3; /* 粉色文字 */
}

.nav-item.active a {
  background-color: #f5f5f5; /* 浅灰色背景 */
  color: #ff7eb3; /* 粉色文字 - 当前页高亮 */
}

.nav-icon {
  margin-right: 1rem;
  font-size: 1.1rem;
}

@media (max-width: 768px) {
  .sidebar {
    width: 230px; /* 相应调整响应式设计下的宽度 */
  }
}

/* 暗色主题样式 - 增强视觉效果 */
html.el-theme-dark .sidebar {
  background-color: var(--el-bg-color) !important;
  color: var(--el-text-color-primary) !important;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.6) !important;
  border-right: 1px solid var(--el-border-color) !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .sidebar-header {
  border-bottom: 1px solid var(--el-border-color) !important;
  background-color: var(--el-bg-color-page) !important;
}

html.el-theme-dark .logo span {
  color: var(--el-text-color-primary) !important;
  font-weight: 700 !important;
}

html.el-theme-dark .close-btn {
  color: var(--el-text-color-secondary) !important;
  background-color: var(--el-bg-color-page) !important;
  border: none !important;
  transition: all 0.2s ease !important;
}

/* 确保close-symbol继承或覆盖父元素的颜色 */
html.el-theme-dark .close-btn .close-symbol {
  color: var(--el-text-color-secondary) !important;
  transition: color 0.2s ease !important;
}

html.el-theme-dark .close-btn:hover {
  color: #ff7eb3 !important;
  border: none !important;
  background-color: var(--el-bg-color-page) !important;
  transform: scale(1.05) !important;
}

/* 直接为悬停状态的close-symbol设置高优先级样式 */
html.el-theme-dark .close-btn:hover .close-symbol {
  color: #ff7eb3 !important;
  color: #ff7eb3 !important; /* 重复声明增强优先级 */
}

html.el-theme-dark .nav-item {
  border-bottom: 1px solid var(--el-border-color-light) !important;
}

html.el-theme-dark .nav-item a {
  color: var(--el-text-color-secondary) !important;
  background-color: transparent !important;
  transition: all 0.2s ease !important;
}

html.el-theme-dark .nav-item a:hover {
  color: var(--el-text-color-primary) !important;
  background-color: var(--el-bg-color-overlay) !important;
  /* 移除可能导致滚动条的transform，使用padding调整 */
  padding-left: calc(1.5rem + 5px) !important;
}

html.el-theme-dark .nav-item.active {
  background-color: var(--el-bg-color-overlay) !important;
  border-left: 4px solid var(--el-color-primary) !important;
}

html.el-theme-dark .nav-item.active a {
  background-color: transparent !important;
  color: var(--el-color-primary) !important;
}

html.el-theme-dark .nav-icon {
  color: var(--el-text-color-secondary) !important;
  font-size: 18px !important;
  margin-right: 12px !important;
  transition: all 0.2s ease !important;
}

html.el-theme-dark .nav-item a:hover .nav-icon {
  color: var(--el-text-color-primary) !important;
  transform: scale(1.1) !important;
}

html.el-theme-dark .nav-item.active .nav-icon {
  color: var(--el-color-primary) !important;
  transform: scale(1.1) !important;
}

html.el-theme-dark .nav-text {
  color: inherit !important;
}
</style>
