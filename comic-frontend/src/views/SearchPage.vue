<template>
  <div class="search-page-container">
    <!-- 搜索头部区域 -->
    <div class="search-header">
      <!-- 返回按钮 -->
      <div class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </div>
      <!-- 页面标题 -->
      <h1 class="page-title">高级搜索</h1>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <!-- 搜索框和过滤器 -->
      <div class="search-controls">
        <div class="search-input-wrapper">
          <el-input
              v-model="advancedKeyword"
              placeholder="搜索"
              class="search-input"
              clearable
              @keyup.enter="performSearch"
          >
            <template #prefix>
              <i class="el-icon-search search-icon"></i>
            </template>
          </el-input>
          <el-button 
            :type="showFilters ? 'primary' : 'default'" 
            @click="toggleFilters" 
            class="filter-button"
            :class="{ 'active': showFilters }"
            size="small"
          >
            <i class="el-icon-arrow-down filter-arrow" :class="{ 'rotated': showFilters }"></i>
            {{ showFilters ? '隐藏过滤器' : '显示过滤器' }}
          </el-button>
        </div>

        <!-- 过滤器选项 -->
        <div v-if="showFilters" class="filter-options">
          <div class="filter-row single-row">
            <div class="filter-item">
              <label>排序方式</label>
              <el-select v-model="sortBy" class="filter-select">
                <el-option label="无" value="none"></el-option>
                <el-option label="人气" value="popularity"></el-option>
                <el-option label="评分" value="rating"></el-option>
                <el-option label="最新" value="newest"></el-option>
              </el-select>
            </div>
            <div class="filter-item">
              <label>标签过滤</label>
              <el-select v-model="filterTags" class="filter-select">
                <el-option label="包含任意" value="any"></el-option>
                <el-option label="包含全部" value="all"></el-option>
              </el-select>
            </div>
            <div class="filter-item">
              <label>作者</label>
              <el-select v-model="authors" class="filter-select">
                <el-option label="任意" value="any"></el-option>
              </el-select>
            </div>
            <div class="filter-item">
              <label>出版年份</label>
              <div class="year-input">
                <el-input v-model="minYear" placeholder="最小" class="year-input-small"></el-input>
                <span>-</span>
                <el-input v-model="maxYear" placeholder="最大" class="year-input-small"></el-input>
              </div>
            </div>
            <div class="filter-item">
              <label>出版状态</label>
              <el-select v-model="publicationStatus" class="filter-select">
                <el-option label="任意" value="any"></el-option>
                <el-option label="连载中" value="ongoing"></el-option>
                <el-option label="已完结" value="completed"></el-option>
              </el-select>
            </div>
          </div>
        </div>
        
        <!-- 搜索操作按钮组 - 始终显示在过滤器按钮下方 -->
        <div class="action-buttons">
          <el-button type="text" class="reset-filters-btn" @click="resetFilters">重置过滤器</el-button>
          <el-button type="text" class="feeling-lucky-btn" @click="imFeelingLucky">幸运搜索</el-button>
          <el-button 
            type="primary" 
            @click="performSearch" 
            class="search-button"
          >
            <i class="el-icon-search"></i>
            搜索
          </el-button>
        </div>
      </div>

      <!-- 布局切换按钮 -->
      <div class="layout-switcher">
        <el-button-group>
          <el-button type="text" :class="{ active: layout === 'list' }" @click="layout = 'list'" icon="el-icon-menu"></el-button>
          <el-button type="text" :class="{ active: layout === 'grid' }" @click="layout = 'grid'" icon="el-icon-s-grid"></el-button>
          <el-button type="text" :class="{ active: layout === 'large' }" @click="layout = 'large'" icon="el-icon-picture-outline"></el-button>
        </el-button-group>
      </div>
    </div>



    <!-- 加载状态 -->
    <div
        v-loading="loading"
        class="loading-container"
        element-loading-text="正在搜索漫画..."
        element-loading-spinner="el-icon-loading"
    >
      <!-- 漫画搜索结果列表 -->
      <div class="comic-grid">
        <div v-for="comic in comicList" :key="comic.id" class="comic-item" @click="goToRead(comic.id)">
          <div class="comic-cover-wrapper">
            <img
                :src="comic.cover || 'https://picsum.photos/300/400?random=' + comic.id"
                :alt="comic.title || '漫画封面'"
                class="comic-cover"
                loading="lazy"
            >
          </div>
          <div class="comic-details">
            <div class="comic-title">{{ comic.title || '未知标题' }}</div>
            <!-- 标签区域 -->
            <div class="comic-tags">
              <span v-for="tag in comic.tags" :key="tag" class="comic-tag">{{ tag }}</span>
            </div>
            <!-- 评分和状态 -->
            <div class="comic-meta">
              <div class="rating">
                <i class="el-icon-star-on"></i>
                <span>{{ comic.rating || 'N/A' }}</span>
              </div>
              <div class="comments">{{ comic.commentCount || 'N/A' }} 评论</div>
              <div class="views">{{ comic.viewCount || 'N/A' }} 浏览</div>
              <div class="status-badge">{{ getStatusText(comic.status) }}</div>
            </div>
            <!-- 描述 -->
            <div class="comic-description" v-if="comic.description">
              {{ truncateDescription(comic.description) }}
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="comicList.length === 0 && !loading" class="empty-state">
        <el-empty description="未找到漫画"></el-empty>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[8, 16, 24, 32]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          v-if="total > 0"
          background
      ></el-pagination>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getComicList } from '../api/comic';
