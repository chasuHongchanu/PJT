// src/api/courseApi.js
import axiosInstance from './axiosInstance'  // 수정: default import 사용

export const courseApi = {
  // 코스 목록 조회
  getCourseList: (page = 1) => {
    return axiosInstance.get(`/course/list?page=${page}`)  // 수정: 중복 api 경로 제거
  },

  // 코스 검색
  searchCourses: (searchParams) => {
    return axiosInstance.get('/course/search', { params: searchParams })  // 수정: 중복 api 경로 제거
  }
}