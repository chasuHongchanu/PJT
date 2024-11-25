<!-- src/components/chat/ChatRoom.vue -->
<template>
    <div class="chat-room-container">
      <!-- 헤더 -->
      <div class="chat-header">
        <button class="back-button" @click="goBack">
          <span>←</span>
        </button>
        <span class="other-user-name">{{ otherUserNickname }}</span>
      </div>
  
      <!-- 물품 정보 -->
      <div class="item-info">
        <img :src="itemInfo.thumbnail" :alt="itemInfo.name" class="item-thumbnail" />
        <div class="item-details">
          <div class="item-name">{{ itemInfo.name }}</div>
          <div class="item-location">{{ itemInfo.location }}</div>
          <div class="item-price">{{ itemInfo.price }}₩</div>
        </div>
      </div>
  
      <!-- 메시지 목록 -->
      <div class="messages-container" ref="messagesContainer">
        <div
          v-for="message in messages"
          :key="message.id"
          :class="['message-wrapper', message.isMine ? 'mine' : 'other']"
        >
          <img
            v-if="!message.isMine"
            :src="message.profileImg"
            :alt="message.sender"
            class="message-profile"
          />
          <div class="message-content">
            <div class="message-bubble">
              {{ message.content }}
            </div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </div>
      </div>
  
      <!-- 메시지 입력 -->
      <div class="message-input-container">
        <button class="attach-button">
          <span>📎</span>
        </button>
        <input
          v-model="newMessage"
          type="text"
          placeholder="메시지를 입력하세요..."
          @keyup.enter="sendMessage"
        />
        <button class="send-button" @click="sendMessage" :disabled="!newMessage.trim()">
          <span>→</span>
        </button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, nextTick } from 'vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const messagesContainer = ref(null)
  const newMessage = ref('')
  
  // 더미 데이터
  const otherUserNickname = ref('판매자')
  const itemInfo = ref({
    thumbnail: '/src/assets/items/smartphone.jpg',
    name: '스마트폰',
    location: '대한민국 서울시 강남구',
    price: '35,000'
  })
  
  const messages = ref([
    {
      id: 1,
      content: '어디서 만날까요?',
      time: '2024.10.25 09:31',
      isMine: false,
      profileImg: '/src/assets/profile-default.png',
      sender: '판매자'
    },
    {
      id: 2,
      content: '거래는 서울에서 희망합니다',
      time: '2024.10.26 12:31',
      isMine: true
    },
    {
      id: 3,
      content: '그니까 서울 어디,,',
      time: '2024.10.31 05:24',
      isMine: false,
      profileImg: '/src/assets/profile-default.png',
      sender: '판매자'
    }
  ])
  
  const scrollToBottom = async () => {
    await nextTick()
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  }
  
  const sendMessage = () => {
    if (!newMessage.value.trim()) return
  
    messages.value.push({
      id: messages.value.length + 1,
      content: newMessage.value,
      time: new Date().toLocaleString(),
      isMine: true
    })
  
    newMessage.value = ''
    scrollToBottom()
  }
  
  const goBack = () => {
    router.back()
  }
  
  onMounted(() => {
    scrollToBottom()
  })
  </script>
  
  <style scoped>
  .chat-room-container {
    display: flex;
    flex-direction: column;
    height: 100%;
    background: white;
  }
  
  .chat-header {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #eee;
  }
  
  .back-button {
    background: none;
    border: none;
    font-size: 24px;
    padding: 0 16px 0 0;
    cursor: pointer;
  }
  
  .other-user-name {
    font-size: 18px;
    font-weight: 600;
  }
  
  .item-info {
    display: flex;
    padding: 12px;
    border-bottom: 1px solid #eee;
  }
  
  .item-thumbnail {
    width: 48px;
    height: 48px;
    object-fit: cover;
    border-radius: 4px;
    margin-right: 12px;
  }
  
  .item-details {
    flex: 1;
  }
  
  .item-name {
    font-weight: 600;
    margin-bottom: 4px;
  }
  
  .item-location {
    font-size: 14px;
    color: #666;
    margin-bottom: 2px;
  }
  
  .item-price {
    font-weight: 600;
    color: #ff3b30;
  }
  
  .messages-container {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
  }
  
  .message-wrapper {
    display: flex;
    margin-bottom: 16px;
  }
  
  .message-wrapper.mine {
    justify-content: flex-end;
  }
  
  .message-profile {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    margin-right: 8px;
  }
  
  .message-content {
    max-width: 70%;
  }
  
  .message-bubble {
    padding: 12px;
    border-radius: 16px;
    background: #f1f1f1;
    margin-bottom: 4px;
  }
  
  .mine .message-bubble {
    background: #ff3b30;
    color: white;
  }
  
  .message-time {
    font-size: 12px;
    color: #999;
    text-align: right;
  }
  
  .message-input-container {
    display: flex;
    padding: 12px;
    border-top: 1px solid #eee;
    background: white;
  }
  
  .attach-button,
  .send-button {
    background: none;
    border: none;
    padding: 8px 12px;
    font-size: 20px;
    cursor: pointer;
  }
  
  .send-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  input {
    flex: 1;
    padding: 8px 12px;
    border: 1px solid #eee;
    border-radius: 20px;
    margin: 0 8px;
  }
  
  input:focus {
    outline: none;
    border-color: #ff3b30;
  }
  </style>