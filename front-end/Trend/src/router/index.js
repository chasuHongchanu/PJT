import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import TheItemView from '@/views/items/TheItemView.vue'
import TheMapView from '@/views/items/TheMapView.vue'
import TheItemDetailView from '@/views/items/TheItemDetailView.vue'
import TheItemRegistView from '@/views/items/TheItemRegistView.vue'
import TheItemEditView from '@/views/items/TheItemEditView.vue'
import TheLessorProfile from '@/views/items/TheLessorProfile.vue'
import TheReservationRegist from '@/views/trade/TheReservationRegist.vue'
import TheReservationDetail from '@/views/trade/TheReservationDetail.vue'
import TheReservationUpdate from '@/views/trade/TheReservationUpdate.vue'
import ThePayView from '@/views/trade/ThePayView.vue'
import TheTradeStart from '@/views/trade/TheTradeStart.vue'
import TheTradeStartDetail from '@/views/trade/TheTradeStartDetail.vue'
import TheReview from '@/views/trade/TheReview.vue'
import TheTradeList from '@/views/trade/TheTradeList.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
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
              meta: { requiresAuth: true },
            },
            {
              path: 'edit',
              name: 'ProfileEdit',
              component: () => import('@/views/user/TheProfileEditView.vue'),
              meta: { requiresAuth: true },
            },
          ],
        },
      ],
    },
    {
      path: '/items',
      name: 'Items',
      children: [
        {
          path: 'view',
          name: 'View',
          component: TheItemView,
        },
        {
          path: 'map',
          name: 'Map',
          component: TheMapView,
        },
        {
          path: 'detail/:id',
          name: 'ItemDetail',
          component: TheItemDetailView,
        },
        {
          path: 'regist',
          name: 'Regist',
          component: TheItemRegistView,
        },
        {
          path: 'update/:id',
          name: 'Update',
          component: TheItemEditView,
        },
        {
          path: 'lessor/:id',
          name: 'LessorProfile',
          component: TheLessorProfile,
        },
      ],
    },
    {
      path: '/auth',
      name: 'Auth',
      children: [
        {
          path: 'login',
          name: 'Login',
          component: () => import('@/views/auth/LoginView.vue'),
          meta: { requiresGuest: true },
        },
        {
          path: 'signup',
          name: 'Signup',
          component: () => import('@/views/auth/SignupView.vue'),
          meta: { requiresGuest: true },
        },
      ],
    },
    {
      path: '/trade',
      name: 'Trade',
      children: [
        {
          path: 'reservation',
          name: 'Reservation',
          component: TheReservationRegist,
        },
        {
          path: 'detail/:id',
          name: 'Detail',
          component: TheReservationDetail,
        },
        {
          path: 'reservationUpdate/:id',
          name: 'ReservationUpdate',
          component: TheReservationUpdate,
        },
        {
          path: 'pay/:id',
          name: 'Pay',
          component: ThePayView,
        },
        {
          path: 'start/:id',
          name: 'Start',
          component: TheTradeStart,
        },
        {
          path: 'startDetail/:id',
          name: 'StartDetail',
          component: TheTradeStartDetail,
        },
        {
          path: 'review/:id',
          name: 'Review',
          component: TheReview,
        },
        {
          path: 'itemList',
          name: 'ItemList',
          component: TheTradeList,
        },
      ],
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 인증이 필요한 페이지에 접근하는 경우
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // 인증되지 않은 경우
    if (!authStore.isAuthenticated) {
      next({
        path: '/auth/login',
        query: { redirect: to.fullPath },
      })
      return
    }
  }

  // 게스트 전용 페이지(로그인, 회원가입 등)에 접근하는 경우
  if (to.matched.some((record) => record.meta.requiresGuest)) {
    // 이미 인증된 경우
    if (authStore.isAuthenticated) {
      next({ path: '/' })
      return
    }
  }

  next()
})

export default router
