<template>
  <div class="home-container">
    <!-- 顶部横幅 -->
    <div class="hero-banner">
      <div class="banner-content">
        <h1>探索无限漫画世界</h1>
        <p>发现最新、最热门的漫画作品，开启你的阅读之旅</p>
        <el-button type="primary" size="large" @click="$router.push('/comic-list')">
          开始探索
        </el-button>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-section">
      <h2 class="section-title">漫画分类</h2>
      <div class="category-list">
        <el-card
            v-for="category in categoryList"
            :key="category.id"
            class="category-card"
            @click="$router.push({path: '/comic-list', query: {categoryId: category.id}})"
        >
          <div class="category-icon">
            <span class="category-symbol">📖</span>
          </div>
          <div class="category-name">{{ category.name }}</div>
        </el-card>
      </div>
    </div>

    <!-- 热门推荐 -->
    <div class="featured-section">
      <div class="section-header">
        <h2 class="section-title">热门推荐</h2>
        <el-link @click="$router.push('/comic-list')">查看全部</el-link>
      </div>
      <div class="comic-grid">
        <div v-if="featuredComics.length === 0" class="empty-placeholder">
          <span class="empty-text">暂无热门漫画数据</span>
        </div>
        <el-card
            v-else
            v-for="comic in featuredComics"
            :key="comic.id"
            class="comic-card"
            shadow="hover"
            @click="$router.push(`/comic-read/${comic.id}`)"
        >
          <div class="comic-cover-container">
            <img
                :src="comic.cover || 'https://picsum.photos/300/400'"
                :alt="comic.title"
                class="comic-cover"
                loading="lazy"
            >
          </div>
          <div class="comic-info">
            <h3 class="comic-title">{{ comic.title }}</h3>
            <p class="comic-author">作者: {{ comic.authorName || '未知作者' }}</p>
            <div class="comic-status">
              <el-tag :type="getStatusType(comic.status)">
                {{ getStatusText(comic.status) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 最新上架 -->
    <div class="latest-section">
      <div class="section-header">
        <h2 class="section-title">最新上架</h2>
        <el-link @click="$router.push('/comic-list')">查看全部</el-link>
      </div>
      <div class="comic-grid">
        <div v-if="latestComics.length === 0" class="empty-placeholder">
          <span class="empty-text">暂无最新漫画数据</span>
        </div>
        <el-card
            v-else
            v-for="comic in latestComics"
            :key="comic.id"
            class="comic-card"
            shadow="hover"
            @click="$router.push(`/comic-read/${comic.id}`)"
        >
          <div class="comic-cover-container">
            <img
                :src="comic.cover || 'https://picsum.photos/300/400'"
                :alt="comic.title"
                class="comic-cover"
                loading="lazy"
            >
          </div>
          <div class="comic-info">
            <h3 class="comic-title">{{ comic.title }}</h3>
            <p class="comic-author">作者: {{ comic.authorName || '未知作者' }}</p>
            <div class="comic-status">
              <el-tag :type="getStatusType(comic.status)">
                {{ getStatusText(comic.status) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCategoryList, getComicList } from '../api/comic';
import { ElMessage, ElTag, ElCard, ElLink, ElButton } from 'element-plus';

const categoryList = ref([]);
const featuredComics = ref([]);
const latestComics = ref([]);
const loading = ref(true);

onMounted(() => {
  Promise.all([
    loadCategories(),
    loadFeaturedComics(),
    loadLatestComics()
  ]).finally(() => {
    loading.value = false;
  });
});

const loadCategories = async () => {
  try {
    const res = await getCategoryList();
    if (res.code === 200) categoryList.value = res.data || [];
    else ElMessage.error(res.msg || '获取分类失败');
  } catch (e) {
    console.error('获取分类失败', e);
    ElMessage.error('获取分类失败，请刷新重试');
  }
};

const loadFeaturedComics = async () => {
  try {
    const res = await getComicList({ page: 1, size: 8, sort: 'collectCount', order: 'desc' });
    if (res.code === 200) featuredComics.value = res.data?.list || [];
    else ElMessage.error(res.msg || '获取热门漫画失败');
  } catch (e) {
    console.error('获取热门漫画失败', e);
    ElMessage.error('获取热门漫画失败');
  }
};

const loadLatestComics = async () => {
  try {
    const res = await getComicList({ page: 1, size: 8, sort: 'createTime', order: 'desc' });
    if (res.code === 200) latestComics.value = res.data?.list || [];
    else ElMessage.error(res.msg || '获取最新漫画失败');
  } catch (e) {
    console.error('获取最新漫画失败', e);
    ElMessage.error('获取最新漫画失败');
  }
};

const getStatusText = (status) => {
  if (status === 1) return '已完结';
  if (status === 0) return '连载中';
  return '未知状态';
};

const getStatusType = (status) => {
  if (status === 1) return 'success';
  if (status === 0) return 'primary';
  return 'info';
};
</script>

<style scoped>
/* 首页容器 */
.home-container {
  width: 100%;
}

/* 顶部横幅 */
.hero-banner {
  width: 100%;
  height: 400px;
  background: linear-gradient(135deg, #333 0%, #666 100%); /* 黑白灰渐变 */
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  border-radius: 8px;
  margin-bottom: 40px;
}

.banner-content {
  text-align: center;
  max-width: 800px;
  padding: 0 20px;
}

.hero-banner h1 {
  font-size: 42px;
  margin-bottom: 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.hero-banner p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

/* 按钮样式覆盖 */
:deep(.el-button--primary) {
  background-color: #ff7eb3; /* 粉色按钮 */
  border-color: #ff7eb3;
  color: #fff;
}

:deep(.el-button--primary:hover) {
  background-color: #ff66a8; /* 深一点的粉色 */
  border-color: #ff66a8;
}

/* 分类区域 */
.category-section {
  margin-bottom: 60px;
}

.section-title {
  font-size: 24px;
  color: #333; /* 深灰色标题 */
  font-weight: 600;
  position: relative;
  padding-bottom: 10px;
  margin-bottom: 30px;
}

.section-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 3px;
  background-color: #ff7eb3; /* 粉色下划线 */
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 20px;
}

.category-card {
  text-align: center;
  padding: 30px 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  border: 1px solid #f0f0f0; /* 浅灰色边框 */
  background-color: #fff; /* 白色背景 */
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  border-color: #ff7eb3; /* 粉色边框高亮 */
}

.category-icon {
  margin-bottom: 15px;
  color: #ff7eb3; /* 粉色图标 */
}

.category-symbol {
  font-size: 32px;
}

.category-name {
  font-size: 16px;
  font-weight: 500;
  color: #333; /* 深灰色文字 */
}

/* 热门推荐 & 最新上架 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 链接样式覆盖 */
:deep(.el-link) {
  color: #666; /* 中灰色链接 */
}

:deep(.el-link:hover) {
  color: #ff7eb3; /* 粉色悬停效果 */
}

.featured-section, .latest-section {
  margin-bottom: 60px;
}

.comic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 25px;
}

.comic-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  overflow: hidden;
  cursor: pointer;
  background-color: #fff; /* 白色卡片 */
  border-radius: 8px;
  border: 1px solid #f0f0f0; /* 浅灰色边框 */
}

.comic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  border-color: #ff7eb3; /* 粉色边框高亮 */
}

.comic-cover-container {
  width: 100%;
  padding-top: 140%;
  position: relative;
  overflow: hidden;
  border-radius: 8px 8px 0 0;
}

.comic-cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.comic-card:hover .comic-cover {
  transform: scale(1.05);
}

.comic-info {
  padding: 15px;
}

.comic-title {
  font-size: 16px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #333; /* 深灰色标题 */
  font-weight: 500;
}

.comic-author {
  font-size: 14px;
  color: #666; /* 中灰色作者名 */
  margin-bottom: 10px;
}

/* 标签样式覆盖 */
:deep(.el-tag--primary) {
  background-color: #f5f5f5;
  color: #333;
  border-color: #ddd;
}

:deep(.el-tag--success) {
  background-color: #f5f5f5;
  color: #ff7eb3;
  border-color: #ff7eb3;
}

/* 无数据占位 */
.empty-placeholder {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  background-color: #fff; /* 白色背景 */
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px dashed #ddd; /* 虚线边框 */
}

.empty-text {
  font-size: 16px;
  color: #999; /* 浅灰色文字 */
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-banner {
    height: 300px;
  }

  .hero-banner h1 {
    font-size: 32px;
  }

  .comic-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }

  .section-title {
    font-size: 20px;
  }
}
</style>
