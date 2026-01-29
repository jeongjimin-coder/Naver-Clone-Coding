import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // 프론트에서 /api로 시작하는 요청을 보내면 백엔드로 전달함
      '/api': {
        target: 'http://localhost:9090/naver', // 내 백엔드 주소 (Context Path 포함)
        changeOrigin: true,
        secure: false,
      }
    }
  }
})