// src/api/userApi.js

import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/user';

// axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 - JWT 토큰 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const userApi = {
  // 사용자 정보 조회
  getUserInfo: async () => {
    try {
      const response = await apiClient.get('/user-info');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch user info: ' + error.message);
    }
  },

  // 사용자 정보 수정
  updateUserInfo: async (formData) => {
    try {
      const response = await apiClient.post('/user-info', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      throw new Error('Failed to update user info: ' + error.message);
    }
  },

  // 기타 사용자 관련 API 메소드들...
  resetPassword: async (passwordData) => {
    try {
      const response = await apiClient.post('/reset-password', passwordData);
      return response.data;
    } catch (error) {
      throw new Error('Failed to reset password: ' + error.message);
    }
  },
  
  // 필요한 다른 API 메소드들 추가...
};