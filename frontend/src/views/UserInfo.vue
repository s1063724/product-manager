<template>
    <div class="app-container">
      <!-- 側邊選單 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h2>商品管理系統</h2>
        </div>
        <ul class="sidebar-menu">
          <li>
            <router-link to="/product-management" class="menu-item">
              <span>Product Manager</span>
            </router-link>
          </li>
          <li>
            <router-link to="/user-info" class="menu-item">
              <span>用戶訊息</span>
            </router-link>
          </li>
          <li>
            <router-link to="/log" class="menu-item">
              <span>Log</span>
            </router-link>
          </li>
        </ul>
      </div>
  
      <!-- 主內容區域 -->
      <div class="main-content">
        <!-- 右上角信箱和登出按鈕 -->
        <div class="header">
          <span class="user-email">{{ user.email }}</span>
          <button @click="logout" class="logout-button">登出</button>
        </div>
  
        <div class="user-info-container">
          <h2>帳號訊息</h2>
          <form @submit.prevent="updateEmail">
            <div class="form-group">
              <label for="email">信箱</label>
              <input
                v-model="user.email"
                type="email"
                id="email"
                placeholder="請輸入信箱"
                required
                class="wide-input"
              />
            </div>
            <button type="submit" :disabled="isLoading" class="submit-button">
              <span v-if="isLoading" class="loading-spinner"></span>
              <span v-else>更新信箱</span>
            </button>
          </form>
  
          <!-- 修改密碼按鈕 -->
          <div class="change-password-section">
            <button @click="togglePasswordForm" class="change-password-button">
              {{ showPasswordForm ? '隱藏修改密碼' : '修改密碼' }}
            </button>
  
            <!-- 修改密碼表單 -->
            <form v-if="showPasswordForm" @submit.prevent="updatePassword" class="password-form">
              <div class="form-group">
                <label for="currentPassword">當前密碼</label>
                <input
                  v-model="password.currentPassword"
                  type="password"
                  id="currentPassword"
                  placeholder="請輸入當前密碼"
                  required
                  class="wide-input"
                />
              </div>
              <div class="form-group">
                <label for="newPassword">新密碼</label>
                <input
                  v-model="password.newPassword"
                  type="password"
                  id="newPassword"
                  placeholder="請輸入新密碼"
                  required
                  class="wide-input"
                />
              </div>
              <div class="form-group">
                <label for="confirmPassword">確認新密碼</label>
                <input
                  v-model="password.confirmPassword"
                  type="password"
                  id="confirmPassword"
                  placeholder="請再次輸入新密碼"
                  required
                  class="wide-input"
                />
              </div>
              <button type="submit" :disabled="isLoading" class="submit-button">
                <span v-if="isLoading" class="loading-spinner"></span>
                <span v-else>修改密碼</span>
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </template>
 <script>
 import axios from 'axios';
 
 export default {
   data() {
     return {
       user: {
         email: '', // 信箱
       },
       password: {
         currentPassword: '',
         newPassword: '',
         confirmPassword: '',
       },
       isLoading: false,
       showPasswordForm: false, // 控制修改密碼表單的顯示
     };
   },
   methods: {
     async fetchUserInfo() {
       try {
         const response = await axios.get('http://localhost:8081/api/user-info', {
           headers: {
             Authorization: `Bearer ${localStorage.getItem('token')}`,
           },
         });
         console.log('API 回應:', response.data); // 打印回應內容
         this.user.email = response.data.email; // 更新信箱
       } catch (error) {
         console.error('獲取用戶訊息失敗', error);
         alert('獲取用戶訊息失敗，請重試');
       }
     },
     async updateEmail() {
       this.isLoading = true;
       try {
         const response = await axios.put('http://localhost:8081/api/user-info/email', {
           email: this.user.email,
         });
         alert('信箱更新成功');
       } catch (error) {
         console.error('更新信箱失敗', error);
         alert('更新信箱失敗，請重試');
       } finally {
         this.isLoading = false;
       }
     },
     async updatePassword() {
       if (this.password.newPassword !== this.password.confirmPassword) {
         alert('新密碼與確認密碼不一致');
         return;
       }
 
       this.isLoading = true;
       try {
         const response = await axios.put('http://localhost:8081/api/user-info/password', {
           currentPassword: this.password.currentPassword,
           newPassword: this.password.newPassword,
         });
         alert('密碼更新成功');
         this.showPasswordForm = false; // 密碼更新成功後隱藏表單
       } catch (error) {
         console.error('更新密碼失敗', error);
         alert('更新密碼失敗，請重試');
       } finally {
         this.isLoading = false;
       }
     },
     togglePasswordForm() {
       this.showPasswordForm = !this.showPasswordForm; // 切換表單顯示狀態
     },
     logout() {
       localStorage.removeItem('token'); // 假設使用 localStorage 存儲 token
       this.$router.push('/login'); // 跳轉到登錄頁面
     },
   },
   mounted() {
     this.fetchUserInfo(); // 組件加載時獲取用戶訊息
   },
 };
 </script>
  
  <style scoped>
  .app-container {
    display: flex;
    min-height: 100vh;
  }
  
  .sidebar {
    width: 250px;
    background: #2c3e50;
    color: white;
    padding: 20px;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  }
  
  .sidebar-header {
    margin-bottom: 20px;
    text-align: center;
  }
  
  .sidebar-menu {
    list-style: none;
    padding: 0;
  }
  
  .sidebar-menu li {
    margin-bottom: 10px;
  }
  
  .menu-item {
    display: block;
    padding: 10px;
    color: white;
    text-decoration: none;
    border-radius: 4px;
    transition: background-color 0.3s;
  }
  
  .menu-item:hover {
    background-color: #34495e;
  }
  
  .main-content {
    flex: 1;
    padding: 20px;
    background: #f8f9fa;
  }
  
  .header {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .user-username {
    margin-right: 15px;
    font-size: 16px;
    color: #333;
  }
  
  .logout-button {
    padding: 8px 16px;
    background-color: #ff4d4f;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .logout-button:hover {
    background-color: #ff7875;
  }
  
  .user-info-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .wide-input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 14px;
  }
  
  .submit-button {
    display: inline-block;
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .submit-button:hover {
    background-color: #0056b3;
  }
  
  .change-password-section {
    margin-top: 20px;
  }
  
  .change-password-button {
    padding: 10px 15px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .change-password-button:hover {
    background-color: #218838;
  }
  
  .password-form {
    margin-top: 15px;
  }
  
  .loading-spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #007bff;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    animation: spin 1s linear infinite;
    margin: 0 auto;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  </style>