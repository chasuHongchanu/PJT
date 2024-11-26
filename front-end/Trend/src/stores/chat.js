// @/stores/chat.js
import { defineStore } from 'pinia'

export const useChatStore = defineStore('chat', {
  state: () => ({
    isChatOpen: false,
    selectedRoomId: null,
    selectedItemId: null,
  }),

  actions: {
    openChat(roomId = null, itemId = null) {
      this.isChatOpen = true
      this.selectedRoomId = roomId
      this.selectedItemId = itemId
    },

    closeChat() {
      this.isChatOpen = false
      this.selectedRoomId = null
      this.selectedItemId = null
    },

    selectRoom(roomId, itemId) {
      this.selectedRoomId = roomId
      this.selectedItemId = itemId
    },
  },
})
