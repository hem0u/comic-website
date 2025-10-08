<template>
  <div class="user-center-container">
    <!-- 页面标题 -->
    <h1 class="page-title">个人中心</h1>

    <!-- 用户信息卡片 -->
    <el-card class="user-info-card" shadow="hover">
      <div class="user-info-wrapper">
        <div class="avatar-box">
          <el-avatar :size="100" class="user-avatar">
            <img :src="getAvatarUrl(userInfo?.avatar) || 'https://picsum.photos/200'" alt="用户头像" />
          </el-avatar>
        </div>
        <div class="user-meta">
          <h2 class="nickname">{{ userInfo?.nickname || '未设置昵称' }}</h2>
          <p class="username">用户名：{{ userInfo?.username || '' }}</p>
          <p class="email">邮箱：{{ userInfo?.email || '未绑定邮箱' }}</p>
          <el-button
              type="danger"
              size="small"
              @click="handleLogout"
              style="margin-top: 12px;"
          >
            退出登录
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 标签页：收藏 + 历史 -->
    <el-tabs v-model="activeTab" class="content-tabs" style="margin-top: 20px;">
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
          <el-card
              v-else
              v-for="comic in collectList"
              :key="comic.id"
              class="collect-comic-card"
              shadow="hover"
          >
            <div class="card-content">
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
          <el-card
              v-else
              v-for="item in historyList"
              :key="item.historyId"
              class="history-item"
              shadow="hover"
          >
            <div class="history-content">
              <div class="history-cover" @click="goToRead(item.comicId)">
                <img :src="item.cover || 'https://picsum.photos/100/150'" alt="漫画封面" />
              </div>
              <div class="history-info">
                <h3 class="comic-title" @click="goToRead(item.comicId)">{{ item.comicTitle || '未知漫画' }}</h3>
                <p class="chapter-name">最近阅读：{{ item.chapterTitle || '未选择章节' }}</p>
                <p class="read-time">阅读时间：{{ formatTime(item.readTime) }}</p>
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElLoading, ElEmpty } from 'element-plus';
import { useUserStore } from '../stores/userStore';
import { getUserInfo } from '../api/user';
import { getUserCollects, cancelCollect } from '../api/collect';
import { getUserReadHistory } from '../api/history';

// 路由和状态实例
const router = useRouter();
const userStore = useUserStore();

// 页面状态
const activeTab = ref('collect');
const userInfo = ref({});
const collectList = ref([]);
const isCollectLoading = ref(false);
const historyList = ref([]);
const isHistoryLoading = ref(false);

// 退出登录
const handleLogout = () => {
  userStore.clearToken();
  localStorage.removeItem('token');
  router.push('/login');
  ElMessage.success('已成功退出登录');
};

// 处理头像路径，确保正确显示
const getAvatarUrl = (avatarPath) => {
  // 如果没有头像，返回空字符串
  if (!avatarPath) return '';
  
  // 如果是绝对路径或者已经是完整的URL，则直接返回
  if (avatarPath.startsWith('http://') || avatarPath.startsWith('https://')) {
    return avatarPath;
  }
  
  // 处理相对路径
  if (avatarPath.startsWith('../assets/')) {
    // 将 ../assets/ 转换为 ./assets/
    return avatarPath.replace('../assets/', './assets/');
  }
  
  // 其他情况，直接返回
  return avatarPath;
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
/* 保持原有样式不变 */
.user-center-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 20px 0;
}

.user-info-card {
  background-color: #fff;
  margin-bottom: 20px;
}

.user-center-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 20px 0;
}

.user-info-card {
  background-color: #fff;
  margin-bottom: 20px;
}

.user-info-wrapper {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 25px;
}

.avatar-box {
  flex-shrink: 0;
}

.user-avatar {
  border: 2px solid #f0f0f0;
}

.user-meta {
  flex: 1;
}

.nickname {
  font-size: 20px;
  color: #333;
  margin: 0 0 10px 0;
}

.username, .email {
  font-size: 14px;
  color: #666;
  margin: 6px 0;
}

.content-tabs {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.comic-collect-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.collect-comic-card {
  transition: transform 0.3s ease;
  cursor: default;
}

.collect-comic-card:hover {
  transform: translateY(-5px);
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.comic-cover {
  width: 100%;
  height: 280px;
  overflow: hidden;
  border-radius: 4px 4px 0 0;
  cursor: pointer;
}

.comic-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.comic-cover img:hover {
  transform: scale(1.05);
}

.comic-info {
  padding: 12px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.comic-title {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.comic-title:hover {
  color: #1890ff;
}

.comic-author, .comic-category {
  font-size: 12px;
  color: #666;
  margin: 4px 0;
}

.card-actions {
  padding: 0 12px 12px;
  text-align: right;
}

.read-history-list {
  margin-top: 20px;
}

.history-item {
  margin-bottom: 15px;
  transition: box-shadow 0.3s ease;
}

.history-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.history-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px;
}

.history-cover {
  width: 80px;
  height: 120px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 4px;
  cursor: pointer;
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

.chapter-name, .read-time {
  font-size: 14px;
  color: #666;
  margin: 6px 0;
}
</style>