<template>
  <div class="user-center-container">
    <!-- 用户信息横幅 -->
    <div class="user-header-banner">
      <div class="banner-content">
        <div class="user-info">
          <div class="avatar-container">
            <div class="avatar-large">
              <img :src="getAvatarUrl(userInfo?.avatar) || 'https://picsum.photos/200'" alt="用户头像" />
            </div>
          </div>
          <div class="user-details">
            <h2 class="user-name">{{ userInfo?.nickname || '未设置昵称' }}</h2>
            <div class="user-meta-info">
              <span class="username-tag">{{ userInfo?.username || '' }}</span>
              <span class="user-badge">User</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="main-content">
      <!-- 标签页：首页 + 收藏 + 历史 -->
      <el-tabs v-model="activeTab" class="content-tabs">
        <!-- 首页 -->
        <el-tab-pane label="首页" name="home">
          <div class="home-section">
            <!-- 用户统计卡片 -->
            <div class="stats-cards">
              <el-card class="stat-card" shadow="hover">
                <div class="stat-content">
                  <div class="stat-icon collect-icon">
                    <i class="fa fa-star"></i>
                  </div>
                  <div class="stat-info">
                    <p class="stat-label">收藏漫画</p>
                    <p class="stat-value">{{ collectList.length }}</p>
                  </div>
                </div>
              </el-card>
              
              <el-card class="stat-card" shadow="hover">
                <div class="stat-content">
                  <div class="stat-icon history-icon">
                    <i class="fa fa-clock-o"></i>
                  </div>
                  <div class="stat-info">
                    <p class="stat-label">阅读历史</p>
                    <p class="stat-value">{{ historyList.length }}</p>
                  </div>
                </div>
              </el-card>
            </div>

            <!-- 最近活动 -->
            <div class="recent-activity">
              <h3 class="section-title">最近活动</h3>
              <el-empty
                  v-if="collectList.length === 0 && historyList.length === 0"
                  description="暂无活动记录"
                  style="margin: 40px 0;"
              ></el-empty>
              
              <!-- 最近收藏 -->
              <div v-if="collectList.length > 0" class="recent-collect">
                <h4 class="subsection-title">最近收藏</h4>
                <div class="recent-comic-grid">
                  <div v-for="comic in recentCollects" :key="comic.id" class="comic-item" @click="goToRead(comic.id)">
                    <div class="comic-cover-wrapper">
                      <img :src="comic.cover || 'https://picsum.photos/100/150'" alt="漫画封面" />
                    </div>
                    <span class="comic-title">{{ comic.title || '未知标题' }}</span>
                  </div>
                </div>
              </div>

              <!-- 最近阅读 -->
              <div v-if="historyList.length > 0" class="recent-read">
                <h4 class="subsection-title">最近阅读</h4>
                <div class="history-list">
                  <el-card
                      v-for="item in recentHistory"
                      :key="item.historyId"
                      class="history-item"
                      shadow="hover"
                  >
                    <div class="history-item-content">
                      <div class="history-cover">
                        <img :src="item.cover || 'https://picsum.photos/100/150'" alt="漫画封面" />
                      </div>
                      <div class="history-info">
                        <h5 class="comic-title">{{ item.comicTitle || '未知漫画' }}</h5>
                        <p class="chapter-name">最近阅读：{{ item.chapterTitle || '未选择章节' }}</p>
                        <p class="read-time">阅读时间：{{ formatTime(item.readTime) }}</p>
                      </div>
                      <div class="history-actions">
                        <el-button
                            type="text"
                            size="small"
                            @click.stop="goToRead(item.comicId)"
                        >
                          继续阅读
                        </el-button>
                      </div>
                    </div>
                  </el-card>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 我的收藏 -->
        <el-tab-pane label="我的收藏" name="collect">
          <div class="comic-collect-list">
            <el-loading v-if="isCollectLoading" fullscreen text="加载收藏中..."></el-loading>

            <!-- 空状态 -->
            <el-empty
                v-else-if="collectList.length === 0"
                description="暂无收藏漫画，去发现喜欢的漫画吧～"
                style="margin: 60px 0;"
            ></el-empty>

            <!-- 收藏列表 -->
              <div v-else class="comic-grid">
                <el-card
                    v-for="comic in collectList"
                    :key="comic.id"
                    class="comic-card"
                    shadow="hover">
                <div class="comic-card-content">
                  <div class="comic-cover" @click="goToRead(comic.id)">
                    <img :src="comic.cover || 'https://picsum.photos/300/400'" alt="漫画封面" />
                  </div>
                  <div class="comic-info">
                    <h3 class="comic-title" @click="goToRead(comic.id)">{{ comic.title || '未知标题' }}</h3>
                    <p class="comic-author">作者：{{ comic.authorName || '未知作者' }}</p>
                    <p class="comic-category">分类：{{ comic.categoryName || '未分类' }}</p>
                  </div>
                  <div class="card-actions">
                    <el-button
                        type="text"
                        color="#ff4d4f"
                        @click.stop="handleCancelCollect(comic.id)"
                    >
                      取消收藏
                    </el-button>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-tab-pane>

        <!-- 阅读历史 -->
        <el-tab-pane label="阅读历史" name="history">
          <div class="read-history-list">
            <el-loading v-if="isHistoryLoading" fullscreen text="加载历史中..."></el-loading>

            <!-- 空状态 -->
            <el-empty
                v-else-if="historyList.length === 0"
                description="暂无阅读记录，去阅读漫画吧～"
                style="margin: 60px 0;"
            ></el-empty>

            <!-- 历史列表 -->
            <div v-else class="history-list">
              <el-card
                  v-for="item in historyList"
                  :key="item.historyId"
                  class="history-item"
                  shadow="hover"
              >
                <div class="history-item-content">
                  <div class="history-cover">
                    <img :src="item.cover || 'https://picsum.photos/100/150'" alt="漫画封面" />
                  </div>
                  <div class="history-info">
                    <h5 class="comic-title">{{ item.comicTitle || '未知漫画' }}</h5>
                    <p class="chapter-name">最近阅读：{{ item.chapterTitle || '未选择章节' }}</p>
                    <p class="read-time">阅读时间：{{ formatTime(item.readTime) }}</p>
                  </div>
                  <div class="history-actions">
                    <el-button
                        type="text"
                        size="small"
                        @click.stop="goToRead(item.comicId)"
                    >
                      继续阅读
                    </el-button>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 底部操作按钮 -->
      <div class="bottom-actions">
        <el-button
              type="danger"
              @click="handleLogout"
              class="logout-button"
        >
          <i class="fa fa-sign-out"></i>
          退出登录
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElLoading, ElEmpty } from 'element-plus';
import { Camera } from '@element-plus/icons-vue';
import { useUserStore } from '../stores/userStore';
import { getUserInfo } from '../api/user';
import { getUserCollects, cancelCollect } from '../api/collect';
import { getUserReadHistory } from '../api/history';

