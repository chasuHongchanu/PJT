// src/api/courseApi.js
import axiosInstance from './axiosInstance'

export const courseApi = {
  // 코스 목록 조회 (page는 1부터 시작)
  getCourseList: (page = 1, size = 10) => {
    return axiosInstance.get('/course/list', {
      params: {
        page,
        size
      }
    })
  },

  // 코스 검색
  searchCourses: (searchParams) => {
    return axiosInstance.get('/course/search', { 
      params: {
        ...searchParams,
        page: searchParams.page || 1,
        size: searchParams.size || 10
      }
    })
  },

  // 코스 상세 조회
  getCourseDetail: (courseId) => {
    return axiosInstance.get(`/course/detail/${courseId}`)
  }
}