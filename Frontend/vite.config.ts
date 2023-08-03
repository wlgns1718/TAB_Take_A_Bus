import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
// 파일 절대경로 설정
import tsconfigPaths from "vite-tsconfig-paths";
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  publicDir: false, // vite의 public 폴더 설정을 비활성화합니다.
  css: {
    devSourcemap: true,
  },
});