// 路由和状态实例
const router = useRouter();
const userStore = useUserStore();

// 页面状态
const activeTab = ref('home');
const userInfo = ref({});
const collectList = ref([]);
const isCollectLoading = ref(false);
const historyList = ref([]);
const isHistoryLoading = ref(false);

// 计算属性：最近收藏的5个漫画
const recentCollects = computed(() => {
  return collectList.value.slice(0, 5);
});

// 计算属性：最近阅读的5条记录
const recentHistory = computed(() => {
  return historyList.value.slice(0, 5);
});

// 退出登录
const handleLogout = () => {
  userStore.clearToken();
  localStorage.removeItem('token');
  router.push('/login');
  ElMessage.success('已成功退出登录');
};

// 处理头像路径，确保正确显示
const getAvatarUrl = (avatarPath) => {
  // 如果没有头像，返回默认头像
  if (!avatarPath) return 'https://picsum.photos/200';
  
  // 如果是绝对路径或者已经是完整的URL，则直接返回
  if (avatarPath.startsWith('http://') || avatarPath.startsWith('https://')) {
    return avatarPath;
  }
  
  // 处理相对路径
  // 将各种相对路径格式转换为正确的资源路径
  if (avatarPath.startsWith('../assets/') || avatarPath.startsWith('./assets/')) {
    // 提取相对路径中的文件名部分，然后使用 import.meta.url 构建正确的路径
    const assetName = avatarPath.replace(/^\.\.?\/assets\//, '');
    return new URL(`../assets/${assetName}`, import.meta.url).href;
  }
  
  // 处理上传路径 /upload/ 开头的图片
  if (avatarPath.startsWith('/upload/')) {
    // 拼接基础URL，确保能正确访问后端上传的图片
    let fullPath = `http://localhost:8080${avatarPath}`;
    // 如果路径没有文件扩展名，添加.jpg（常见的头像格式）
    if (!avatarPath.includes('.')) {
      fullPath += '.jpg';
    }
    return fullPath;
  }
  
  // 其他情况，返回默认头像
  return 'https://picsum.photos/200';
};

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const res = await getUserInfo();
    if (res?.code === 200 && res.data) {
      userInfo.value = res.data;
    } else {
      ElMessage.error('用户信息加载失败');
    }
  } catch (error) {
    ElMessage.error('网络错误，请重试');
    console.error('用户信息接口错误:', error);
  }
};

