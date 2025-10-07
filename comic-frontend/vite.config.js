import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// 引入按需导入相关插件
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    // 自动导入 Element Plus 的组合式 API 等
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    // 自动导入 Element Plus 的组件和图标
    Components({
      resolvers: [
        ElementPlusResolver({
          importStyle: "css", // 按需导入样式
          importIcons: true, // 显式指定导入图标
        }),
      ],
    }),
  ],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '') // 去掉/api前缀
      }
    }
  }
})