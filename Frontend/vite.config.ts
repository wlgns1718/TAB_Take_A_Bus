import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
// 파일 절대경로 설정
import tsconfigPaths from "vite-tsconfig-paths";
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  css: {
    devSourcemap: true,
  },
});
