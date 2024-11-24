import axios from 'axios'

export const authApi = {
  // 로그인
  login: async (userId, userPassword) => {
    const response = await axios.post('/user/login', {
      userId,
      userPassword,
    })
    console.log('전체 응답', response)
    console.log('헤더:', response.headers)
    console.log('Authorization 헤더', response.headers.authorization)

    return response
  },

  // access token 갱신
  refreshToken: async () => {
    const response = await axios.post('/user/refresh-token')
    return response
  },

  // 로그아웃
  logout: async () => {
    const response = await axios.post('/user/logout')
    return response
  },

  // 회원가입 함수 추가
  signup: async (signupData) => {
    const response = await axios.post('/user/signup', signupData)
    return response
  },

  // ID 중복 검사 함수 추가
  checkDuplicateId: async (userId) => {
    const response = await axios.get(`/user/duplicate-check/${userId}`)
    return response
  },
}