import { useRouter, useRoute } from 'vue-router';
import { ElInput, ElButton, ElCard, ElTag, ElEmpty, ElPagination, ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';

const router = useRouter();
const route = useRoute();

// 布局模式
const layout = ref('grid'); // 'list', 'grid', 'large'

// 过滤器显示状态
const showFilters = ref(true);

// 过滤器选项
const sortBy = ref('none');
const filterTags = ref('any');
const authors = ref('any');
const minYear = ref('');
const maxYear = ref('');
const publicationStatus = ref('any');

// 漫画列表数据 - 添加符合图片样式的模拟数据
const comicList = ref([
  {
    id: 1,
    title: "Sewayaki Kitsune no Senko-san",
    cover: "https://picsum.photos/300/400?random=1",
    authorName: "Ritsu Yuuki",
    categoryName: "Comedy",
    status: 1,
    viewCount: 54000,
    collectCount: 388,
    rating: 8.01,
    commentCount: 209,
    description: "Like many hardworking members of the workforce, Kuroto Nakano is perpetually stressed out by his job. Still, since he lives alone, he must carry on to sustain himself. Little do humans like Kuroto know, this stress takes the form of darkness residing within a person's body and will bring one's life to ruin.",
    tags: ["Animals", "Romance", "Comedy", "Office Workers", "Magic", "Fantasy", "Monster Girls", "Slice of Life"]
  },
  {
    id: 2,
    title: "Mobile Suit Gundam: The Witch from Mercury - Sulemio Cuddle",
    cover: "https://picsum.photos/300/400?random=2",
    authorName: "Bandai Namco",
    categoryName: "Romance",
    status: 1,
    viewCount: 29000,
    collectCount: 368,
    rating: 8.39,
    commentCount: 203,
    description: "A heartwarming spinoff from the popular Gundam series focusing on the relationship between Suletta and Miorine.",
    tags: ["Doujinshi", "Oneshot", "Romance", "Girls Love", "Web Comic"]
  }
]);
// 分页参数
const currentPage = ref(1);
const pageSize = ref(8);
const total = ref(1);
// 搜索关键词
const advancedKeyword = ref('');
// 加载状态
const loading = ref(false);

// 获取漫画列表
const fetchComicList = async () => {
  loading.value = true;
  try {
    const keyword = advancedKeyword.value.trim();
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: keyword
    };
    const res = await getComicList(params);

    if (!res || !res.data) {
      throw new Error('接口返回格式异常');
    }

    // 如果返回有数据，使用API数据；如果没有数据或关键词为空，清空列表
    if (res.data.list && res.data.list.length > 0) {
      comicList.value = res.data.list;
      total.value = res.data.total || 0;
    } else {
      comicList.value = [];
      total.value = 0;
    }

  } catch (error) {
    console.error('获取漫画列表失败', error);
    // 发生错误时清空列表，不显示提示
    comicList.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
  }
};

// 执行搜索
const performSearch = () => {
  currentPage.value = 1;
  fetchComicList();
};

// 返回上一页
const goBack = () => {
  router.back();
};

