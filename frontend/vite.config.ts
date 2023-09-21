import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: {},
  },
  // server: {
  //   port: 3000,
  //   https: true,
  //   hmr: {
  //     host: 'localhost:3001/',
  //     port: 3001,
  //     protocol: 'ws',
  //   },
  // },
  resolve: {
    alias: [
      { find: '@atom', replacement: '/src/atom' },
      { find: '@utils', replacement: '/src/utils' },
      { find: '@routers', replacement: '/src/routers' },
      { find: '@constants', replacement: '/src/constants' },
      { find: '@assets', replacement: '/src/assets' },
      { find: '@apis', replacement: '/src/apis' },
      { find: '@pages', replacement: '/src/components/pages' },
      { find: '@components', replacement: '/src/components' },
      { find: '@', replacement: '/src' },
    ],
  },
});
