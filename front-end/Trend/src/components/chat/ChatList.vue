<!-- src/components/chat/ChatList.vue -->
<template>
  <div class="chat-list-container">
    <div class="chat-header">
      <h2>메시지</h2>
    </div>
    <div v-if="isLoading" class="loading-state">로딩중...</div>
    <div v-else-if="chatRooms.length === 0" class="empty-state">메시지가 없습니다</div>
    <div v-else class="chat-rooms">
      <div
        v-for="room in chatRooms"
        :key="room.roomId"
        class="chat-room"
        @click="enterChatRoom(room.roomId)"
      >
        <div class="profile-section">
          <img :src="room.userProfileImg" :alt="room.userNickname" class="profile-image" />
        </div>
        <div class="chat-info">
          <div class="user-name">{{ room.userNickname }}</div>
          <div class="last-message">{{ room.lastMessage || '새로운 대화를 시작해보세요' }}</div>
        </div>
        <div class="chat-meta">
          <div class="timestamp">{{ formatTimestamp(room.lastMessageTime) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getDatabase, ref as dbRef, onValue, off } from 'firebase/database'
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const chatRooms = ref([])
const database = getDatabase()
const isLoading = ref(true) // isLoading ref 선언을 여기로 이동
let chatRoomsListeners = new Map()

// Firebase Authentication 설정
const setupFirebaseAuth = async () => {
  try {
    const auth = getAuth()

    // 이미 로그인되어 있다면 재사용
    if (!auth.currentUser) {
      // Firebase 익명 로그인
      await signInWithEmailAndPassword(auth, `${authStore.userId}@trend.com`, authStore.userId)
    }

    console.log('Firebase 인증 성공:', auth.currentUser.uid)
  } catch (error) {
    console.error('Firebase 인증 실패:', error)
    throw error
  }
}

// 채팅방 목록 초기 로드 및 실시간 업데이트 설정
const initializeChatRooms = async () => {
  try {
    isLoading.value = true
    console.log('채팅방 목록 요청 시작')

    // Firebase 인증 먼저 수행
    await setupFirebaseAuth()

    const response = await axios.get('/chat/rooms', {
      headers: {
        Authorization: authStore.accessToken,
      },
    })
    console.log('받은 채팅방 데이터:', response.data)

    // 초기 데이터 설정
    chatRooms.value = response.data.data
    console.log('설정된 채팅방 목록:', chatRooms.value)

    // 각 채팅방에 대한 실시간 리스너 설정
    chatRooms.value.forEach((room) => {
      console.log(`채팅방 ${room.roomId}에 대한 리스너 설정`)
      setupRoomListener(room)
    })
  } catch (error) {
    console.error('채팅방 목록 초기화 실패:', error)
  } finally {
    isLoading.value = false
  }
}

// 개별 채팅방 리스너 설정
const setupRoomListener = (room) => {
  if (chatRoomsListeners.has(room.roomId)) {
    console.log(`기존 리스너 제거: ${room.roomId}`)
    off(chatRoomsListeners.get(room.roomId))
  }

  const roomRef = dbRef(database, `chatrooms/${room.roomId}`)
  console.log(`Firebase 경로 설정: chatrooms/${room.roomId}`)

  const listener = onValue(
    roomRef,
    async (snapshot) => {
      console.log(`채팅방 ${room.roomId} 데이터 업데이트:`, snapshot.val())
      const roomData = snapshot.val()
      if (!roomData) return

      try {
        // 현재 사용자가 해당 채팅방의 멤버인지 확인
        const members = roomData.members || {}
        console.log(`채팅방 ${room.roomId} 멤버:`, members)
        console.log('현재 사용자:', authStore.userId)

        if (!members[authStore.userId]) {
          console.log(`사용자 ${authStore.userId}는 채팅방 ${room.roomId}의 멤버가 아님`)
          return
        }

        // 최근 메시지 찾기
        let lastMessage = null
        let lastMessageTime = null

        if (roomData.messages) {
          const messages = Object.values(roomData.messages)
          console.log(`채팅방 ${room.roomId}의 메시지:`, messages)

          const latestMessage = messages.reduce((latest, current) => {
            return new Date(current.chatCreatedAt) > new Date(latest.chatCreatedAt)
              ? current
              : latest
          })

          lastMessage = latestMessage.messageContent
          lastMessageTime = latestMessage.chatCreatedAt
          console.log(`최신 메시지:`, { lastMessage, lastMessageTime })
        }

        // 채팅방 정보 업데이트
        const updatedRoom = {
          ...room,
          lastMessage: lastMessage || room.lastMessage,
          lastMessageTime: lastMessageTime || room.lastMessageTime,
        }

        // 채팅방 목록 업데이트
        chatRooms.value = chatRooms.value.map((r) => (r.roomId === room.roomId ? updatedRoom : r))
        console.log('업데이트된 채팅방 목록:', chatRooms.value)
      } catch (error) {
        console.error(`채팅방 ${room.roomId} 데이터 처리 중 오류:`, error)
      }
    },
    (error) => {
      console.error(`채팅방 ${room.roomId} 리스너 에러:`, error)
    },
  )

  chatRoomsListeners.set(room.roomId, roomRef)
}

// 타임스탬프 포맷팅
const formatTimestamp = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return `${date.getFullYear().toString().substr(-2)}. ${(date.getMonth() + 1)
    .toString()
    .padStart(2, '0')}. ${date.getDate().toString().padStart(2, '0')}.`
}

// 채팅방 입장
const enterChatRoom = (roomId) => {
  router.push(`/chat/room/${roomId}`)
}

// 디버깅을 위한 watch
watch(
  chatRooms,
  (newRooms) => {
    console.log('채팅방 목록 변경 감지:', newRooms)
  },
  { deep: true },
)

// 컴포넌트 마운트 시 초기화
onMounted(async () => {
  try {
    console.log('컴포넌트 마운트 - 초기화 시작')
    chatRooms.value = []
    await initializeChatRooms()
  } catch (error) {
    console.error('채팅 목록 초기화 실패:', error)
    chatRooms.value = []
  }
})

// 컴포넌트 언마운트 시 리스너 정리
onUnmounted(() => {
  console.log('컴포넌트 언마운트 - 리스너 정리')
  chatRoomsListeners.forEach((ref) => {
    off(ref)
  })
  chatRoomsListeners.clear()
})
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

.loading-state,
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100% - 65px);
  color: #666;
}
</style>
