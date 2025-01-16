<template>
  <div class="login-container">
    <div class="login-box">
      <h2>Login</h2>
      <form @submit.prevent="login">
        <div>
          <label for="email">Email</label>
          <input
            v-model="email"
            type="email"
            id="email"
            placeholder="請輸入帳號"
            required
          />
        </div>
        <div>
          <label for="password">Password</label>
          <input
            v-model="password"
            type="password"
            id="password"
            placeholder="請輸入密碼"
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      <div class="links">
        <router-link to="/register">註冊</router-link>
        <router-link to="/forgot-password">忘記密碼？</router-link>
      </div>
    </div>

    <!-- 登录成功弹窗 -->
    <div v-if="showSuccessAlert" class="alert-box">
      <p>登入成功！</p>
      <button @click="redirectToProductManagement">確定</button>
    </div>

    <!-- 登录失败弹窗 -->
    <div v-if="showErrorAlert" class="alert-box">
      <p>登入失敗，請檢查帳號和密碼。</p>
      <button @click="showErrorAlert = false">關閉</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      email: '', // 移除默认值
      password: '', // 移除默认值
      showSuccessAlert: false, // 登录成功弹窗
      showErrorAlert: false, // 登录失败弹窗
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('http://localhost:8080/api/auth/login', {
          email: this.email,
          password: this.password,
        });

        // 登录成功
        localStorage.setItem('token', response.data.token); // 保存 token
        this.showSuccessAlert = true; // 显示成功弹窗
      } catch (error) {
        console.error('Login failed', error);
        this.showErrorAlert = true; // 显示失败弹窗
      }
    },
    redirectToProductManagement() {
      this.showSuccessAlert = false; // 关闭弹窗
      this.$router.push('/product-management'); // 跳转到商品管理页面
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

form div {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
}

input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-bottom: 1rem;
}

button:hover {
  background-color: #0056b3;
}

.links {
  display: flex;
  justify-content: space-between;
}

.links a {
  color: #007bff;
  text-decoration: none;
}

.links a:hover {
  text-decoration: underline;
}

.alert-box {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.alert-box button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.alert-box button:hover {
  background-color: #0056b3;
}
</style>