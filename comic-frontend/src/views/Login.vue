<template>
  <div class="login-page">
    <!-- 中间登录核心内容 -->
    <div class="login-container">
      <!-- 品牌标识 -->
      <div class="brand">
        <div class="brand-icon"></div>
        <h2 class="brand-title">登录或注册</h2>
      </div>

      <!-- 第三方登录按钮 -->
      <div class="social-buttons">
        <el-button
            type="text"
            class="social-btn"
            @click="showToast('QQ登录暂未开发')"
        >
          <i class="fab fa-qq qq-icon"></i>
          <span class="btn-text">使用 QQ 账户继续</span>
        </el-button>
        <el-button
            type="text"
            class="social-btn"
            @click="showToast('微信登录暂未开发')"
        >
          <i class="fab fa-weixin wechat-icon"></i>
          <span class="btn-text">使用微信账户继续</span>
        </el-button>
      </div>

      <!-- 分隔线 -->
      <div class="separator">
        <span>或</span>
      </div>

      <!-- 用户名密码登录表单 -->
      <el-form
          :model="loginForm"
          :rules="loginRules"
          ref="loginFormRef"
          @submit.prevent="handleLogin"
          class="login-form"
      >
        <el-form-item label="用户名" prop="username" class="compact-form-item no-error-text">
          <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              prefix-icon="User"
              :disabled="isLoading"
              @input="checkFormComplete"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" class="compact-form-item no-error-text">
          <el-input
              type="password"
              v-model="loginForm.password"
              placeholder="密码"
              prefix-icon="Lock"
              :disabled="isLoading"
              @input="checkFormComplete"
          ></el-input>
        </el-form-item>
        <el-form-item class="compact-form-item btn-form-item">
          <el-button
              type="primary"
              @click="handleLogin"
              :loading="isLoading"
              :disabled="!isFormComplete || isLoading"
              :class="{ 'login-btn-active': isFormComplete && !isLoading }"
          >
            登录
          </el-button>
          <el-button
              type="default"
              @click="goToRegister"
              style="margin-left: 10px;"
              :disabled="isLoading"
              class="register-btn"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 帮助链接 -->
      <div class="help-link">
        <el-link href="#" class="help-link-text">登录时需要帮助？</el-link>
      </div>

      <!-- 条款与隐私 -->
      <div class="terms">
        <p>
          选择“注册”，即表示您将创建漫画星球账户，并同意
          <span class="term-link">条款</span>
          和
          <span class="term-link">隐私政策</span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';
import { login } from '../api/user';
import { ElMessage, ElForm } from 'element-plus';


// 登录表单数据
const loginForm = ref({
  username: '',
  password: ''
});

// 表单是否填写完整
const isFormComplete = ref(false);

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: ' ', trigger: 'blur' },
    { min: 3, message: ' ', trigger: 'blur' }
  ],
  password: [
    { required: true, message: ' ', trigger: 'blur' },
    { min: 6, message: ' ', trigger: 'blur' }
  ]
};

// 表单引用
const loginFormRef = ref(ElForm);

// 状态管理
const isLoading = ref(false);
const router = useRouter();
const userStore = useUserStore();

// 检查表单是否填写完整
const checkFormComplete = () => {
  isFormComplete.value = loginForm.value.username.trim().length >= 3 &&
      loginForm.value.password.trim().length >= 6;
};

// 监听表单变化，实时更新按钮状态
watch(
    () => [loginForm.value.username, loginForm.value.password],
    () => {
      checkFormComplete();
    },
    { immediate: true }
);

// 跳转注册页
const goToRegister = () => {
  loginForm.value.username = '';
  loginForm.value.password = '';
  router.push('/register');
};