// 切换过滤器显示状态
const toggleFilters = () => {
  showFilters.value = !showFilters.value;
};

// 重置过滤器
  const resetFilters = () => {
    advancedKeyword.value = '';
    sortBy.value = 'none';
    filterTags.value = 'any';
    authors.value = 'any';
    minYear.value = '';
    maxYear.value = '';
    publicationStatus.value = 'any';
    comicList.value = [];
    total.value = 0;
    ElMessage.info('过滤器已重置');
  };

// 幸运搜索
const imFeelingLucky = () => {
  if (comicList.value.length > 0) {
    const randomIndex = Math.floor(Math.random() * comicList.value.length);
    goToRead(comicList.value[randomIndex].id);
  } else {
    ElMessage.warning('没有可用漫画');
  }
};

// 截断描述
const truncateDescription = (description) => {
  if (description && description.length > 300) {
    return description.substring(0, 300) + '...';
  }
  return description;
};

// 跳转到漫画阅读页
const goToRead = (comicId) => {
  router.push(`/comic-read/${comicId}`);
};

// 分页处理函数
const handleSizeChange = (size) => {
  pageSize.value = size;
  fetchComicList();
};

const handleCurrentChange = (current) => {
  currentPage.value = current;
  fetchComicList();
};

// 状态文本映射
const getStatusText = (status) => {
  if (status === 1) return '已完结';
  if (status === 0) return '连载中';
  return '未知状态';
};

// 状态标签类型映射
const getStatusType = (status) => {
  if (status === 1) return 'success';
  if (status === 0) return 'primary';
  return 'info';
};

// 监听路由参数变化，更新搜索关键词
watch(
  () => route.query.keyword,
  (newKeyword) => {
    if (newKeyword) {
      advancedKeyword.value = decodeURIComponent(newKeyword);
      currentPage.value = 1;
      fetchComicList();
    }
  },
  { immediate: true }
);

// 页面挂载时初始化
onMounted(() => {
  if (route.query.keyword) {
    // 已经在watch中处理
  } else {
    // 如果没有搜索关键词，清空列表
    comicList.value = [];
    total.value = 0;
  }
});
</script>

<style scoped>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 主容器样式 */
.search-page-container {
  min-height: 100vh;
  background-color: #fff;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

/* 搜索头部区域 */
.search-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.back-button {
  cursor: pointer;
  color: #333;
  font-size: 18px;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.back-button:hover {
  background-color: #f5f5f5;
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
}

/* 搜索区域 */
.search-area {
  margin-bottom: 20px;
}

/* 搜索控制区域 */
.search-controls {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  min-width: 300px;
}

/* 过滤器选项样式 */
.filter-options {
  background-color: #f9f9f9;
  padding: 16px;
  border-radius: 4px;
  margin-top: 12px;
  width: 100%;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 16px;
  gap: 16px;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.filter-item {
  display: flex;
  flex-direction: column;
  min-width: 160px;
}

.filter-item label {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
  font-weight: 500;
}

.filter-select {
  width: 160px;
  font-size: 14px;
}

.year-input {
  display: flex;
  align-items: center;
  gap: 8px;
}

.year-input-small {
  width: 60px;
}

.year-input span {
  color: #666;
}

/* 搜索输入框样式 - 灰色无边框带圆角 */
:deep(.search-input .el-input__wrapper) {
  border-radius: 8px;
  border: none;
  box-shadow: none;
  transition: all 0.2s ease;
  background-color: #f5f5f5;
}

:deep(.search-input .el-input__wrapper:hover) {
  background-color: #e8e8e8;
}

:deep(.search-input .el-input__wrapper.is-focus) {
  background-color: #e8e8e8;
  box-shadow: 0 0 0 2px rgba(255, 126, 179, 0.3);
}

:deep(.search-input .el-input__inner) {
  font-size: 14px;
  color: #000;
  padding: 8px 12px;
  height: 40px;
  background-color: transparent;
}

.search-icon {
  color: #666;
  font-size: 16px;
  margin-right: 4px;
}

/* 过滤器按钮 - 初始浅灰色，激活状态粉色 */
.filter-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  padding: 0 12px;
  height: 40px;
  border: none;
  border-radius: 8px;
  background-color: #e0e0e0;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
}

/* 过滤器按钮箭头图标样式 */
.filter-arrow {
  font-size: 12px;
  transition: transform 0.2s ease;
  color: #666;
}

/* 激活状态下的箭头图标颜色 */
.filter-button.active .filter-arrow {
  color: #fff;
}

.filter-button:hover {
  background-color: #d0d0d0;
  color: #333;
}

/* 确保图标正确显示 */
.el-icon-search,
.el-icon-arrow-down {
  display: inline-block;
}

/* 激活状态 - 显示过滤器时 */
.filter-button.active {
  background-color: #ff7eb3;
  color: #fff;
}

.filter-button.active:hover {
  background-color: #ff5e99;
  color: #fff;
}

.filter-arrow {
  font-size: 12px;
  transition: transform 0.2s ease;
}

.filter-arrow.rotated {
  transform: rotate(180deg);
}

/* 过滤器按钮组 - 已移动到外部 */
.filter-buttons {
  display: flex;
  gap: 12px;
  margin-top: 8px;
  margin-left: 0;
}

/* 单行显示的过滤器 */
.filter-row.single-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  width: 100%;
}

.filter-item {
  flex-shrink: 0;
  margin-bottom: 0 !important;
}

/* 搜索操作按钮组 - 始终显示在过滤器按钮下方，右对齐 */
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
  padding: 0;
  width: 100%;
}

