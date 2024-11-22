import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { setupAxios } from './plugins/axios'
import './index.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
setupAxios()
app.use(router)

app.mount('#app')