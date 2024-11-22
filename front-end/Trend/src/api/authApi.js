import axios from 'axios'

export const authApi = {
  login: async (userId, userPassword) => {
    const response = await axios.post('/user/login', {
      userId,
      userPassword
    })
    console.log("전체 응답", response);
    console.log("헤더:", response.headers);
    console.log("Authorization 헤더", response.headers.authorization);
    
    return response
  },

  refreshToken: async () => {
    const response = await axios.post('/user/refresh-token')
    return response
  },

  logout: async () => {
    const response = await axios.post('/user/logout')
    return response
  }
}