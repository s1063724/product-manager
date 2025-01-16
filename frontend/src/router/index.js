import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import ForgotPassword from '../views/ForgotPassword.vue';
import ProductManagement from '../views/ProductManagement.vue';
import UserInfo from '@/views/UserInfo.vue';
import Log from '@/views/Log.vue'; // Log 頁面

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/forgot-password', component: ForgotPassword },
  
  {
    path: '/log',
    name: 'Log',
    component: Log,
  },
  {
    path: '/user-info',
    name: 'UserInfo',
    component: UserInfo,
  },
  {
    path: '/product-management',
    component: ProductManagement,
    meta: { requiresAuth: true }, // 需要登录
  },
  
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 导航守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token'); // 检查是否登录
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login'); // 未登录则跳转到登录页面
  } else {
    next(); // 否则继续
  }
});

export default router;