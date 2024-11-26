import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
})

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  (config) => {
    // multipart/form-data인 경우 Content-Type 헤더 제거 (브라우저가 자동으로 설정)
    if (config.data instanceof FormData) {
      config.headers['Content-Type'] = 'multipart/form-data'
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const authStore = useAuthStore()

    // refresh token API 요청 시에는 인터셉터 스킵
    if (error.config.url.includes('/refresh-token')) {
      return Promise.reject(error)
    }

    if (error.response?.status === 401) {
      try {
        const newAccessToken = await authStore.refreshToken()
        error.config.headers.Authorization = newAccessToken
        return axiosInstance(error.config)
      } catch (refreshError) {
        // 토큰 갱신 실패 시 로컬 스토리지 클리어 및 상태 초기화
        authStore.clearAuth()
        window.location.href = '/auth/login'  // router.push 대신 직접 리다이렉트
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  },
)

export default axiosInstance
