// src/api/itemApi.js
import axiosInstance from './axiosInstance'

export const itemApi = {
  // 물품 등록 API
  registerItem(formData) {
    return axiosInstance.post('/item/rent', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`
      },
    })
  },

  // 물품 상세 정보 조회
  getItemForEdit: async (itemId) => {
    return axiosInstance.get(`/item/rent/update/${itemId}`, {
      headers: {
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  // 물품 수정
  updateItem: async (formData) => {
    return axiosInstance.put('/item/rent', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`
      }
    })
  },
}
