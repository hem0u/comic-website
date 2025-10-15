<template>
  <div class="home-container">
    <!-- 热门新漫画轮播 -->
    <div class="hot-comic-carousel">
      <div class="section-header">
        <h2 class="section-title">热门新漫画</h2>
      </div>
      <div v-if="topComics.length === 0" class="empty-placeholder">
        <span class="empty-text">暂无热门榜数据</span>
      </div>
      <div v-else class="carousel-container">
        <div class="carousel-item active" v-for="(comic, index) in topComics.slice(0, 10)" :key="comic.id" :class="{ 'active': currentSlide === index }" v-show="currentSlide === index">
          <div class="comic-rank">
            <span class="rank-number">NO.{{ index + 1 }}</span>
          </div>
          <div class="comic-content">
            <div class="comic-cover-grayscale">
              <img :src="comic.cover || 'https://picsum.photos/300/400'" :alt="comic.title" class="cover-image" />
            </div>
            <div class="comic-details">
              <h3 class="comic-main-title">{{ comic.title }}</h3>
              <div class="comic-meta-info">
                <span class="comic-tag" v-for="(tag, tagIndex) in comic.tags" :key="tagIndex">
                  {{ tag.name }}
                </span>
              </div>
              <p class="comic-description">{{ comic.description || '暂无简介' }}</p>
            </div>
            <div class="comic-creator-wrapper">
              <div class="comic-creator">
                <span class="creator-name">{{ comic.authorName || '未知作者' }}</span>
              </div>
            </div>
          </div>
          <button class="carousel-btn prev" @click="prevSlide">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M15 18l-6-6 6-6"></path>
            </svg>
          </button>
          <button class="carousel-btn next" @click="nextSlide">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 18l6-6-6-6"></path>
            </svg>
          </button>
        </div>
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
const topComics = ref([]);
const loading = ref(true);
const currentSlide = ref(0);

onMounted(() => {
  Promise.all([
    loadCategories(),
    loadFeaturedComics(),
    loadLatestComics(),
    loadTopComics()
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

const loadTopComics = async () => {
  try {
    const res = await getComicList({ page: 1, size: 10, sort: 'collectCount', order: 'desc' });
    if (res.code === 200) topComics.value = res.data?.list || [];
    else ElMessage.error(res.msg || '获取热门榜漫画失败');
  } catch (e) {
    console.error('获取热门榜漫画失败', e);
    ElMessage.error('获取热门榜漫画失败');
  }
};

const scrollToFeatured = () => {
  const featuredSection = document.querySelector('.featured-section');
  if (featuredSection) {
    featuredSection.scrollIntoView({ behavior: 'smooth' });
  }
};

const goToComicDetail = (comicId) => {
  window.location.href = `/comic-read/${comicId}`;
};

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + topComics.value.length) % topComics.value.length;
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % topComics.value.length;
};

const getRankClass = (rank) => {
  if (rank === 1) return 'rank-first';
  if (rank === 2) return 'rank-second';
  if (rank === 3) return 'rank-third';
  return 'rank-normal';
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
  margin-bottom: 10px;
}

/* 链接样式覆盖 */
:deep(.el-link) {
  color: #666; /* 中灰色链接 */
}

:deep(.el-link:hover) {
  color: #ff7eb3; /* 粉色悬停效果 */
}

.featured-section, .latest-section, .top-ranking-section {
  margin-bottom: 60px;
}

/* 热门新漫画轮播样式 */
.hot-comic-carousel {
  margin-bottom: 40px;
  padding: 30px;
  background-color: #fff; /* 和外部容器背景颜色完全一致 */
  border-radius: 16px;
  position: relative;
  overflow: hidden;
}

.comic-details {
  flex: 1;
  color: #333; /* 黑色文本 */
  max-width: 600px;
  position: relative;
}

.comic-main-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 15px;
  color: #333; /* 黑色标题 */
  text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
}

.comic-meta-info {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.comic-tag {
  padding: 5px 12px;
  background-color: rgba(0,0,0,0.05);
  border-radius: 20px;
  font-size: 12px;
  color: #666;
  border: 1px solid rgba(0,0,0,0.1);
}

.comic-description {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.comic-creator {
  font-size: 14px;
  color: #999;
}

.creator-name {
  color: #ff7eb3;
  font-weight: 500;
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0,0,0,0.1);
  border: 1px solid rgba(0,0,0,0.2);
  color: #333;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 3;
}

.carousel-btn:hover {
  background-color: rgba(255,126,179,0.2);
  transform: translateY(-50%) scale(1.1);
}

.carousel-container {
  position: relative;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.carousel-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  transition: opacity 0.5s ease;
}

.comic-rank {
  position: absolute;
  top: 20px;
  right: 30px;
  z-index: 2;
}

.rank-number {
  color: #ff7eb3;
  font-size: 24px;
  font-weight: bold;
  font-family: 'Arial Black', sans-serif;
  letter-spacing: 1px;
}

.comic-content {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%;
  position: relative;
}

.comic-cover-grayscale {
  flex-shrink: 0;
  position: relative;
}

.comic-cover-grayscale::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(255,255,255,0.1), transparent);
  z-index: 1;
}

