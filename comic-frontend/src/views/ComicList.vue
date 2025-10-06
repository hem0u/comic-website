<template>
  <div class="comic-list-container">
    <!-- 页面标题 -->
    <h1 class="page-title">漫画列表</h1>

    <!-- 筛选区域 -->
    <div class="filter-bar">
      <el-select
          v-model="selectedCategoryId"
          placeholder="选择分类"
          class="filter-item"
          @change="handleCategoryChange"
      >
        <el-option value="" label="全部分类"></el-option>
        <el-option
            v-for="category in categoryList"
            :key="category.id"
            :label="category.name"
            :value="category.id.toString()"
        ></el-option>
      </el-select>

      <el-input
          v-model="searchKeyword"
          placeholder="搜索漫画标题或作者"
          class="filter-item"
          clearable
          @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button icon="Search" @click="handleSearch"></el-button>
        </template>
      </el-input>
    </div>

    <!-- 加载状态 -->
    <div
        v-loading="loading"
        class="loading-container"
        element-loading-text="正在加载漫画..."
        element-loading-spinner="el-icon-loading"
    >
      <!-- 漫画列表 -->
      <div class="comic-grid">
        <el-card
            v-for="comic in comicList"
            :key="comic.id"
            class="comic-card"
            shadow="hover"
            @click="goToRead(comic.id)"
        >
          <div class="comic-cover-container">
            <img
                :src="comic.cover || 'https://picsum.photos/300/400'"
                :alt="comic.title || '漫画封面'"
                class="comic-cover"
                loading="lazy"
            >
          </div>
          <div class="comic-info">
            <h3 class="comic-title" @click.stop="goToRead(comic.id)">{{ comic.title || '未知标题' }}</h3>
            <p>作者: {{ comic.authorName || '未知作者' }}</p>
            <p>分类: {{ comic.categoryName || '未分类' }}</p>
            <div class="comic-status">
              <el-tag :type="getStatusType(comic.status)">
                {{ getStatusText(comic.status) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 空状态 -->
      <div v-if="comicList.length === 0 && !loading" class="empty-state">
        <el-empty description="没有找到匹配的漫画"></el-empty>
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
import { ref, onMounted, computed } from 'vue';
import { getComicList, getCategoryList } from '../api/comic';
import { useRouter } from 'vue-router';
import { ElSelect, ElOption, ElInput, ElButton, ElCard, ElTag, ElEmpty, ElPagination, ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';

const router = useRouter();

// 分类列表
const categoryList = ref([]);
// 漫画列表数据
const comicList = ref([]);
// 分页参数
const currentPage = ref(1);
const pageSize = ref(8);
const total = ref(0);
// 筛选参数
const selectedCategoryId = ref('');
const searchKeyword = ref('');
// 加载状态
const loading = ref(false);
// 搜索防抖计时器
const searchTimer = ref(null);

// 分类ID到名称的映射
const categoryMap = computed(() => {
  return categoryList.value.reduce((map, category) => {
    map[category.id] = category.name;
    return map;
  }, {});
});

// 根据分类ID获取名称
const getCategoryName = (categoryId) => {
  return categoryMap.value[categoryId] || '未分类';
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

// 页面挂载时初始化
onMounted(() => {
  getCategoryOptions();
  fetchComicList();
});

// 获取分类列表
const getCategoryOptions = async () => {
  try {
    const res = await getCategoryList();
    categoryList.value = res.data || [];
    if (categoryList.value.length === 0) {
      ElMessage.warning('当前没有可用的分类数据');
    }
  } catch (error) {
    console.error('获取分类失败', error);
    ElMessage.error('获取分类失败，请刷新页面重试');
  }
};

// 获取漫画列表
const fetchComicList = async () => {
  loading.value = true;
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value,
      categoryId: selectedCategoryId.value ? Number(selectedCategoryId.value) : undefined
    };
    const res = await getComicList(params);

    if (!res || !res.data) {
      throw new Error('接口返回格式异常');
    }

    comicList.value = res.data.list || [];
    total.value = res.data.total || 0;

    // 首次加载且无数据时提示
    if (comicList.value.length === 0 && currentPage.value === 1) {
      ElMessage.info('没有找到符合条件的漫画');
    }
  } catch (error) {
    console.error('获取漫画列表失败', error);
    comicList.value = [];
    total.value = 0;
    ElMessage.error('获取漫画失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 分类改变事件
const handleCategoryChange = () => {
  currentPage.value = 1;
  fetchComicList();
};

// 搜索事件（带防抖）
const handleSearch = () => {
  if (searchTimer.value) {
    clearTimeout(searchTimer.value);
  }
  searchTimer.value = setTimeout(() => {
    currentPage.value = 1;
    fetchComicList();
  }, 300);
};

// 分页事件
const handleSizeChange = (val) => {
  if (val < 1) return;
  pageSize.value = val;
  currentPage.value = 1;
  fetchComicList();
};

const handleCurrentChange = (val) => {
  if (val < 1) return;
  currentPage.value = val;
  fetchComicList();
};

// 跳转到阅读页
const goToRead = (comicId) => {
  if (!comicId || isNaN(comicId)) {
    ElMessage.warning('无效的漫画ID');
    return;
  }
  router.push(`/comic-read/${comicId}`);
};
</script>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f5f5f5;
}

::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #bbb;
}

body {
  background-color: #f9f9f9;
  color: #333;
  line-height: 1.6;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}
</style>

<style scoped>
.comic-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  text-align: center;
  margin: 20px 0 30px;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
  align-items: center;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-item {
  flex: 1;
  min-width: 200px;
}

/* 漫画列表 */
.comic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 25px;
  margin-bottom: 40px;
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

.comic-card:active {
  transform: translateY(-2px);
}

.comic-cover-container {
  width: 100%;
  padding-top: 140%; /* 保持封面比例 */
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
  transition: color 0.2s;
}

.comic-title:hover {
  color: #409eff;
}

.comic-author,
.comic-category {
  font-size: 13px;
  color: #666;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.comic-status {
  margin-top: 10px;
}

/* 分页 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin: 30px 0 50px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 0;
  color: #999;
}

/* 加载容器 */
.loading-container {
  position: relative;
  min-height: 400px;
}
</style>
    