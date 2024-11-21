import { createRouter, createWebHistory } from 'vue-router'
import ProfilePage from '@/views/mypage/ProfilePage.vue';
import MainView from '@/views/MainView.vue';
import TheItemView from '@/views/items/TheItemView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path : "/",
      name : "main",
      component : MainView
    },
    {
      path: '/items',
      name: 'Items',
      component: TheItemView,
      children: [
        
      ]
    },
  ],
})

export default router