.cover-image {
  width: 220px;
  height: 320px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 3px 8px rgba(0,0,0,0.2);
  transition: all 0.3s ease;
  cursor: pointer;
}

.cover-image:hover {
  transform: scale(1.02);
}

.comic-details {
    flex: 1;
    color: #333;
    max-width: 600px;
    position: relative;
  }

  .comic-main-title {
    font-size: 28px;
    font-weight: 800;
    margin-top: -160px;
    margin-bottom: 8px;
    color: #333;
  }

  .comic-creator-wrapper {
    position: absolute;
    bottom: 10px;
    left: 238px;
  }

  .comic-meta-info {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    flex-wrap: wrap;
  }

  .comic-tag {
    padding: 5px 12px;
    background-color: rgba(0,0,0,0.05);
    border-radius: 20px;
    font-size: 12px;
    color: #666;
    border: 1px solid rgba(0,0,0,0.1);
  }

  .comic-description {
    font-size: 14px;
    line-height: 1.5;
    color: #666;
    margin-bottom: 10px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .comic-creator {
    font-size: 13px;
    color: #999;
    margin-top: 10px;
  }

.creator-name {
  color: #ff7eb3;
  font-weight: 500;
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.3);
  color: #fff;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 3;
}

.carousel-btn:hover {
  background-color: rgba(255,126,179,0.3);
  transform: translateY(-50%) scale(1.1);
}

.carousel-btn.prev {
  left: 20px;
}

.carousel-btn.next {
  right: 20px;
}

.carousel-btn svg {
  width: 24px;
  height: 24px;
}

/* 暗色主题适配 */
html.el-theme-dark .hot-comic-carousel {
  background-color: #111 !important;
  border: 1px solid #333 !important;
}

html.el-theme-dark .comic-tag {
  background-color: rgba(255,255,255,0.1) !important;
  border-color: rgba(255,255,255,0.2) !important;
  color: #ccc !important;
}

/* 隐藏原来的热门榜样式 */
.top-ranking-section {
  display: none;
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

/* 暗色主题样式 */
html.el-theme-dark .category-card {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .category-card:hover {
  border-color: var(--el-color-primary) !important;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5) !important;
}

html.el-theme-dark .category-name {
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .comic-card {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .comic-card:hover {
  border-color: var(--el-color-primary) !important;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5) !important;
}

html.el-theme-dark .comic-title {
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .comic-author {
  color: var(--el-text-color-secondary) !important;
}

html.el-theme-dark .empty-placeholder {
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

html.el-theme-dark .empty-text {
  color: var(--el-text-color-secondary) !important;
}

html.el-theme-dark .section-title {
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .section-title::after {
  background-color: var(--el-color-primary) !important;
}

/* 暗色主题标签样式 - 增加选择器特异性 */

/* 暗色主题主页容器样式 */
html.el-theme-dark .home-container {
  background-color: var(--el-bg-color-page) !important;
  border: none !important;
  border-radius: 0 !important;
}
/* 连载中标签 - 使用灰色调 */
html.el-theme-dark .comic-card :deep(.el-tag--primary) {
  background-color: rgba(255, 255, 255, 0.1) !important; /* 白色半透明背景 */
  color: var(--el-text-color-primary) !important; /* 白色文本 */
  border-color: var(--el-border-color) !important; /* 灰色边框 */
}

/* 已完结标签 - 使用粉色调 */
html.el-theme-dark .comic-card :deep(.el-tag--success) {
  background-color: rgba(255, 126, 179, 0.2) !important; /* 粉色半透明背景 */
  color: var(--el-color-primary) !important; /* 粉色文本 */
  border-color: var(--el-color-primary) !important; /* 粉色边框 */
}

/* 确保所有可能的标签位置都被覆盖 */
html.el-theme-dark :deep(.el-tag--primary) {
  background-color: rgba(255, 255, 255, 0.1) !important; /* 白色半透明背景 */
  color: var(--el-text-color-primary) !important; /* 白色文本 */
  border-color: var(--el-border-color) !important; /* 灰色边框 */
}

html.el-theme-dark :deep(.el-tag--success) {
  background-color: rgba(255, 126, 179, 0.2) !important; /* 粉色半透明背景 */
  color: var(--el-color-primary) !important; /* 粉色文本 */
  border-color: var(--el-color-primary) !important; /* 粉色边框 */
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
