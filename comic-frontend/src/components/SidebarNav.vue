<template>
  <div class="sidebar" :class="{ 'sidebar-open': isOpen }">
    <div class="sidebar-header">
      <h2 class="sidebar-title">漫画导航</h2>
      <button class="close-btn" @click="closeSidebar">
        <span class="close-symbol">×</span>
      </button>
    </div>
    <nav class="sidebar-nav">
      <ul>
        <li class="nav-item" :class="{ active: $route.path === '/home' }">
          <router-link to="/home" @click="closeSidebar">
            <span class="nav-icon">🏠</span>
            <span class="nav-text">首页</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/category' }">
          <router-link to="/category" @click="closeSidebar">
            <span class="nav-icon">📚</span>
            <span class="nav-text">全部分类</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/rank' }">
          <router-link to="/rank" @click="closeSidebar">
            <span class="nav-icon">🏆</span>
            <span class="nav-text">排行榜</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/update' }">
          <router-link to="/update" @click="closeSidebar">
            <span class="nav-icon">🔄</span>
            <span class="nav-text">最新更新</span>
          </router-link>
        </li>
        <li class="nav-item" :class="{ active: $route.path === '/favorite' }">
          <router-link to="/favorite" @click="closeSidebar">
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
  width: 280px;
  background-color: #fff; /* 白色背景 */
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transform: translateX(-100%);
  transition: transform 0.3s ease-in-out;
  z-index: 100;
  overflow-y: auto;
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
    width: 250px;
  }
}
</style>
