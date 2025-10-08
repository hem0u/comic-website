<template>
  <header class="top-nav">
    <div class="top-nav-inner">
      <!-- 菜单按钮 -->
      <button
          class="menu-toggle"
          @click="onMenuClick"
          :class="{ 'hidden': sidebarOpen }"
      >
        <span class="menu-symbol">☰</span>
      </button>

      <!-- 网站标识 -->
      <div class="logo" :class="{ 'hidden': sidebarOpen }">
        <img src="../assets/vue.svg" alt="ComicPondLogo" class="logo-img">
        <span class="logo-text">ComicPond</span>
      </div>

      <!-- 右侧功能区：搜索框 + 用户图标 -->
      <div class="right-actions">
        <!-- 搜索框 -->
        <div class="search-container">
          <input
              type="text"
              placeholder="搜索漫画..."
              class="search-input"
              ref="searchInput"
              @keydown.enter="handleSearch"
          >
          <i class="fa fa-search search-icon" @click="handleSearch"></i>
        </div>

        <!-- 用户图标 -->
        <div class="user-icon-wrapper" ref="userIconWrapper">
          <div class="user-icon" @click="toggleUserMenu">
            <i class="fa fa-user-circle"></i>
          </div>
        </div>
      </div>
    </div>
  </header>
  
  <!-- 遮罩层 -->
  <div v-if="isUserMenuOpen" class="user-menu-overlay" @click="closeUserMenu"></div>
  
  <!-- 用户菜单弹窗 -->
  <div 
    v-if="isUserMenuOpen" 
    class="user-menu"
    :style="menuPositionStyle"
  >
    <div class="menu-header">
      <div class="avatar">
        <i class="fa fa-user-circle"></i>
      </div>
      <div class="user-info">
        <div class="username">LuvinU01</div>
        <div class="user-role">User</div>
      </div>
    </div>
    <div class="menu-divider"></div>
    <div class="menu-items">
        <div class="menu-item" @click="goToUserCenter">
          <i class="fa fa-user"></i>
          <span>我的资料</span>
        </div>
      <div class="menu-item">
        <i class="fa fa-bookmark"></i>
        <span>我的关注</span>
      </div>
      <div class="menu-item">
        <i class="fa fa-list"></i>
        <span>我的列表</span>
      </div>
      <div class="menu-item">
        <i class="fa fa-users"></i>
        <span>我的群组</span>
      </div>
      <div class="menu-item">
        <i class="fa fa-bell"></i>
        <span>我的报告</span>
      </div>
      <div class="menu-item">
        <i class="fa fa-bullhorn"></i>
        <span>公告</span>
      </div>
      <div class="menu-divider"></div>
      <div class="menu-item">
        <i class="fa fa-cog"></i>
        <span>设置</span>
      </div>
      <div class="menu-item" @click="handleLogout">
        <i class="fa fa-sign-out"></i>
        <span>退出登录</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits, defineProps, ref, onMounted, onUnmounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
  sidebarOpen: {
    type: Boolean,
    default: false
  }
});

// 搜索框引用
const searchInput = ref(null);

const emit = defineEmits(['open-menu']);

// 用户菜单状态
const isUserMenuOpen = ref(false);
const userIconWrapper = ref(null);

// 计算菜单位置
const menuPositionStyle = computed(() => {
  if (!userIconWrapper.value || !isUserMenuOpen.value) {
    return {};
  }
  
  const rect = userIconWrapper.value.getBoundingClientRect();
  return {
    position: 'fixed',
    top: `${rect.bottom}px`,
    right: `${window.innerWidth - rect.right}px`,
    zIndex: '1000'
  };
});

const onMenuClick = () => {
  emit('open-menu');
};

const handleSearch = () => {
  if (!searchInput.value) return;
  const keyword = searchInput.value.value.trim();
  // 无论关键词是否为空，都跳转到搜索页面
  router.push({ path: '/search', query: { keyword: encodeURIComponent(keyword) } });
  // 清空搜索框
  searchInput.value.value = '';
};

// 跳转到个人中心
const goToUserCenter = () => {
  closeUserMenu();
  router.push('/user-center');
};

