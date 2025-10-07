<template>
  <div class="register-container">
    <!-- 注册卡片 -->
    <el-card shadow="never" style="max-width: 430px;">
      <!-- 品牌标识 -->
      <div class="brand">
        <div class="brand-icon"></div>
        <h2 class="title">注册账号</h2>
      </div>

      <!-- 注册表单（带实时校验） -->
      <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          label-position="top"
          label-width="0"
          class="register-form"
      >
        <!-- 用户名 -->
        <el-form-item prop="username" class="compact-form-item no-error-text">
          <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              prefix-icon="User"
              maxlength="20"
              @input="checkFormValid"
          ></el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password" class="compact-form-item no-error-text">
          <el-input
              type="password"
              v-model="registerForm.password"
              placeholder="密码"
              prefix-icon="Lock"
              maxlength="20"
              @input="checkFormValid"
          ></el-input>
        </el-form-item>

        <!-- 昵称 -->
        <el-form-item prop="nickname" class="compact-form-item no-error-text">
          <el-input
              v-model="registerForm.nickname"
              placeholder="昵称"
              prefix-icon="User"
              maxlength="10"
              @input="checkFormValid"
          ></el-input>
        </el-form-item>

        <!-- 邮箱 -->
        <el-form-item prop="email" class="compact-form-item no-error-text">
          <el-input
              v-model="registerForm.email"
              placeholder="邮箱"
              prefix-icon="Message"
              @input="checkFormValid"
          ></el-input>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item class="compact-form-item btn-form-item">
          <el-button 
              type="primary" 
              @click="handleRegister"
              :disabled="!isFormValid"
              :class="isFormValid ? 'login-btn-active' : 'login-btn-disabled'"
              style="width: 100%;"
          >
            注册
          </el-button>
          <div class="help-link">
            <el-link href="#" @click.prevent="goToLogin" class="help-link-text">
              已有账号？去登录
            </el-link>
          </div>
        </el-form-item>
      </el-form>
        
        <!-- 条款与隐私 -->
        <div class="terms">
          <p>
            选择"注册"，即表示您将创建漫画星球账户，并同意
            <span class="term-link">条款</span>
            和
            <span class="term-link">隐私政策</span>
          </p>
        </div>
      </el-card>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { register } from '../api/user';

// 表单引用（用于手动校验）
const registerFormRef = ref(null);

const goToLogin = () => {
  // 清空注册表单
  registerFormRef.value.resetFields();
  // 跳转登录页
  router.push('/login');
};

// 注册表单数据（与后端UserRegisterDTO字段对应）
const registerForm = ref({
  username: '',    // 用户名
  password: '',    // 密码
  nickname: '',    // 昵称
  email: ''        // 邮箱
});

// 表单是否有效
const isFormValid = ref(false);

// 表单校验规则（前端先校验，减少后端请求）
const registerRules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{3,20}$/, message: '用户名需3-20位字母/数字', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码需6-20位', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '昵称需2-10位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效邮箱格式', trigger: 'blur' }
  ]
});

const router = useRouter();

// 检查表单是否有效
const checkFormValid = () => {
  // 简单检查各字段是否填写且格式基本符合要求
  const { username, password, nickname, email } = registerForm.value;
  const usernameRegex = /^[a-zA-Z0-9]{3,20}$/;
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  
  isFormValid.value = username.trim() !== '' && usernameRegex.test(username) &&
                    password.trim() !== '' && password.length >= 6 &&
                    nickname.trim() !== '' && nickname.length >= 2 &&
                    email.trim() !== '' && emailRegex.test(email);
};

// 监听表单变化，实时更新按钮状态
watch(
  () => [registerForm.value.username, registerForm.value.password, registerForm.value.nickname, registerForm.value.email],
  () => {
    checkFormValid();
  },
  { immediate: true }
);

// 注册按钮点击事件
const handleRegister = async () => {
  if (!isFormValid.value) return;
  
  // 1. 先做前端表单校验
  const valid = await registerFormRef.value.validate();
  if (!valid) return;

  try {
    // 2. 调用后端注册接口
    await register(registerForm.value);

    // 3. 注册成功：提示并跳转登录页
    ElMessage.success('注册成功！请登录');
    router.push('/login');
  } catch (error) {
    // 4. 注册失败：后端返回错误提示（如“用户名已存在”）
    console.error('注册失败', error);
  }
};
</script>

