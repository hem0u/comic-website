<template>
  <div id="app">
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

    <!-- 主内容区域 -->
    <main class="main-outer" :class="{ 'has-sidebar': isSidebarOpen }">
      <div class="main-inner">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import TopNav from './components/TopNav.vue';
import SidebarNav from './components/SidebarNav.vue';

const isSidebarOpen = ref(false);
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f9f9f9;
  overflow-x: hidden;
}

#app {
  position: relative;
  min-height: 100vh;
}

/* 主内容区域样式 - 只影响页面主体，不影响顶部导航 */
.main-outer {
  width: 100%;
  padding-top: 64px;
  padding-left: 0;
  transition: padding-left 0.3s ease-in-out;
}

.main-outer.has-sidebar {
  padding-left: 280px; /* 只移动主内容区，不影响顶部导航 */
}

.main-inner {
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
  padding: 20px 15px;
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
    