// 加载收藏列表 - 修复版
const loadCollectList = async () => {
  isCollectLoading.value = true;
  try {
    console.log('开始加载收藏列表...');
    const res = await getUserCollects();
    console.log('收藏列表原始数据:', res);

    if (res?.code === 200) {
      // 直接使用返回的漫画列表数据
      collectList.value = Array.isArray(res.data) ? res.data : [];
      console.log('处理后的收藏列表:', collectList.value);

      if (collectList.value.length === 0) {
        ElMessage.info('暂无收藏漫画');
      }
    } else {
      collectList.value = [];
      ElMessage.error(res?.msg || '加载收藏失败');
    }
  } catch (error) {
    collectList.value = [];
    ElMessage.error('加载收藏列表失败');
    console.error('收藏接口错误:', error);
  } finally {
    isCollectLoading.value = false;
  }
};

// 加载阅读历史 - 修复版
const loadHistoryList = async () => {
  isHistoryLoading.value = true;
  try {
    console.log('开始加载阅读历史...');
    const res = await getUserReadHistory();
    console.log('历史列表原始数据:', res);

    if (res?.code === 200) {
      // 直接使用返回的历史数据
      historyList.value = Array.isArray(res.data) ? res.data : [];
      console.log('处理后的历史列表:', historyList.value);

      if (historyList.value.length === 0) {
        ElMessage.info('暂无阅读历史');
      }
    } else {
      historyList.value = [];
      ElMessage.error(res?.msg || '加载历史失败');
    }
  } catch (error) {
    historyList.value = [];
    ElMessage.error('加载阅读历史失败');
    console.error('历史接口错误:', error);
  } finally {
    isHistoryLoading.value = false;
  }
};

// 取消收藏
const handleCancelCollect = async (comicId) => {
  if (!comicId) {
    ElMessage.error('无效的漫画ID');
    return;
  }
  try {
    const res = await cancelCollect(comicId);
    if (res?.code === 200) {
      ElMessage.success('取消收藏成功');
      loadCollectList(); // 刷新收藏列表
    } else {
      ElMessage.error(res?.msg || '取消收藏失败');
    }
  } catch (error) {
    ElMessage.error('操作失败，请检查网络');
    console.error('取消收藏错误:', error);
  }
};

// 跳转到阅读页
const goToRead = (comicId) => {
  if (!comicId) {
    ElMessage.error('无效的漫画ID');
    return;
  }
  router.push(`/comic-read/${comicId}`);
};

// 时间格式化
const formatTime = (timeStr) => {
  if (!timeStr) return '未知时间';
  return timeStr.replace('T', ' ').split('.')[0];
};

// 页面挂载时初始化
onMounted(() => {
  if (!userStore.token) {
    router.push('/login');
    ElMessage.warning('请先登录');
    return;
  }

  // 并行加载所有数据
  Promise.all([
    loadUserInfo(),
    loadCollectList(),
    loadHistoryList()
  ]).catch(err => {
    ElMessage.error('数据加载异常，请重试');
    console.error('初始化数据错误:', err);
  });
});
</script>