// 登录处理
  const handleLogin = async () => {
    try {
      await loginFormRef.value.validate();
    } catch (error) {
      return;
    }

    isLoading.value = true;
    try {
      const res = await login(loginForm.value);
      // 从服务器响应中获取头像信息，如果存在则传递给setUserInfo
      const avatar = res.data.avatar || '';
      userStore.setUserInfo(res.data.token, res.data.username, avatar);
      router.push('/home');
    } catch (error) {
      ElMessage.error(error.response?.data?.msg || '登录失败，请稍后重试');
      console.error('登录失败：', error);
    } finally {
      isLoading.value = false;
    }
  };

// 第三方登录提示
const showToast = (message) => {
  ElMessage.info(message);
};
</script>

<style scoped>
/* 强制隐藏 html 和 body 的滚动条，确保根元素高度不溢出 */
html, body {
  overflow: hidden !important; /* 强制隐藏滚动条，覆盖所有可能的冲突 */
  height: 100% !important; /* 使根元素高度与视口一致 */
  width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
}

/* 外层容器，继承根元素高度，确保内部元素高度可控 */
.login-page {
  height: 100vh !important;
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--el-bg-color-page, #fff); /* 默认白色，支持主题变量 */
  min-width: 1200px;
  overflow: hidden !important;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  padding-bottom: 90px; /* 通过底部填充使内容向上移动20px */
}

