import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { setupAxios } from './plugins/axios'
import { useAuthStore } from './stores/auth';
import './index.css'

async function initializeApp() {
  const app = createApp(App)
  const pinia = createPinia()

  app.use(pinia)
  setupAxios()
  app.use(router)

  // 인증 상태 초기화
  const authStore = useAuthStore();
  await authStore.initializeAuth();

  app.mount('#app')
}

initializeApp().catch(error => {
  console.error('앱 초기화 중 오류 발생:', error);
});