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
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}


body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #fff; /* 白色背景，与style.css保持一致 */
  color: #333; /* 深灰色文字 */
  overflow-x: hidden;
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
  transition: padding-left 0.3s ease-in-out;
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
  background-color: #fff;
}

.auth-inner {
  max-width: 100% !important;
  padding: 0 !important;
  margin: 0 !important;
  height: 100%;
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
    