// 切换用户菜单显示状态
const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value;
  updateBodyScroll();
};

// 关闭用户菜单
const closeUserMenu = () => {
  isUserMenuOpen.value = false;
  updateBodyScroll();
};

// 更新body滚动状态
const updateBodyScroll = () => {
  if (isUserMenuOpen.value) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = 'auto';
  }
};

// 退出登录处理函数
const handleLogout = () => {
  // 显示确认对话框
  if (confirm('确定要退出登录吗？')) {
    // 清除用户登录状态（实际应用中应该清除localStorage中的token等）
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    
    // 关闭用户菜单
    closeUserMenu();
    
    // 显示退出成功消息
    ElMessage.success('退出登录成功');
    
    // 重定向到登录页面（这里假设登录页面路径为/login）
    setTimeout(() => {
      router.push('/login');
    }, 1000);
  }
};

// 组件卸载时恢复滚动
onUnmounted(() => {
  document.body.style.overflow = 'auto';
});
</script>

<style scoped>
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background-color: #fff; /* 白色背景 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 88;
}

.top-nav-inner {
  max-width: 1400px;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  padding: 0 15px;
  display: flex;
  align-items: center;
}

/* 右侧功能区：确保搜索框和用户图标始终靠右 */
.right-actions {
  display: flex;
  align-items: center;
  margin-left: auto;
}

/* 菜单按钮 */
.menu-toggle {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #333; /* 深灰色图标 */
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 4px;
  transition: all 0.2s;
  margin-right: 10px;
}

.menu-toggle:hover {
  background-color: #f5f5f5; /* 浅灰色背景 */
  color: #ff7eb3; /* 粉色强调 */
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
  color: #333; /* 深灰色文字 */
}

/* 搜索框 */
.search-container {
  position: relative;
  max-width: 500px;
  margin-right: 20px;
  transition: border 0.2s;
}

.search-container:focus-within {
  border: 1px solid #ff7eb3;
  border-radius: 4px;
  padding: 2px;
}

.search-input {
  width: 280px;
  padding: 0.5rem 2.5rem 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  outline: none;
  font-size: 14px;
  background-color: #e9e9e9; /* 更深的灰色背景 */
  color: #333; /* 深灰色文字 */
  transition: all 0.2s;
}

.search-input:focus {
  background-color: #fff;
}

.search-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  cursor: pointer;
  transition: color 0.2s;
  z-index: 1;
}

.search-icon:hover {
  color: #ff7eb3; /* 粉色悬停效果 */
}

/* 用户图标 */
.user-icon-wrapper {
  position: relative;
  margin-left: 1rem;
}

.user-icon {
  font-size: 1.8rem;
  cursor: pointer;
  color: #333; /* 深灰色图标 */
  transition: color 0.2s;
  padding: 0.5rem;
}

.user-icon:hover {
  color: #ff7eb3; /* 粉色悬停效果 */
}

/* 用户菜单 */
.user-menu {
  width: 220px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  pointer-events: auto;
}

/* 菜单头部 */
.menu-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #f9f9f9;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #ff7eb3;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.5rem;
  margin-right: 12px;
}

.user-info {
  flex: 1;
}

.username {
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.user-role {
  font-size: 12px;
  color: #666;
}

/* 菜单项 */
.menu-items {
  /* 移除最大高度限制，避免滚动条出现 */
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  color: #333;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.menu-item i {
  width: 20px;
  margin-right: 12px;
  text-align: center;
  color: #666;
}

.menu-divider {
  height: 1px;
  background-color: #eee;
  margin: 0;
}

/* 遮罩层 */
.user-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 110; /* 大于侧边栏的z-index(100)，确保能覆盖侧边栏 */
  background-color: rgba(0, 0, 0, 0.5); /* 半透明黑色遮罩 */
  backdrop-filter: blur(2px); /* 背景模糊效果 */
  cursor: pointer;
  pointer-events: auto;
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
    width: 200px;
  }
}

@media (max-width: 480px) {
  .search-input {
    width: 140px;
    padding: 0.5rem;
    font-size: 12px;
  }
}
</style>
