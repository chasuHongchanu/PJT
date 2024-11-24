import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // 쿠키 전송을 위해 필수
});

export default axiosInstance;