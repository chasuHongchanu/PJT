// src/api/itemApi.js
import axiosInstance from './axiosInstance'

export const chatApi = {
    // 채팅방 생성
    createChatroom: (chatRoomData) => {
      return axiosInstance.post(`/chat/rooms`, chatRoomData)
    },
  }