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
        <!-- 右上角登出按鈕 -->
        <div class="header">
          <button @click="logout" class="logout-button">登出</button>
        </div>
  
        <!-- Log 內容區域 -->
        <div class="log-container">
          <h2>Logs File</h2>
          <!-- Log 文件列表 -->
          <ul class="log-list">
            <li v-for="file in logFiles" :key="file" class="log-item">
              <span>{{ file }}</span>
              <button @click="downloadLog(file)" class="download-button">Download</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        logFiles: [], // 用於存儲從後端獲取的 Log 文件列表
      };
    },
    methods: {
      // 獲取 Log 文件列表
      async fetchLogs() {
        try {
          const response = await axios.get('http://localhost:8081/api/logs/files');
          this.logFiles = response.data; // 將後端返回的文件列表設置到 logFiles 變量中
        } catch (error) {
          console.error('獲取 Log 文件列表失敗', error);
          alert('獲取 Log 文件列表失敗，請重試');
        }
      },
      // 下載單個 Log 文件
      async downloadLog(filename) {
        try {
          const response = await axios.get(`http://localhost:8081/api/logs/download/${filename}`, {
            responseType: 'blob', // 指定返回類型為 Blob
          });
  
          // 創建下載鏈接
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', filename); // 下載文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        } catch (error) {
          console.error('下載 Log 文件失敗', error);
          alert('下載 Log 文件失敗，請重試');
        }
      },
      // 登出
      logout() {
        localStorage.removeItem('token'); // 假設使用 localStorage 存儲 token
        this.$router.push('/login'); // 跳轉到登錄頁面
      },
    },
    mounted() {
      this.fetchLogs(); // 組件加載時獲取 Log 文件列表
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
    margin-bottom: 20px;
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
  
  .log-container {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .log-list {
    list-style: none;
    padding: 0;
  }
  
  .log-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #ddd;
  }
  
  .download-button {
    padding: 5px 10px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
  }
  
  .download-button:hover {
    background-color: #218838;
  }
  
  .log-content {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 8px;
    border: 1px solid #ddd;
    max-height: 500px;
    overflow-y: auto;
  }
  
  pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    font-family: monospace;
    font-size: 14px;
    margin: 0;
  }
  </style>