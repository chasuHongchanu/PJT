import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

export function setupAxios() {
  // 기본 설정
  axios.defaults.baseURL = 'http://localhost:8080/api'
  axios.defaults.withCredentials = true  // 쿠키 포함
  
  //CORS 설정
  axios.defaults.headers.common['Access-Control-Expose-Headers'] = 'authorization'

  // Request Interceptor
  axios.interceptors.request.use(
    config => {
      const authStore = useAuthStore()
      if (authStore.accessToken) {
        config.headers.Authorization = authStore.accessToken  // Bearer 포함된 토큰
      }
      return config
    },
    error => Promise.reject(error)
  )

  // Response Interceptor
  axios.interceptors.response.use(
    response => response,
    async error => {
      const originalRequest = error.config

      // AccessToken이 만료되어 401 에러가 발생한 경우
      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true
        
        try {
          const authStore = useAuthStore()
          await authStore.refreshToken()  // 새로운 토큰 요청
          
          // 새로운 토큰으로 원래 요청 재시도
          return axios(originalRequest)
        } catch (refreshError) {
          // RefreshToken도 만료되었거나 갱신 실패한 경우
          const authStore = useAuthStore()
          await authStore.logout()  // 로그아웃 처리
          window.location.href = '/auth/login'  // 로그인 페이지로 리다이렉트
          return Promise.reject(refreshError)
        }
      }
      
      return Promise.reject(error)
    }
  )
}