.reset-filters-btn,
.feeling-lucky-btn {
  color: #666;
  font-size: 14px;
  padding: 0 16px;
  height: 40px;
  border: none;
  border-radius: 8px;
  background-color: #e0e0e0;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.reset-filters-btn:hover,
.feeling-lucky-btn:hover {
  color: #333;
  background-color: #d0d0d0;
}

/* 搜索按钮 */
.search-button {
  height: 40px;
  min-width: 100px;
  padding: 0 16px;
  font-size: 14px;
  font-weight: 500;
  color: #fff;
  background-color: #ff7eb3;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.search-button:hover {
  background-color: #ff5e99;
}

/* 布局切换器 */
.layout-switcher {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  float: right;
}

:deep(.layout-switcher .el-button-group) {
  border: 1px solid #ccc;
  border-radius: 2px;
  overflow: hidden;
}

:deep(.layout-switcher .el-button) {
  color: #666;
  border: none;
  border-right: 1px solid #ccc;
  background-color: #fff;
  border-radius: 0;
  padding: 6px 12px;
  transition: all 0.2s ease;
}

:deep(.layout-switcher .el-button:last-child) {
  border-right: none;
}

:deep(.layout-switcher .el-button:hover) {
  background-color: #f5f5f5;
  color: #333;
}

:deep(.layout-switcher .el-button.active) {
  background-color: #e6f7ff;
  color: #409eff;
}

/* 广告横幅 */
.ad-banner {
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.ad-image {
  width: 100%;
  height: auto;
  display: block;
}

/* 加载容器 */
.loading-container {
  min-height: 400px;
}

/* 漫画网格 */
.comic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

/* 漫画项 */
.comic-item {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.2s ease;
}

.comic-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

/* 漫画封面 */
.comic-cover-wrapper {
  width: 100%;
  height: 360px;
  overflow: hidden;
  position: relative;
}

.comic-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.comic-item:hover .comic-cover {
  transform: scale(1.05);
}

/* 漫画详情 */
.comic-details {
  padding: 16px;
}

.comic-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  line-height: 1.4;
}

/* 漫画标签 */
.comic-tags {
  margin-bottom: 12px;
}

.comic-tag {
  display: inline-block;
  font-size: 12px;
  color: #666;
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 12px;
  margin-right: 8px;
  margin-bottom: 8px;
}

/* 漫画元数据 */
.comic-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  font-size: 14px;
  color: #666;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

:deep(.rating .el-icon-star-on) {
  color: #e6a23c;
  font-size: 14px;
}

.status-badge {
  padding: 2px 8px;
  background-color: #e6f7ff;
  color: #409eff;
  border-radius: 12px;
  font-size: 12px;
}

/* 漫画描述 */
.comic-description {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 空状态 */
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  color: #999;
}

/* 分页容器 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

:deep(.el-pagination) {
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input-wrapper {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-buttons {
    justify-content: center;
  }
  
  .comic-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
  
  .comic-cover-wrapper {
    height: 280px;
  }
}

</style>