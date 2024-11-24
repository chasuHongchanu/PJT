import axiosInstance from './axiosInstance'

export const authApi = {
  login: async (userId, userPassword) => {
    const response = await axiosInstance.post('/user/login', {
      userId,
      userPassword,
    })
    return response
  },

  refreshToken: async () => {
    const response = await axiosInstance.post('/user/refresh-token')
    return response
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