import axiosInstance from './axiosInstance'
import { useAuthStore } from '@/stores/auth'

export const userApi = {
  async getUserInfo() {
    const authStore = useAuthStore()

    try {
      const response = await axiosInstance.get('/user/userinfo', {
        headers: {
          Authorization: authStore.accessToken,
        },
      })
      return response.data
    } catch (error) {
      if (error.response?.status === 401) {
        try {
          await authStore.refreshToken()
          const retryResponse = await axiosInstance.get('/user/userinfo', {
            headers: {
              Authorization: authStore.accessToken,
            },
          })
          return retryResponse.data
        } catch (refreshError) {
          throw refreshError
        }
      }
      throw error
    }
  },
  async updateUserInfo(userData, profileImage) {
    const authStore = useAuthStore()
    const formData = new FormData()

    try {
      // 이미지 파일이 있는 경우에만 추가 (null이나 undefined면 추가하지 않음)
      if (profileImage instanceof File) {
        formData.append('userProfileImg', profileImage)
      }

      // 나머지 데이터 추가
      Object.keys(userData).forEach((key) => {
        if (userData[key] !== null && userData[key] !== undefined && userData[key] !== '') {
          formData.append(key, userData[key])
        }
      })

      const response = await axiosInstance.put('/user/userinfo', formData, {
        headers: {
          Authorization: authStore.accessToken,
        },
      })

      return response.data
    } catch (error) {
      console.error('프로필 수정 실패:', error)
      throw error
    }
  },
}
