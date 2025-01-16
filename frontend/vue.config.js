const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  lintOnSave: false, // 禁用 ESLint 检查
  transpileDependencies: true,
  devServer: {
    proxy: 'http://localhost:8080', 
    port: 8080
  },
});