/* 背景图容器 */
.side-bg {
  width: 30%;
  height: 100% !important;
  background-size: 60% auto;
  background-position: center;
  background-repeat: no-repeat;
  background-color: var(--el-bg-color-page, #fff); /* 默认白色，支持主题变量 */
  overflow: hidden !important;
  flex-shrink: 0;
}

/* 中间登录容器 */
.login-container {
  width: 100%;
  max-width: 430px;
  padding: 24px;
  text-align: center;
  background-color: var(--el-bg-color-page, #fff); /* 默认白色，支持主题变量 */
  max-height: 100%;
  overflow: hidden !important;
  box-sizing: border-box;
}

/* 左侧背景图路径 */
.side-bg-left {
  background-image: url('../assets/login-left-bg.png');
}

/* 右侧背景图路径 */
.side-bg-right {
  background-image: url('../assets/login-right-bg.png');
}

/* 品牌标识样式 */
.brand {
  margin-bottom: 20px;
}
.brand-icon {
  width: 48px;
  height: 48px;
  background: #000;
  border-radius: 6px;
  margin: 0 auto 10px;
}
.brand-title {
  font-size: 26px;
  font-weight: 600;
  color: #333;
}

/* 第三方按钮区域 */
.social-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
}
.social-btn {
  width: 100% !important;
  height: 40px !important;
  border: 1px solid var(--el-border-color, #dadce0) !important;
  border-radius: 4px !important;
  font-size: 14px !important;
  background: var(--el-bg-color, #fff) !important;
  padding: 0 16px !important;
  margin: 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  box-sizing: border-box !important;
}
.social-btn i {
  width: 22px !important;
  height: 22px !important;
  margin-right: 8px !important;
  margin-top: 7px !important;
  flex-shrink: 0 !important;
}
.btn-text {
  text-align: center;
  flex: 1;
  color: #000 !important;
  font-weight: 600 !important;
}
.qq-icon {
  color: #12b7f5 !important;
}
.wechat-icon {
  color: #07c160 !important;
}

/* 表单相关样式 */
:deep(.el-form-item__label) {
  display: none;
}
.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
:deep(.el-form-item) {
  width: 100% !important;
  margin-bottom: 10px !important;
}
:deep(.el-form-item__content) {
  width: 100% !important;
  margin-left: 0 !important;
}
:deep(.el-input) {
  width: 100% !important;
  margin: 0 auto;
  display: block;
}
:deep(.el-input__wrapper) {
  height: 40px !important;
  width: 100% !important;
  border-radius: 4px !important;
  background-color: var(--el-input-bg-color, #f5f5f5) !important;
  border: 1px solid var(--el-input-border-color, #e0e0e0) !important;
  transition: border-width 0.2s ease-in-out;
}
:deep(.el-input__wrapper:focus-within) {
  border: 2px solid #000 !important;
  box-shadow: none !important;
}
:deep(.el-input__prefix) {
  color: #999 !important;
}
:deep(.el-input__wrapper:focus-within .el-input__prefix) {
  color: #000 !important;
}
:deep(.no-error-text .el-form-item__error) {
  display: none !important;
  height: 0 !important;
  margin-top: 0 !important;
}
.compact-form-item {
  margin-bottom: 10px !important;
}
.btn-form-item {
  margin-top: 10px !important;
}

/* 分隔线样式 */
.separator {
  margin: 16px 0;
  position: relative;
}
.separator::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background-color: var(--el-border-color, #e0e0e0);
  z-index: 0;
}
.separator span {
  display: inline-block;
  padding: 0 10px;
  background: var(--el-bg-color-page, #fff);
  position: relative;
  z-index: 1;
  font-size: 16px;
  color: var(--el-text-color-secondary, #999);
  font-weight: 500;
}

/* 按钮样式 */
.el-form-item:last-child .el-form-item__content {
  display: flex;
  gap: 10px;
  width: 100%;
  margin: 0 auto;
}
.el-form-item:last-child .el-button {
  flex: 1;
  height: 40px;
  width: 195px;
  border-radius: 4px;
}
:deep(.el-button--primary) {
  background-color: #ccc !important;
  border-color: #ccc !important;
  color: #fff !important;
  cursor: not-allowed !important;
  font-weight: 500 !important;
}
:deep(.login-btn-active) {
  background-color: #333 !important;
  border-color: #333 !important;
  color: #fff !important;
  cursor: pointer !important;
}
:deep(.register-btn) {
  border-color: #ddd !important;
  color: #333 !important;
  background-color: #fff !important;
}
:deep(.register-btn:focus-visible),
:deep(.register-btn:hover) {
  color: #ff7eb3 !important;
  border-color: #ff7eb3 !important;
  background-color: #fff !important;
  outline: none !important;
}

/* 底部链接样式 */
.help-link {
  margin-top: 14px;
  font-size: 13px;
}
.help-link-text {
  color: #333 !important;
  font-weight: 600 !important;
  --el-link-text-color: #333 !important;
  --el-link-hover-text-color: #333 !important;
}
.terms {
  margin-top: 20px;
  font-size: 12px;
  color: #5f6368;
}
.term-link {
  color: #333 !important;
  font-weight: 600 !important;
  cursor: pointer;
}

/* 移除默认按钮hover样式 */
:deep(.el-button--text:hover) {
  background-color: transparent !important;
}

/* 清除浏览器默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
/* 暗色主题样式 - 超级优先级覆盖 */
html.el-theme-dark .login-page {
  background: var(--el-bg-color-page) !important;
}

html.el-theme-dark .login-container {
  background: var(--el-bg-color-page) !important; /* 统一容器内外背景 */
}

html.el-theme-dark .brand-title {
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .social-btn {
  background: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
}

html.el-theme-dark .btn-text {
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark .separator::before {
  background-color: var(--el-border-color) !important;
}

html.el-theme-dark .separator span {
  background: var(--el-bg-color-page) !important; /* 与容器背景一致 */
  color: var(--el-text-color-secondary) !important;
}

/* 超级优先级输入框样式覆盖 - 使用复合选择器和!important确保完全覆盖 */
html.el-theme-dark :deep(.el-input),
html.el-theme-dark :deep(.el-input__wrapper),
html.el-theme-dark :deep(.el-input-group),
html.el-theme-dark :deep(.el-input-group__append),
html.el-theme-dark :deep(.el-input-group__prepend) {
  --el-input-bg-color: var(--el-bg-color) !important;
  --el-input-text-color: var(--el-text-color-primary) !important;
  --el-input-border-color: var(--el-border-color) !important;
  --el-input-focus-border-color: var(--el-color-primary) !important;
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
  box-shadow: none !important;
}

/* 确保输入框各个状态都被覆盖 */
html.el-theme-dark :deep(.el-input__wrapper:hover) {
  border-color: var(--el-color-primary) !important;
}

html.el-theme-dark :deep(.el-input__wrapper:focus-within) {
  border-color: var(--el-color-primary) !important;
  box-shadow: 0 0 0 2px rgba(var(--el-color-primary-rgb), 0.2) !important;
}

/* 输入框内部元素 */
html.el-theme-dark :deep(.el-input__inner) {
  color: var(--el-text-color-primary) !important;
  background-color: transparent !important;
}

html.el-theme-dark :deep(.el-input__prefix),
html.el-theme-dark :deep(.el-input__suffix) {
  color: var(--el-text-color-secondary) !important;
}

html.el-theme-dark :deep(.el-input__wrapper:focus-within .el-input__prefix),
html.el-theme-dark :deep(.el-input__wrapper:focus-within .el-input__suffix) {
  color: var(--el-color-primary) !important;
}

/* 超级优先级按钮样式覆盖 */
html.el-theme-dark :deep(.el-button),
html.el-theme-dark :deep(.el-button--default) {
  --el-button-bg-color: var(--el-bg-color) !important;
  --el-button-text-color: var(--el-text-color-primary) !important;
  --el-button-border-color: var(--el-border-color) !important;
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
}

/* 主按钮 */
html.el-theme-dark :deep(.el-button--primary) {
  --el-button-bg-color: var(--el-color-primary) !important;
  --el-button-text-color: white !important;
  --el-button-border-color: var(--el-color-primary) !important;
  background-color: var(--el-color-primary) !important;
  border-color: var(--el-color-primary) !important;
  color: white !important;
}

/* 登录按钮激活状态 */
html.el-theme-dark :deep(.login-btn-active) {
  --el-button-bg-color: var(--el-color-primary) !important;
  --el-button-text-color: white !important;
  --el-button-border-color: var(--el-color-primary) !important;
  background-color: var(--el-color-primary) !important;
  border-color: var(--el-color-primary) !important;
  color: white !important;
}

/* 注册按钮 - 超高优先级选择器 */
html.el-theme-dark :deep(.el-button.el-button--default.register-btn),
html.el-theme-dark :deep(.register-btn) {
  --el-button-bg-color: var(--el-bg-color) !important;
  --el-button-text-color: var(--el-text-color-primary) !important;
  --el-button-border-color: var(--el-border-color) !important;
  background-color: var(--el-bg-color) !important;
  border-color: var(--el-border-color) !important;
  color: var(--el-text-color-primary) !important;
}

html.el-theme-dark :deep(.register-btn:hover) {
  --el-button-hover-bg-color: var(--el-bg-color-soft) !important;
  --el-button-hover-text-color: var(--el-text-color-primary) !important;
  --el-button-hover-border-color: var(--el-color-primary) !important;
  border-color: var(--el-color-primary) !important;
  color: var(--el-color-primary) !important;
  background-color: var(--el-bg-color) !important;
}

html.el-theme-dark :deep(.register-btn:hover) {
  border-color: var(--el-color-primary) !important;
  color: var(--el-color-primary) !important;
  background-color: var(--el-bg-color) !important;
}

/* 表单容器样式 */
html.el-theme-dark :deep(.el-form) {
  --el-form-item-label-color: var(--el-text-color-primary) !important;
}

html.el-theme-dark :deep(.el-form-item) {
  --el-form-item-label-color: var(--el-text-color-primary) !important;
}

html.el-theme-dark :deep(.el-form-item__content) {
  background-color: transparent !important;
}

html.el-theme-dark .help-link-text {
  color: var(--el-text-color-secondary) !important;
  --el-link-text-color: var(--el-text-color-secondary) !important;
  --el-link-hover-text-color: var(--el-color-primary) !important;
}

html.el-theme-dark .terms {
  color: var(--el-text-color-placeholder) !important;
}

html.el-theme-dark .term-link {
  color: var(--el-color-primary) !important;
}
</style>