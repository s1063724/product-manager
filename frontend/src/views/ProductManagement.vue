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
        <div class="product-management-container">
          <!-- 搜尋框和登出按鈕 -->
          <div class="header">
            <div class="search-container">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="搜尋商品..."
                class="search-box"
              />

            </div>
            <button @click="logout" class="logout-button">登出</button>
          </div>
  
          <!-- 添加商品表單 -->
          <div class="add-product-container">
            <h2>添加商品</h2>
            <form @submit.prevent="addProduct">
              <div class="form-group">
                <label for="name">商品名稱</label>
                <input
                  v-model="product.name"
                  type="text"
                  id="name"
                  placeholder="請輸入商品名稱"
                  required
                  class="wide-input"
                />
              </div>
              <div class="form-group">
                <label for="specification">產品資訊</label>
                <input
                  v-model="product.specification"
                  type="text"
                  id="specification"
                  placeholder="請描述產品規格"
                  required
                  class="wide-input"
                />
              </div>
              <div class="form-group">
                <label for="price">產品價格</label>
                <input
                  v-model="product.price"
                  type="number"
                  id="price"
                  placeholder="請輸入產品價格"
                  required
                  class="wide-input"
                />
              </div>
              <div class="form-group">
                <label>圖片上傳方式</label>
                <div class="radio-group">
                  <label>
                    <input
                      type="radio"
                      v-model="imageUploadMethod"
                      value="upload"
                    />
                    上傳圖片
                  </label>
                  <label>
                    <input
                      type="radio"
                      v-model="imageUploadMethod"
                      value="link"
                    />
                    輸入圖片連結
                  </label>
                </div>
              </div>
              <div v-if="imageUploadMethod === 'upload'" class="form-group">
                <label for="image">上傳圖片</label>
                <input
                  type="file"
                  id="image"
                  @change="handleFileUpload"
                  accept="image/*"
                />
              </div>
              <div v-if="imageUploadMethod === 'link'" class="form-group">
                <label for="imageUrl">圖片連結</label>
                <input
                  v-model="product.imageUrl"
                  type="text"
                  id="imageUrl"
                  placeholder="請輸入圖片連結"
                  class="wide-input"
                />
              </div>
              <button type="submit" :disabled="isLoading" class="submit-button">
                <span v-if="isLoading" class="loading-spinner"></span>
                <span v-else>添加商品</span>
              </button>
            </form>
          </div>
  
          <!-- 產品列表 -->
          <div class="product-list">
            <h2>商品列表</h2>
            <table class="styled-table">
              <thead>
                <tr>
                  <th>商品名稱</th>
                  <th>產品資訊</th>
                  <th>價格</th>
                  <th>圖片</th>
                  <th>操作</th> <!-- 新增操作欄 -->
                </tr>
              </thead>
              <tbody>
                <tr v-for="(product, index) in filteredProducts" :key="index">
                  <td>{{ product.name }}</td>
                  <td>{{ product.specification }}</td>
                  <td>{{ product.price }}</td>
                  <td>
                    <img :src="product.imageUrl" alt="商品圖片" class="product-image" v-if="product.imageUrl" />
                    <span v-else class="placeholder">無圖片</span>
                  </td>
                  <td>
                    <button @click="editProduct(product)" class="edit-button">編輯</button>
                    <button @click="deleteProduct(product.id)" class="delete-button">刪除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
  
          <!-- 彈窗提示 -->
          <div v-if="showAlert" class="alert-box">
            <p>{{ alertMessage }}</p>
            <button @click="showAlert = false" class="close-alert">關閉</button>
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
        product: {
          name: '',
          specification: '',
          price: 0,
          imageUrl: '',
        },
        file: null, // 用於存儲上傳的文件
        imageUploadMethod: 'upload', // 默認選擇上傳圖片
        showAlert: false, // 是否顯示彈窗
        alertMessage: '', // 彈窗提示信息
        isLoading: false, // 加載狀態
        searchQuery: '', // 搜尋框內容
        products: [], // 產品列表
      };
    },
    computed: {
      filteredProducts() {
        return this.products.filter(product =>
          product.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      },
    },
    methods: {
      handleFileUpload(event) {
        this.file = event.target.files[0];
      },
      async addProduct() {
        try {
          if (this.imageUploadMethod === 'upload' && this.file) {
            const formData = new FormData();
            formData.append('file', this.file);
  
            const uploadResponse = await axios.post('http://localhost:8080/api/upload', formData, {
              headers: {
                'Content-Type': 'multipart/form-data',
              },
            });
  
            this.product.imageUrl = uploadResponse.data;
          }
  
          const response = await axios.post('http://localhost:8080/api/products/add', this.product);
          this.alertMessage = '商品添加成功';
          this.showAlert = true;
          this.fetchProducts();
          setTimeout(() => {
            this.showAlert = false;
          }, 2000);
        } catch (error) {
          if (error.response) {
            switch (error.response.status) {
              case 400:
                this.alertMessage = error.response.data || '請求參數錯誤';
                break;
              case 409:
                this.alertMessage = '商品已存在';
                break;
              default:
                this.alertMessage = '添加商品失敗，請重試';
            }
          } else {
            this.alertMessage = '網絡錯誤，請檢查連接';
          }
          this.showAlert = true;
        } finally {
          this.isLoading = false;
        }
      },
      async deleteProduct(productId) {
        try {
          await axios.delete(`http://localhost:8080/api/products/${productId}`);
          this.alertMessage = '商品刪除成功';
          this.showAlert = true;
          this.fetchProducts(); // 重新獲取商品列表
          setTimeout(() => {
            this.showAlert = false;
          }, 2000);
        } catch (error) {
          this.alertMessage = '刪除商品失敗，請重試';
          this.showAlert = true;
        }
      },
      editProduct(product) {
        // 將商品信息填充到表單中
        this.product = { ...product };
        this.imageUploadMethod = this.product.imageUrl ? 'link' : 'upload';
      },
      logout() {
        localStorage.removeItem('token');
        this.$router.push('/login');
      },
      async fetchProducts() {
        try {
          const response = await axios.get('http://localhost:8080/api/products');
          this.products = response.data;
        } catch (error) {
          console.error('獲取產品列表失敗', error);
        }
      },
      
    },
    mounted() {
      this.fetchProducts();
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
  
  .product-management-container {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .search-container {
    display: flex;
    align-items: center;
    width: 70%;
  }
  
  .search-box {
    flex: 1;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    margin-right: 10px;
  }
  
  .search-button {
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .search-button:hover {
    background-color: #0056b3;
  }
  
  .logout-button {
    padding: 10px 15px;
    background-color: #ff4d4f;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
  }
  
  .logout-button:hover {
    background-color: #ff7875;
  }
  
  .add-product-container {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
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
  
  .radio-group label {
    margin-right: 15px;
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
  
  .product-list {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .styled-table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
    font-size: 14px;
  }
  
  .styled-table th, .styled-table td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  .styled-table th {
    background-color: #f2f2f2;
    color: #333;
  }
  
  .product-image {
    max-width: 80px;
    max-height: 80px;
    border-radius: 4px;
    object-fit: cover;
  }
  
  .placeholder {
    color: #aaa;
    font-style: italic;
  }
  
  .alert-box {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  }
  
  .alert-box p {
    margin: 0 0 10px;
    font-size: 16px;
  }
  
  .close-alert {
    padding: 8px 12px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
  }
  
  .close-alert:hover {
    background-color: #0056b3;
  }
  .edit-button {
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    margin-right: 5px;
    }

    .edit-button:hover {
    background-color: #0056b3;
    }

    .delete-button {
    padding: 5px 10px;
    background-color: #ff4d4f;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    }

    .delete-button:hover {
    background-color: #ff7875;
    }
  </style>