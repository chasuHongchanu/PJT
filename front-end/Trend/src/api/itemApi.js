// src/api/itemApi.js
import axiosInstance from './axiosInstance'

export const itemApi = {
  // 물품 등록 API
  registerItem(formData) {
    return axiosInstance.post('/item/rent', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },
}