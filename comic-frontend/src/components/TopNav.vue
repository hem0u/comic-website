<template>
  <header class="top-nav">
    <!-- 顶部导航导航内容容器 -->
    <div class="top-nav-inner">
      <!-- 菜单按钮：侧边栏打开时隐藏 -->
      <button
          class="menu-toggle"
          @click="onMenuClick"
          :class="{ 'hidden': sidebarOpen }"
      >
        <span class="menu-symbol">☰</span>
      </button>

      <!-- 网站标识：侧边栏打开时隐藏 -->
      <div class="logo" :class="{ 'hidden': sidebarOpen }">
        <img src="../assets/vue.svg" alt="漫画世界logo" class="logo-img">
        <span class="logo-text">漫画世界</span>
      </div>

      <!-- 右侧功能区：包含搜索框和用户图标 -->
      <div class="right-actions">
        <!-- 搜索框 -->
        <div class="search-container">
          <input
              type="text"
              placeholder="搜索漫画..."
              class="search-input"
              @keydown.enter="handleSearch"
          >
          <button class="search-btn" @click="handleSearch">
            <i class="fa fa-search"></i>
          </button>
        </div>

        <!-- 用户图标 -->
        <div class="user-icon" @click="handleUserClick">
          <i class="fa fa-user-circle"></i>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { defineEmits, defineProps } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  sidebarOpen: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['open-menu']);

const onMenuClick = () => {
  emit('open-menu');
};

const handleSearch = () => {
  ElMessage.info('搜索功能即将上线');
};

const handleUserClick = () => {
  ElMessage.info('用户中心即将上线');
};
</script>

<style scoped>
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 90;
}

.top-nav-inner {
  max-width: 1400px;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  padding: 0 15px;
  display: flex;
  align-items: center;
  justify-content: space-between; /* 主轴两端对齐 */
}

/* 右侧功能区：包含搜索框和用户图标，确保始终靠右 */
.right-actions {
  display: flex;
  align-items: center;
  margin-left: auto; /* 自动推到右侧 */
}

/* 菜单按钮 */
.menu-toggle {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #333;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 4px;
  transition: background-color 0.2s;
  margin-right: 10px;
}

.menu-toggle:hover {
  background-color: #f5f5f5;
}

/* 网站标识 */
.logo {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 2rem;
  height: 2rem;
  margin-right: 0.5rem;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  color: #409eff;
}

/* 搜索框 */
.search-container {
  display: flex;
  align-items: center;
  max-width: 500px;
  margin-right: 20px; /* 与用户图标保持距离 */
}

.search-input {
  width: 200px; /* 固定搜索框宽度 */
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 4px 0 0 4px;
  outline: none;
  font-size: 14px;
}

.search-btn {
  background-color: #409eff;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-btn:hover {
  background-color: #3088e6;
}

/* 用户图标 */
.user-icon {
  font-size: 1.8rem;
  cursor: pointer;
  color: #333;
  transition: color 0.2s;
}

.user-icon:hover {
  color: #409eff;
}

.hidden {
  display: none !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .logo-text {
    font-size: 1.2rem;
  }

  .search-container {
    max-width: 300px;
  }

  .search-input {
    width: 150px;
  }
}

@media (max-width: 480px) {
  .search-input {
    width: 120px;
    padding: 0.5rem;
    font-size: 12px;
  }
}
</style>
