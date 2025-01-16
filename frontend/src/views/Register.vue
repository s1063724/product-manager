<template>
  <div class="register-container">
    <div class="register-box">
      <h2>Register</h2>
      <form @submit.prevent="register">
        <div>
          <label for="username">用戶名稱</label>
          <input
            v-model="username"
            type="text"
            id="username"
            placeholder="請輸入用戶名稱"
            required
          />
        </div>
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
        <button type="submit">Register</button>
      </form>
      <div class="links">
        <router-link to="/login">返回登入</router-link>
      </div>
    </div>

    <!-- 弹窗提示 -->
    <div v-if="showAlert" class="alert-box">
      <p>{{ alertMessage }}</p>
      <button @click="showAlert = false">關閉</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '', // 新增：用戶名稱
      email: '',
      password: '',
      showAlert: false,
      alertMessage: '',
    };
  },
  methods: {
    async register() {
      if (!this.username || !this.email || !this.password) {
        this.alertMessage = '請填寫所有字段';
        this.showAlert = true;
        return;
      }

      if (!this.isPasswordValid()) {
        this.showAlert = true;
        return;
      }

      try {
        await axios.post('http://localhost:8080/api/auth/register', {
          username: this.username, // 新增：用戶名稱
          email: this.email,
          password: this.password,
        });
        alert('註冊完成');
        this.$router.push('/login');
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.alertMessage = error.response.data;
          this.showAlert = true;
        } else {
          console.error('Registration failed', error);
          this.alertMessage = '註冊失敗，請重試';
          this.showAlert = true;
        }
      }
    },
    isPasswordValid() {
      if (this.password.length < 8) {
        this.alertMessage = '密碼長度至少為 8 個字符';
        return false;
      }

      if (this.password.charAt(0) !== this.password.charAt(0).toUpperCase()) {
        this.alertMessage = '密碼首字母必須為大寫';
        return false;
      }

      if (!/\d/.test(this.password)) {
        this.alertMessage = '密碼必須包含至少一個數字';
        return false;
      }


      return true;
    },
  },
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.register-box {
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
  text-align: center;
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
  z-index: 1000;
}

.alert-box p {
  margin-bottom: 1rem;
  font-size: 1.1rem;
  color: #333;
}

.alert-box button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

.alert-box button:hover {
  background-color: #0056b3;
}
</style>