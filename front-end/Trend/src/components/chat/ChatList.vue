<!-- src/components/chat/ChatList.vue -->
<template>
    <div class="chat-list-container">
      <div class="chat-header">
        <h2>메시지</h2>
      </div>
      <div class="chat-rooms">
        <div
          v-for="room in chatRooms"
          :key="room.roomId"
          class="chat-room"
          @click="enterChatRoom(room.roomId)"
        >
          <div class="profile-section">
            <img :src="room.otherUserProfileImg" :alt="room.otherUserNickname" class="profile-image" />
          </div>
          <div class="chat-info">
            <div class="user-name">{{ room.otherUserNickname }}</div>
            <div class="last-message">{{ room.lastMessage }}</div>
          </div>
          <div class="chat-meta">
            <div class="timestamp">{{ room.lastMessageTime }}</div>
            <div v-if="room.unreadCount > 0" class="unread-count">
              {{ room.unreadCount }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  // 채팅방 목록 더미 데이터
  const chatRooms = ref([
    {
      roomId: 1,
      otherUserNickname: 'Pikachu',
      otherUserProfileImg: '/src/assets/profile-default.png',
      lastMessage: '안녕하세요. 거래 가능할까요? 쿨거래 희망합니다.',
      lastMessageTime: '24. 10. 15.',
      unreadCount: 1
    },
    // ... 더미 데이터
  ])
  
  const enterChatRoom = (roomId) => {
    router.push(`/chat/room/${roomId}`)
  }
  </script>
  
  <style scoped>
  .chat-list-container {
    height: 100%;
    background: white;
  }
  
  .chat-header {
    padding: 20px;
    border-bottom: 1px solid #eee;
  }
  
  .chat-header h2 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
  }
  
  .chat-rooms {
    overflow-y: auto;
    height: calc(100% - 65px);
  }
  
  .chat-room {
    display: flex;
    padding: 16px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
  }
  
  .chat-room:hover {
    background: #f9f9f9;
  }
  
  .profile-section {
    margin-right: 12px;
  }
  
  .profile-image {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .chat-info {
    flex: 1;
    min-width: 0;
  }
  
  .user-name {
    font-weight: 600;
    margin-bottom: 4px;
  }
  
  .last-message {
    color: #666;
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .chat-meta {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 64px;
  }
  
  .timestamp {
    color: #999;
    font-size: 12px;
    margin-bottom: 4px;
  }
  
  .unread-count {
    background: #ff3b30;
    color: white;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 12px;
    min-width: 20px;
    text-align: center;
  }
  </style>