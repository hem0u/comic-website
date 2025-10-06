<template>
  <div class="login-container">
    <el-card shadow="hover" style="width: 400px; margin: 100px auto;">
      <template #header>
        <h2>漫画网站 - 登录</h2>
      </template>
      <el-form :model="loginForm" label-width="80px" @submit.prevent="handleLogin">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button @click="goToRegister" style="margin-left: 10px;">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';
import { login } from '../api/user';

// 登录表单数据
const loginForm = ref({
  username: '',
  password: ''
});

const goToRegister = () => {
  // 清空登录表单
  loginForm.value.username = '';
  loginForm.value.password = '';
  // 跳转注册页
  router.push('/register');
};

const router = useRouter();
const userStore = useUserStore();

// 登录处理
const handleLogin = async () => {
  try {
    const res = await login(loginForm.value);
    // 保存用户信息到Pinia
    userStore.setUserInfo(res.data.token, res.data.username);
    // 登录成功跳转个人中心
    router.push('/user-center');
  } catch (error) {
    console.error('登录失败', error);
  }
};
</script>

<style scoped>
.login-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>