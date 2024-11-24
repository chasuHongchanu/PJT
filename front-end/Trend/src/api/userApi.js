import axiosInstance from './axiosInstance';
import { useAuthStore } from '@/stores/auth';

export const userApi = {
  async getUserInfo() {
    const authStore = useAuthStore();
    
    try {
      const response = await axiosInstance.get('/user/userinfo', {
        headers: {
          Authorization: authStore.accessToken
        }
      });
      return response.data;
    } catch (error) {
      if (error.response?.status === 401) {
        try {
          await authStore.refreshToken();
          const retryResponse = await axiosInstance.get('/user/userinfo', {
            headers: {
              Authorization: authStore.accessToken
            }
          });
          return retryResponse.data;
        } catch (refreshError) {
          throw refreshError;
        }
      }
      throw error;
    }
  }
};