// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router'
import TheProfileInfoView from '@/views/user/TheProfileInfoView.vue'
import MainView from '@/views/MainView.vue'
import TheProfileEditView from '@/views/user/TheProfileEditView.vue'  // 동적 import 제거

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainView
    },
    {
      path: '/user',
      name: 'User',
      children: [
        {
          path: 'profile',
          name: 'Profile',
          children: [
            {
              path: 'info',
              name: 'ProfileInfo',
              component: TheProfileInfoView
            },
            {
              path: 'edit',
              name: 'ProfileEdit',
              component: TheProfileEditView
            }
          ]
        }
      ]
    },
  ]
})

export default router