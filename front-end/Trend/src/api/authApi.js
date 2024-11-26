import axiosInstance from './axiosInstance'
import axios from 'axios'

export const authApi = {
  login: async (userId, userPassword) => {
    const response = await axiosInstance.post('/user/login', {
      userId,
      userPassword,
    })
    return response
  },

  refreshToken: async () => {
    // axios import 추가 및 baseURL 포함
    return axios.post(
      'http://localhost:8080/api/user/refresh-token',
      {},
      {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  },

  logout: async () => {
    const response = await axiosInstance.post('/user/logout')
    return response
  },

  signup: async (signupData) => {
    const response = await axiosInstance.post('/user/signup', signupData)
    return response
  },

  checkDuplicateId: async (userId) => {
    const response = await axiosInstance.get(`/user/duplicate-check/${userId}`)
    return response
  },
}
