import { createRouter, createWebHistory } from 'vue-router'
import ProfilePage from '@/views/mypage/ProfilePage.vue';
import MainView from '@/views/MainView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path : "/myPage",
      name : "myPage",
      component : ProfilePage
    },
    {
      path : "/",
      name : "main",
      component : MainView
    },
  ],
})

export default router
