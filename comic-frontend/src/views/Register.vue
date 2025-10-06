<template>
  <div class="register-container">
    <!-- 注册卡片 -->
    <el-card shadow="hover" style="width: 500px; margin: 80px auto;">
      <template #header>
        <h2 class="title">漫画网站 - 用户注册</h2>
      </template>

      <!-- 注册表单（带实时校验） -->
      <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          label-width="120px"
          class="register-form"
      >
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="请输入3-20位字母/数字"
              maxlength="20"
          ></el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input
              type="password"
              v-model="registerForm.password"
              placeholder="请输入6-20位密码"
              maxlength="20"
          ></el-input>
        </el-form-item>

        <!-- 昵称 -->
        <el-form-item label="昵称" prop="nickname">
          <el-input
              v-model="registerForm.nickname"
              placeholder="请输入2-10位昵称"
              maxlength="10"
          ></el-input>
        </el-form-item>

        <!-- 邮箱 -->
        <el-form-item label="邮箱" prop="email">
          <el-input
              v-model="registerForm.email"
              placeholder="请输入有效邮箱（如xxx@xxx.com）"
          ></el-input>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item class="btn-group">
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button @click="goToLogin" style="margin-left: 15px;">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
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

// 注册按钮点击事件
const handleRegister = async () => {
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
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-top: 20px;
}

.title {
  text-align: center;
  color: #333;
}

.register-form {
  margin-top: 20px;
}

.btn-group {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
</style>