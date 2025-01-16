<template>
    <div class="forgot-password-container">
      <div class="forgot-password-box">
        <h2>忘記密碼</h2>
        <form @submit.prevent="requestPasswordReset">
          <div>
            <label for="email">電子郵件</label>
            <input
              v-model="email"
              type="email"
              id="email"
              placeholder="請輸入您的電子郵件"
              required
            />
          </div>
          <button type="submit">提交</button>
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
        email: '',
        showAlert: false,
        alertMessage: '',
      };
    },
    methods: {
      async requestPasswordReset() {
        if (!this.isEmailValid()) {
          this.alertMessage = '請輸入有效的電子郵件地址';
          this.showAlert = true;
          return;
        }
  
        try {
          await axios.post('http://localhost:8080/api/auth/forgot-password', {
            email: this.email,
          });
          this.alertMessage = '密碼重置請求已發送，請檢查您的電子郵件';
          this.showAlert = true;
        } catch (error) {
          console.error('Password reset request failed', error);
          this.alertMessage = '請求失敗，請稍後再試';
          this.showAlert = true;
        }
      },
      isEmailValid() {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(this.email);
      },
    },
  };
  </script>
  
  <style scoped>
  .forgot-password-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f0f2f5;
  }
  
  .forgot-password-box {
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
  }
  
  .alert-box button {
    margin-top: 1rem;
  }
  </style>