<style scoped>
/* 全局样式 */
.user-center-container {
  max-width: 100%;
  margin: 0 auto;
  background-color: #fff;
  min-height: 100vh;
  width: 100%;
}

/* 暗色主题下背景与外部一致 */
html.el-theme-dark .user-center-container {
  background-color: var(--el-bg-color-page) !important;
}

/* 用户信息横幅 */
.user-header-banner {
  position: relative;
  background-image: url('https://picsum.photos/1200/500');
  background-size: cover;
  background-position: center;
  padding: 20px 30px 0;
  color: #fff;
  margin-bottom: 30px;
  border-radius: 0;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.2);
  height: 350px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 0;
}

.user-header-banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.2) 0%, rgba(0,0,0,0.7) 100%);
  z-index: 1;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  margin-bottom: 0;
}

.user-info {
    position: relative;
    z-index: 3;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    text-align: center;
    margin: 0;
    padding-bottom: 20px;
    transform: translateY(160px);
  }

.avatar-container {
  position: relative;
  flex-shrink: 0;
}

.avatar-large {
    width: 160px;
    height: 160px;
    border-radius: 0;
    overflow: hidden;
    border: none;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
    cursor: pointer;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }

.avatar-large:hover {
    transform: scale(1.05);
    box-shadow: 0 15px 40px rgba(0, 0, 0, 0.4);
  }

  .avatar-large img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

.user-name {
  font-size: 28px;
  margin: 0 0 10px 0;
  font-weight: bold;
  color: white;
}

.user-meta-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username-tag {
  background-color: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 14px;
}

.user-badge {
  background-color: #ff6b6b;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: bold;
}

/* 主内容区域 */
.main-content {
  max-width: 100%;
  margin: 150px auto 0;
  padding: 0 20px;
}

.content-tabs {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: none;
}

/* 暗色主题下content-tabs背景与外部一致 */
html.el-theme-dark .content-tabs {
  background-color: var(--el-bg-color-page) !important;
}

/* 首页样式 */
.home-section {
  padding: 0;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  background-color: #f5f5f5;
  color: #333;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.2);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 25px;
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  background-color: rgba(255, 255, 255, 0.2);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 8px 0;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: white;
  margin: 0;
}

/* 最近活动 */
.recent-activity {
  margin-top: 40px;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.subsection-title {
  font-size: 18px;
  color: #666;
  margin: 40px 0 20px 0;
}

/* 最近漫画网格 */
.recent-comic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 20px;
}

.comic-item {
  cursor: pointer;
  text-align: center;
  transition: all 0.3s ease;
  border-radius: 8px;
  padding: 10px;
  background-color: #f5f5f5;
}

.comic-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.comic-cover-wrapper {
  width: 100%;
  height: 180px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 12px;
}

.comic-cover-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.comic-item:hover .comic-cover-wrapper img {
  transform: scale(1.05);
}

.comic-title {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin: 0;
  font-weight: 500;
}

/* 历史记录样式 */
.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-item {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  background-color: #f5f5f5;
}

.history-item:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.history-item-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.history-cover {
  width: 100px;
  height: 150px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.history-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.history-cover img:hover {
  transform: scale(1.05);
}

.history-info {
  flex: 1;
}

.history-info .comic-title {
  font-size: 18px;
  color: #333;
  margin: 0 0 10px 0;
  font-weight: 500;
}

.chapter-name, .read-time {
  font-size: 14px;
  color: #666;
  margin: 6px 0;
}

.history-actions {
  margin-left: auto;
}

/* 收藏列表样式 */
.comic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 25px;
  margin-top: 20px;
}

.comic-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.comic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.comic-card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.comic-card .comic-cover {
  width: 100%;
  height: 300px;
  overflow: hidden;
  cursor: pointer;
}

.comic-card .comic-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.comic-card:hover .comic-cover img {
  transform: scale(1.05);
}

