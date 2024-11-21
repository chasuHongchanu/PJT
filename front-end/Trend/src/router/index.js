import { createRouter, createWebHistory } from 'vue-router'
import ProfilePage from '@/views/mypage/ProfilePage.vue';
import MainView from '@/views/MainView.vue';
import TheItemView from '@/views/items/TheItemView.vue';
import TheMapView from '@/views/items/TheMapView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: MainView
    },
    {
      path: '/items',
      name: 'Items',
      children: [
        {
          path: "view",
          name: "View",
          component: TheItemView
        },
        {
          path: "map",
          name: "Map",
          component: TheMapView
        }
      ]
    },
  ],
})

export default router