<style scoped>
.register-container {
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
  padding-bottom: 40px;
}

.title {
    text-align: center;
    color: #333;
    font-size: 24px;
    margin-bottom: 5px;
    /* 移除标题下划线 */
    position: relative;
    border: none !important;
  }
  /* 品牌标识样式 - 与登录页保持一致 */
  .brand {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
  }
  .brand-icon {
    width: 48px;
    height: 48px;
    background: #000;
    border-radius: 6px;
    margin-bottom: 10px;
  }
  /* 确保标题样式 */
  .brand .title {
    margin: 0;
    font-size: 24px;
    color: #333;
    font-weight: 600;
  }
  /* 彻底覆盖Element UI卡片标题下的边框线 */
  .el-card__header {
    border-bottom: none !important;
    padding-bottom: 0 !important;
  }
  /* 穿透shadow DOM移除Element UI卡片标题的下划线 */
  :deep(.el-card__header) {
    border-bottom: none !important;
    padding-bottom: 0 !important;
  }
  /* 移除可能的伪元素下划线 */
  .title::after {
    display: none !important;
  }
  .el-card__header::after {
    display: none !important;
  }

  /* 表单相关样式 - 与登录页保持一致 */
  .register-form {
    margin-top: 0;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  /* 隐藏表单标签 */
  :deep(.el-form-item__label) {
    display: none;
  }
  
  /* 确保表单项宽度 */
  :deep(.el-form-item) {
    width: 100% !important;
    margin-bottom: 10px !important;
  }
  
  /* 确保表单项内容宽度 */
  :deep(.el-form-item__content) {
    width: 100% !important;
    margin-left: 0 !important;
  }
  
  /* 输入框样式 */
  :deep(.el-input) {
    width: 100% !important;
    margin: 0 auto;
    display: block;
  }

.compact-form-item {
  margin-bottom: 10px;
}

.no-error-text :deep(.el-form-item__error) {
  display: none;
}

.btn-form-item {
  margin-top: 10px;
}

.login-btn-active {
    background-color: #000000;
    border-color: #000000;
    color: #ffffff;
    height: 40px;
    width: 100%;
  }
  
  .login-btn-disabled {
    background-color: #ccc !important;
    border-color: #ccc !important;
    color: #ffffff !important;
    cursor: not-allowed !important;
    height: 40px !important;
  }
  
  .login-btn-disabled:hover {
    background-color: #ccc !important;
    border-color: #ccc !important;
    color: #ffffff !important;
  }

  /* 底部链接样式 */
  .help-link {
    margin-top: 14px;
    font-size: 13px;
    text-align: center;
    width: 100%;
    display: flex;
    justify-content: center;
  }
  .help-link-text {
    color: #333 !important;
    font-weight: 600 !important;
    --el-link-text-color: #333 !important;
    --el-link-hover-text-color: #333 !important;
    --el-link-underline-height: 1px !important;
    text-underline-offset: 0px !important;
    text-align: center !important;
  }
  .terms {
    margin-top: 20px;
    font-size: 12px;
    color: #5f6368;
    text-align: center;
  }
  .term-link {
    color: #333 !important;
    font-weight: 600 !important;
    cursor: pointer;
  }

.el-card {
  border: none !important;
  box-shadow: none !important;
}

/* 输入框包装器样式 - 与登录页保持一致 */
:deep(.el-input__wrapper) {
  width: 100% !important;
  height: 40px !important;
  border-radius: 4px !important;
  background-color: #f5f5f5 !important;
  border: 1px solid #e0e0e0 !important;
  transition: border-width 0.2s ease-in-out;
  margin: 0 auto;
}

/* 输入框聚焦样式 */
:deep(.el-input__wrapper:focus-within) {
  box-shadow: none !important;
  border: 2px solid #000000 !important;
}

/* 输入框内部样式 */
:deep(.el-input__inner) {
  height: 40px !important;
  line-height: 40px !important;
  font-size: 14px;
}

/* 前缀图标样式 */
:deep(.el-input__prefix) {
  color: #999 !important;
}

/* 聚焦时前缀图标样式 */
:deep(.el-input__wrapper:focus-within .el-input__prefix) {
  color: #000 !important;
}
</style>