.comic-card .comic-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.comic-card .comic-title {
  font-size: 18px;
  color: #333;
  margin: 0 0 10px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  font-weight: 500;
}

.comic-card .comic-title:hover {
  color: #1890ff;
}

.comic-card .comic-author, .comic-card .comic-category {
  font-size: 14px;
  color: #666;
  margin: 6px 0;
}

.comic-card .card-actions {
  padding: 0 16px 16px;
  text-align: right;
}

/* 底部操作按钮 */
.bottom-actions {
  margin-top: 40px;
  text-align: center;
  padding-bottom: 60px;
}

.logout-button {
  font-size: 16px;
  padding: 10px 30px;
  border-radius: 8px;
  background-color: #ff6b6b;
  border: none;
  transition: all 0.3s ease;
}

.logout-button:hover {
  background-color: #ff5252;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
}

/* 暗色主题样式 - 增强视觉效果 */
:deep(.el-theme-dark) .user-center-container {
  background-color: var(--el-bg-color-page);
  padding: 20px;
}

:deep(.el-theme-dark) .user-header-banner {
  background-color: var(--el-bg-color-overlay);
  border-bottom: 2px solid var(--el-border-color);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  padding: 100px 30px 50px;
  margin-bottom: 30px;
  height: 350px;
  display: flex;
  align-items: flex-end;
}

html.el-theme-dark .user-name {
  color: var(--el-text-color-primary) !important;
  font-size: 24px !important;
  font-weight: 700 !important;
  margin-bottom: 8px !important;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .user-id {
  color: var(--el-text-color-secondary) !important;
  font-size: 14px !important;
  margin-bottom: 15px !important;
}

html.el-theme-dark .user-stats {
  display: flex !important;
  gap: 30px !important;
  margin-top: 20px !important;
}

html.el-theme-dark .user-stats > div {
  color: var(--el-text-color-primary) !important;
  font-size: 16px !important;
  font-weight: 600 !important;
  text-align: center !important;
  padding: 10px 15px !important;
  background-color: var(--el-bg-color) !important;
  border-radius: 8px !important;
  border: 1px solid var(--el-border-color) !important;
  transition: all 0.2s ease !important;
}

html.el-theme-dark .user-stats > div:hover {
  transform: translateY(-2px) !important;
  border-color: var(--el-color-primary) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2) !important;
}

html.el-theme-dark .el-tabs__nav {
  border-bottom: 2px solid var(--el-border-color) !important;
  padding-bottom: 5px !important;
}

html.el-theme-dark .el-tabs__item {
  color: var(--el-text-color-secondary) !important;
  font-size: 16px !important;
  padding: 12px 24px !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .el-tabs__item:hover {
  color: var(--el-text-color-primary) !important;
  transform: translateY(-2px) !important;
}

html.el-theme-dark .el-tabs__item.is-active {
  color: var(--el-color-primary) !important;
  font-weight: 600 !important;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2) !important;
}

html.el-theme-dark .el-tabs__active-bar {
  background-color: var(--el-color-primary) !important;
  height: 3px !important;
}

html.el-theme-dark .user-stats-card {
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 12px !important;
  padding: 25px !important;
  margin-bottom: 25px !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2) !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .user-stats-card:hover {
  transform: translateY(-3px) !important;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .user-stats-card h3 {
  color: var(--el-text-color-primary) !important;
  font-size: 20px !important;
  font-weight: 600 !important;
  margin-bottom: 20px !important;
  border-bottom: 2px solid var(--el-border-color) !important;
  padding-bottom: 10px !important;
}

html.el-theme-dark .user-stats-card .stats-value {
  color: var(--el-color-primary) !important;
  font-size: 32px !important;
  font-weight: 700 !important;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2) !important;
}

html.el-theme-dark .user-stats-card .stats-label {
  color: var(--el-text-color-secondary) !important;
  font-size: 14px !important;
  margin-top: 5px !important;
}

html.el-theme-dark .el-card {
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 12px !important;
  margin-bottom: 25px !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2) !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .el-card:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .el-card__header {
  background-color: var(--el-bg-color-page) !important;
  border-bottom: 1px solid var(--el-border-color) !important;
  padding: 15px 20px !important;
  border-radius: 12px 12px 0 0 !important;
}

