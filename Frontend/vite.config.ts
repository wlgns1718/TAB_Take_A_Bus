import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  publicDir: false, // vite의 public 폴더 설정을 비활성화합니다.
  css : {
    devSourcemap: true,
  }
})
