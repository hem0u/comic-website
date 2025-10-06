<template>
  <div class="comic-read-container">
    <!-- 顶部导航栏 -->
    <div class="read-header">
      <!-- 返回列表按钮 -->
      <el-button
          icon="ArrowLeft"
          type="text"
          @click="$router.push('/comic-list')"
          class="back-btn"
      >
        返回漫画列表
      </el-button>

      <!-- 漫画标题 -->
      <h2 class="comic-title">{{ comicDetail.title }}</h2>

      <!-- 章节选择下拉框 -->
      <el-select
          v-model="selectedChapterId"
          placeholder="选择章节"
          style="width: 200px;"
          @change="switchChapter"
      >
        <el-option
            v-for="chapter in chapterList"
            :key="chapter.id"
            :label="chapter.title"
            :value="chapter.id"
        ></el-option>
      </el-select>
    </div>

    <!-- 阅读主体区域 -->
    <div class="read-content">
      <!-- 加载中提示 -->
      <el-loading v-if="loading" fullscreen text="加载漫画中..."></el-loading>

      <!-- 图片列表 -->
      <img
          v-for="(image, index) in currentChapterImages"
          :key="index"
          :src="baseUrl + image.url"
          alt="漫画图片"
          class="comic-image"
          @load="handleImageLoad"
      >

      <!-- 空数据提示 -->
      <el-empty
          v-if="!loading && currentChapterImages.length === 0"
          description="当前章节暂无图片"
          class="empty-tip"
      ></el-empty>
    </div>

    <!-- 底部翻页控制栏 -->
    <div class="read-footer" v-if="currentChapterImages.length > 0">
      <el-button
          icon="ArrowLeft"
          :disabled="!hasPrevChapter"
          @click="goToPrevChapter"
      >
        上一话
      </el-button>
      <span class="page-info">
        第{{ currentChapterIndex + 1 }}话 / 共{{ chapterList.length }}话
      </span>
      <el-button
          icon="ArrowRight"
          :disabled="!hasNextChapter"
          @click="goToNextChapter"
      >
        下一话
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage, ElLoading } from 'element-plus';
import { getComicDetail } from '../api/comic';
import { recordReadHistory } from '../api/history';

// 路由实例（获取当前漫画ID）
const route = useRoute();
const comicId = ref(route.params.id);  // 从路由参数获取漫画ID
const baseUrl = 'http://localhost:8080'; // 后端服务地址

// 状态管理
const loading = ref(true);                // 加载状态
const comicDetail = ref({});              // 漫画详情（标题、作者等）
const chapterList = ref([]);              // 章节列表
const selectedChapterId = ref('');        // 当前选中的章节ID
const currentChapterImages = ref([]);     // 当前章节的图片列表
const currentChapterIndex = ref(0);       // 当前章节在列表中的索引

// 计算属性：是否有上一话/下一话
const hasPrevChapter = computed(() => currentChapterIndex.value > 0);
const hasNextChapter = computed(() => currentChapterIndex.value < chapterList.value.length - 1);

// 页面挂载时加载漫画数据
onMounted(() => {
  loadComicData();
});

// 1. 加载漫画详情和章节列表
const loadComicData = async () => {
  loading.value = true;
  try {
    // 调用后端接口获取漫画详情（含章节列表）
    const res = await getComicDetail(comicId.value);
    comicDetail.value = res.data;
    chapterList.value = res.data.chapters || [];

    // 若有章节，默认选中第一话并加载图片
    if (chapterList.value.length > 0) {
      selectedChapterId.value = chapterList.value[0].id;
      currentChapterIndex.value = 0;
      loadChapterImages(selectedChapterId.value);
    }
  } catch (error) {
    ElMessage.error('加载漫画失败，请重试');
    console.error('加载漫画详情错误', error);
  } finally {
    loading.value = false;
  }
};

// 2. 加载指定章节的图片
const loadChapterImages = async (chapterId) => {
  const currentChapter = chapterList.value.find(ch => ch.id === chapterId);
  if (currentChapter) {
    // 从章节数据中获取图片列表（后端返回的章节已包含images字段）
    console.log('当前章节图片列表', currentChapter.images);
    currentChapterImages.value = currentChapter.images || [];
    try {
      // 记录阅读历史（调用后端接口）
      await recordReadHistory(comicId.value, chapterId);
    } catch (error) {
      if (error.response && error.response.data && error.response.data.msg === '用户不存在') {
        ElMessage.error('用户不存在，无法记录阅读历史');
      } else {
        ElMessage.error('记录阅读历史失败，请重试');
      }
      console.error('记录阅读历史错误', error);
    }
  }
};

// 3. 切换章节（下拉框选择）
const switchChapter = (chapterId) => {
  // 找到选中章节的索引
  const index = chapterList.value.findIndex(ch => ch.id === chapterId);
  if (index !== -1) {
    currentChapterIndex.value = index;
    loadChapterImages(chapterId);
    // 滚动到页面顶部
    window.scrollTo(0, 0);
  }
};

// 4. 上一话
const goToPrevChapter = () => {
  if (hasPrevChapter.value) {
    const prevIndex = currentChapterIndex.value - 1;
    selectedChapterId.value = chapterList.value[prevIndex].id;
    currentChapterIndex.value = prevIndex;
    loadChapterImages(selectedChapterId.value);
    window.scrollTo(0, 0);
  }
};

// 5. 下一话
const goToNextChapter = () => {
  if (hasNextChapter.value) {
    const nextIndex = currentChapterIndex.value + 1;
    selectedChapterId.value = chapterList.value[nextIndex].id;
    currentChapterIndex.value = nextIndex;
    loadChapterImages(selectedChapterId.value);
    window.scrollTo(0, 0);
  }
};

// 6. 图片加载完成处理（可选：优化体验）
const handleImageLoad = (e) => {
  console.log('图片加载完成');
  e.target.style.opacity = 1;  // 图片加载完成后显示（避免模糊过渡）
};
</script>

<style scoped>
.comic-read-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航栏样式 */
.read-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 10;
}

.back-btn {
  color: #1890ff;
  font-size: 14px;
}

.comic-title {
  font-size: 18px;
  margin: 0;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 50%;
}

/* 阅读主体区域样式 */
.read-content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px 0;
}

.image-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.comic-image {
  width: 100%;
  border-radius: 4px;
  opacity: 0;  /* 初始透明，加载完成后显示 */
  transition: opacity 0.3s ease;
}

.empty-tip {
  padding: 80px 0;
}

/* 底部翻页控制栏样式 */
.read-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 -2px 4px rgba(0,0,0,0.05);
}

.page-info {
  font-size: 14px;
  color: #666;
}
</style>