html.el-theme-dark .el-card__header h3 {
  color: var(--el-text-color-primary) !important;
  font-size: 18px !important;
  font-weight: 600 !important;
}

html.el-theme-dark .el-card__body {
  background-color: var(--el-bg-color) !important;
  color: var(--el-text-color-primary) !important;
  padding: 20px !important;
}

html.el-theme-dark .activity-item {
  border-bottom: 1px solid var(--el-border-color-light) !important;
  padding: 15px 0 !important;
  transition: all 0.2s ease !important;
}

html.el-theme-dark .activity-item:hover {
  background-color: var(--el-bg-color-overlay) !important;
  padding-left: 10px !important;
  border-left: 3px solid var(--el-color-primary) !important;
}

html.el-theme-dark .activity-item .activity-time {
  color: var(--el-text-color-secondary) !important;
  font-size: 13px !important;
  margin-bottom: 8px !important;
}

html.el-theme-dark .comic-cover {
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.4) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 6px !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .comic-cover:hover {
  transform: scale(1.03) !important;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.5) !important;
}

html.el-theme-dark .comic-title {
  color: var(--el-text-color-primary) !important;
  font-size: 16px !important;
  font-weight: 600 !important;
  margin-bottom: 5px !important;
  transition: color 0.2s ease !important;
}

html.el-theme-dark .comic-title:hover {
  color: var(--el-color-primary) !important;
}

html.el-theme-dark .comic-info {
  color: var(--el-text-color-secondary) !important;
  font-size: 14px !important;
}

html.el-theme-dark .history-item {
  border-bottom: 1px solid var(--el-border-color-light) !important;
  padding: 15px 0 !important;
  transition: all 0.2s ease !important;
}

html.el-theme-dark .history-item:hover {
  background-color: var(--el-bg-color-overlay) !important;
  padding-left: 10px !important;
  border-left: 3px solid var(--el-color-primary) !important;
}

html.el-theme-dark .history-item .history-time {
  color: var(--el-text-color-secondary) !important;
  font-size: 13px !important;
  margin-bottom: 8px !important;
}

html.el-theme-dark .history-item .history-chapter {
  color: var(--el-text-color-primary) !important;
  font-size: 15px !important;
  font-weight: 500 !important;
  margin-bottom: 10px !important;
}

html.el-theme-dark .history-item .history-progress {
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 10px !important;
  height: 8px !important;
  overflow: hidden !important;
}

html.el-theme-dark .history-item .history-progress-bar {
  background-color: var(--el-color-primary) !important;
  height: 100% !important;
  transition: width 0.3s ease !important;
  border-radius: 10px !important;
}

html.el-theme-dark .favorite-grid {
  display: grid !important;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)) !important;
  gap: 20px !important;
}

html.el-theme-dark .favorite-grid .favorite-item {
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 12px !important;
  padding: 15px !important;
  transition: all 0.3s ease !important;
  text-align: center !important;
}

html.el-theme-dark .favorite-grid .favorite-item:hover {
  border-color: var(--el-color-primary) !important;
  transform: translateY(-5px) !important;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .favorite-grid .favorite-item .comic-title {
  color: var(--el-text-color-primary) !important;
  font-size: 15px !important;
  font-weight: 600 !important;
  margin-top: 10px !important;
  margin-bottom: 5px !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}

html.el-theme-dark .favorite-grid .favorite-item .comic-info {
  color: var(--el-text-color-secondary) !important;
  font-size: 13px !important;
  line-height: 1.4 !important;
}

html.el-theme-dark .clear-history-btn {
  color: var(--el-text-color-secondary) !important;
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color) !important;
  border-radius: 6px !important;
  padding: 8px 20px !important;
  font-size: 14px !important;
  transition: all 0.3s ease !important;
}

html.el-theme-dark .clear-history-btn:hover {
  color: var(--el-color-danger) !important;
  border-color: var(--el-color-danger) !important;
  background-color: rgba(245, 108, 108, 0.1) !important;
  transform: scale(1.03) !important;
}
</style>