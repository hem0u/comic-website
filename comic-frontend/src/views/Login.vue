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
    userStore.setUserInfo(res.data.token, res.data.username);
    router.push('/user-center');
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
  background: #fff;
  min-width: 1200px;
  overflow: hidden !important;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 背景图容器 */
.side-bg {
  width: 30%;
  height: 100% !important;
  background-size: 60% auto;
  background-position: center;
  background-repeat: no-repeat;
  background-color: #fff;
  overflow: hidden !important;
  flex-shrink: 0;
}

/* 中间登录容器 */
.login-container {
  width: 100%;
  max-width: 400px;
  padding: 24px;
  text-align: center;
  background-color: #fff;
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
  border: 1px solid #dadce0 !important;
  border-radius: 4px !important;
  font-size: 14px !important;
  background: #fff !important;
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
}
:deep(.el-input) {
  width: 100% !important;
}
:deep(.el-input__wrapper) {
  height: 40px !important;
  border-radius: 4px !important;
  background-color: #f5f5f5 !important;
  border: 1px solid #e0e0e0 !important;
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
  background-color: #e0e0e0;
  z-index: 0;
}
.separator span {
  display: inline-block;
  padding: 0 10px;
  background: #fff;
  position: relative;
  z-index: 1;
  font-size: 16px;
  color: #999;
  font-weight: 500;
}

/* 按钮样式 */
.el-form-item:last-child .el-form-item__content {
  display: flex;
  gap: 8px;
}
.el-form-item:last-child .el-button {
  flex: 1;
  height: 40px;
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
</style>