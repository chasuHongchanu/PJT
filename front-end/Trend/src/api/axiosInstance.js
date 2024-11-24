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
    const router = useRouter()

    if (error.response?.status === 401) {
      try {
        // 토큰 갱신 시도
        const newAccessToken = await authStore.refreshToken()

        // 갱신된 토큰으로 원래 요청 재시도
        const originalRequest = error.config
        originalRequest.headers.Authorization = newAccessToken

        return axiosInstance(originalRequest)
      } catch (refreshError) {
        // 토큰 갱신 실패 시 로그인 페이지로 리다이렉트
        console.error('토큰 갱신 실패:', refreshError)
        authStore.clearAuth()
        router.push('/auth/login')
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  },
)

export default axiosInstance
