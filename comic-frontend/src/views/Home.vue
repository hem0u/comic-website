<template>
  <div class="home-container">
    <!-- 顶部横幅：依赖 main-inner 容器居中 -->
    <div class="hero-banner">
      <div class="banner-content">
        <h1>探索无限漫画世界</h1>
        <p>发现最新、最热门的漫画作品，开启你的阅读之旅</p>
        <el-button type="primary" size="large" @click="$router.push('/comic-list')">
          开始探索
        </el-button>
      </div>
    </div>

    <!-- 分类导航：依赖 main-inner 容器居中 -->
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

    <!-- 热门推荐：依赖 main-inner 容器居中 -->
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

    <!-- 最新上架：依赖 main-inner 容器居中 -->
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
// 脚本部分完全不变，确保数据加载正常
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
/* 首页容器：100%宽度，依赖外层 main-inner 居中 */
.home-container {
  width: 100%;
}

/* 顶部横幅：100%宽度，在 main-inner 容器中居中 */
.hero-banner {
  width: 100%;
  height: 400px;
  background: linear-gradient(135deg, #409eff 0%, #69b1ff 100%);
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
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.hero-banner p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

/* 分类区域：100%宽度，在 main-inner 容器中居中 */
.category-section {
  margin-bottom: 60px;
}

.section-title {
  font-size: 24px;
  color: #333;
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
  background-color: #409eff;
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
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
}

.category-icon {
  margin-bottom: 15px;
  color: #409eff;
}

.category-symbol {
  font-size: 32px;
}

.category-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

/* 热门推荐 & 最新上架：100%宽度，在 main-inner 容器中居中 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
  background-color: #fff;
  border-radius: 8px;
}

.comic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
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
  color: #333;
  font-weight: 500;
}

.comic-author {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

/* 无数据占位 */
.empty-placeholder {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.empty-text {
  font-size: 16px;
  color: #999;
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