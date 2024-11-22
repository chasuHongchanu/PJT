import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import ProfilePage from '@/views/mypage/ProfilePage.vue';
import MainView from '@/views/MainView.vue';
import TheItemView from '@/views/items/TheItemView.vue';
import TheMapView from '@/views/items/TheMapView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
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
              component: () => import('@/views/user/TheProfileInfoView.vue'),
              meta: { requiresAuth: true }
            },
            {
              path: 'edit',
              name: 'ProfileEdit',
              component: () => import('@/views/user/TheProfileEditView.vue'),
              meta: { requiresAuth: true }
            }
          ]
        }
      ]
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
    {
      path: '/auth',
      name: 'Auth',
      children: [
        {
          path: 'login',
          name: 'Login',
          component: () => import('@/views/auth/LoginView.vue'),
          meta: { requiresGuest: true }
        }
      ]
    }
  ],


})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 인증이 필요한 페이지에 접근하는 경우
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 인증되지 않은 경우
    if (!authStore.isAuthenticated) {
      next({
        path: '/auth/login',
        query: { redirect: to.fullPath }
      })
      return
    }
  }

  // 게스트 전용 페이지(로그인, 회원가입 등)에 접근하는 경우
  if (to.matched.some(record => record.meta.requiresGuest)) {
    // 이미 인증된 경우
    if (authStore.isAuthenticated) {
      next({ path: '/' })
      return
    }
  }

  next()
})

export default router