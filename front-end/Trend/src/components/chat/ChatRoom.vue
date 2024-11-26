<template>
  <div class="chat-room-container">
    <!-- 헤더 -->
    <div class="header">
      <button class="back-button" @click="$emit('back')">
        <span><</span>
      </button>
      <span v-if="itemInfo" class="seller-name">{{ itemInfo.lessorNickname }}</span>
      <span v-else class="seller-name">로딩중...</span>
      <button
        v-if="itemInfo && itemInfo.lessorId === currentUserId"
        class="reservation-button"
        @click="handleReservation"
      >
        예약하기
      </button>
    </div>

    <!-- 물품 정보 -->
    <div class="item-info" v-if="itemInfo">
      <div class="item-content">
        <img
          v-if="itemInfo?.itemImageNames?.[0]"
          :src="itemInfo.itemImageNames?.[0]"
          :alt="itemInfo.itemName"
          class="item-img"
          @error="handleImageError"
        />
        <div class="item-details">
          <div class="item-name">{{ itemInfo.itemName }}</div>
          <div class="item-area">{{ itemInfo.address }}</div>
          <div class="item-price">{{ formatPrice(itemInfo.itemPrice) }}원</div>
        </div>
      </div>
    </div>

    <!-- 메시지 목록 -->
    <div class="messages-container" ref="messagesContainer" @scroll="handleScroll">
      <div v-if="isLoading" class="loading-state">로딩중...</div>
      <template v-else>
        <div
          v-for="message in messages"
          :key="message.messageId"
          :class="[
            'message-wrapper',
            message.senderId === currentUserId ? 'my-message' : 'other-message',
          ]"
        >
          <template v-if="message.senderId !== currentUserId">
            <img
              :src="message.senderProfileImg || DefaultProfileImg"
              :alt="itemInfo?.lessorNickname || '상대방'"
              class="sender-profile"
              @error="handleImageError"
            />
          </template>
          <div
            :class="['message-box', message.senderId === currentUserId ? 'my-box' : 'other-box']"
          >
            <div class="message-content">
              <template v-if="message.messageImg && message.messageContent">
                <!-- 이미지와 텍스트가 모두 있는 경우 -->
                <div class="message-image-container">
                  <img
                    v-if="message.messageImg"
                    :src="message.messageImg"
                    alt="첨부 이미지"
                    class="message-image"
                    @error="handleImageError"
                    @load="handleImageLoad"
                    @click="openImageViewer(message.messageImg)"
                  />
                </div>
                <div class="message-text">{{ message.messageContent }}</div>
              </template>
              <template v-else-if="message.messageImg">
                <!-- 이미지만 있는 경우 -->
                <img
                  :src="message.messageImg"
                  alt="첨부 이미지"
                  class="message-image"
                  @error="handleImageError"
                  @load="handleImageLoad"
                  @click="openImageViewer(message.messageImg)"
                />
              </template>
              <template v-else>
                <!-- 텍스트만 있는 경우 -->
                <div class="message-text">{{ message.messageContent }}</div>
              </template>
            </div>
            <div
              :class="[
                'message-time',
                message.senderId === currentUserId ? 'my-time' : 'other-time',
              ]"
            >
              {{ formatTime(message.chatCreatedAt) }}
            </div>
          </div>
        </div>
      </template>
    </div>

    <!-- 이미지 미리보기 부분 수정 -->
    <div v-if="previewImage" class="image-preview">
      <img :src="previewImage" alt="미리보기" class="preview-image" />
      <div class="preview-info">
        <span class="preview-filename">{{ previewFileName }}</span>
        <span class="preview-size">{{ formatFileSize(selectedImage?.size) }}</span>
      </div>
      <button class="preview-close" @click="clearPreview">×</button>
    </div>

    <!-- 메시지 입력 영역 -->
    <div class="input-container">
      <input
        type="file"
        ref="fileInput"
        accept="image/*"
        class="hidden"
        @change="handleImageSelect"
      />
      <button class="attach-button" @click="$refs.fileInput.click()">
        <span>📎</span>
      </button>
      <input
        v-model="newMessage"
        type="text"
        class="message-input"
        placeholder="메시지를 입력하세요"
        @keyup.enter="sendMessage"
      />
      <button
        class="send-button"
        :disabled="!newMessage.trim() && !selectedImage"
        @click="sendMessage"
      >
        >
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import {
  getDatabase,
  ref as dbRef,
  onValue,
  off,
  query,
  orderByChild,
  limitToLast,
  endBefore,
  get,
} from 'firebase/database'
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth'
import axios from 'axios'
import { userApi } from '@/api/userApi'
import { firebaseUtils } from '@/utils/firebaseUtils'
import DefaultProfileImg from '@/assets/default-profile.svg'
import DefaultImage from '@/assets/default-image.svg'
import { useRouter } from 'vue-router'
import { useChatStore } from '@/stores/chat' // import 추가

// setup 내부에 chatStore 추가
const chatStore = useChatStore()

const router = useRouter()
const props = defineProps({
  roomId: {
    type: String,
    required: true,
  },
  itemId: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['back'])
const authStore = useAuthStore()
const currentUserId = computed(() => authStore.userId)

// Firebase 관련 상태
const database = getDatabase()
const messageListener = ref(null)

// 상태 관리
const messagesContainer = ref(null)
const newMessage = ref('')
const fileInput = ref(null)
const selectedImage = ref(null)
const previewImage = ref(null)
const previewFileName = ref('')
const isLoading = ref(true)
const itemInfo = ref(null)
const messages = ref([])

// 이미지 로드 상태 관리
const imageLoadStatus = ref({})
const imageCache = ref(new Map())

// 이미지 뷰어 상태
const showImageViewer = ref(false)
const viewerImage = ref(null)

// 페이지네이션 관련 상태
const PAGE_SIZE = 20
const isLoadingMore = ref(false)
const hasMoreMessages = ref(true)
const oldestMessageTime = ref(null)
const isInitialLoad = ref(true)

// Firebase 인증 설정
const setupFirebaseAuth = async () => {
  try {
    const auth = getAuth()
    if (!auth.currentUser) {
      await signInWithEmailAndPassword(auth, `${authStore.userId}@trend.com`, authStore.userId)
    }
    console.log('Firebase 인증 성공:', auth.currentUser.uid)
    return true
  } catch (error) {
    console.error('Firebase 인증 실패:', error)
    throw error
  }
}

// 사용자 프로필 이미지 조회 함수 수정
const getUserProfileImage = async (userId) => {
  try {
    // 캐시 확인
    if (imageCache.value.has(`profile_${userId}`)) {
      return imageCache.value.get(`profile_${userId}`)
    }

    // 사용자의 프로필 이미지 경로 조회
    const imagePath = await userApi.getUserProfileImg(userId)

    // Firebase Storage에서 이미지 URL 가져오기
    const imageUrl = await firebaseUtils.getProfileImageUrl(imagePath)

    // 캐시 저장
    imageCache.value.set(`profile_${userId}`, imageUrl)
    return imageUrl
  } catch (error) {
    console.error('프로필 이미지 조회 실패:', error)
    return DefaultProfileImg
  }
}

// 메시지 이미지 조회 함수 수정
const getMessageImage = async (messageId, messageImg) => {
  try {
    // 캐시 확인
    if (imageCache.value.has(`message_${messageId}`)) {
      return imageCache.value.get(`message_${messageId}`)
    }

    if (!messageImg) {
      return null
    }

    const imageUrl = await firebaseUtils.getImageUrl(messageImg)

    // 캐시 저장
    imageCache.value.set(`message_${messageId}`, imageUrl)
    return imageUrl
  } catch (error) {
    console.error('메시지 이미지 조회 실패:', error)
    return DefaultImage
  }
}

// setupMessageListener 함수 수정
const setupMessageListener = () => {
  const roomRef = dbRef(database, `chatrooms/${props.roomId}/messages`)
  const recentMessagesQuery = query(roomRef, orderByChild('chatCreatedAt'), limitToLast(PAGE_SIZE))

  if (messageListener.value) {
    off(roomRef, messageListener.value)
  }

  const newListener = onValue(recentMessagesQuery, async (snapshot) => {
    const roomData = snapshot.val()
    if (!roomData) return

    try {
      const membersRef = dbRef(database, `chatrooms/${props.roomId}/members`)
      const membersSnapshot = await get(membersRef)
      const members = membersSnapshot.val() || {}

      if (!members[currentUserId.value]) {
        console.log('사용자가 채팅방 멤버가 아님')
        return
      }

      const messageEntries = Object.entries(roomData).sort(
        ([, a], [, b]) => new Date(a.chatCreatedAt) - new Date(b.chatCreatedAt),
      )

      const processedMessages = await Promise.all(
        messageEntries.map(async ([key, msg]) => {
          const userProfileImg = await getUserProfileImage(msg.senderId)
          let messageImg = msg.messageImg
            ? await getMessageImage(msg.messageId || key, msg.messageImg)
            : null

          return {
            messageId: msg.messageId || key,
            roomId: msg.roomId,
            senderId: msg.senderId,
            senderProfileImg: userProfileImg,
            messageContent: msg.messageContent,
            messageImg: messageImg,
            chatCreatedAt: msg.chatCreatedAt,
          }
        }),
      )

      if (isInitialLoad.value) {
        messages.value = processedMessages
        isInitialLoad.value = false
        if (processedMessages.length > 0) {
          oldestMessageTime.value = processedMessages[0].chatCreatedAt
        }
        await nextTick()
        scrollToBottom()
      } else {
        const lastExistingMessageTime =
          messages.value.length > 0 ? messages.value[messages.value.length - 1].chatCreatedAt : 0

        const newMessages = processedMessages.filter(
          (msg) => new Date(msg.chatCreatedAt) > new Date(lastExistingMessageTime),
        )

        if (newMessages.length > 0) {
          messages.value = [...messages.value, ...newMessages]
          await nextTick()
          scrollToBottom()
        }
      }
    } catch (error) {
      console.error('메시지 처리 중 오류:', error)
    }
  })

  messageListener.value = newListener
}

// 이전 메시지 로드
const loadMoreMessages = async () => {
  if (isLoadingMore.value || !hasMoreMessages.value || !oldestMessageTime.value) return

  try {
    isLoadingMore.value = true

    const roomRef = dbRef(database, `chatrooms/${props.roomId}/messages`)
    const olderMessagesQuery = query(
      roomRef,
      orderByChild('chatCreatedAt'),
      endBefore(oldestMessageTime.value),
      limitToLast(PAGE_SIZE),
    )

    const snapshot = await get(olderMessagesQuery)
    const olderMessages = snapshot.val()

    if (!olderMessages) {
      hasMoreMessages.value = false
      return
    }

    const olderMessageArray = await Promise.all(
      Object.entries(olderMessages).map(async ([key, msg]) => {
        const userProfileImg = await getUserProfileImage(msg.senderId)
        let messageImg = null
        if (msg.hasImage) {
          messageImg = await getMessageImage(msg.messageId || key, msg.messageImg)
        }

        return {
          messageId: msg.messageId || key,
          roomId: msg.roomId,
          senderId: msg.senderId,
          senderProfileImg: userProfileImg,
          messageContent: msg.messageContent,
          messageImg: messageImg,
          chatCreatedAt: msg.chatCreatedAt,
        }
      }),
    ).sort((a, b) => new Date(a.chatCreatedAt) - new Date(b.chatCreatedAt))

    if (olderMessageArray.length > 0) {
      oldestMessageTime.value = olderMessageArray[0].chatCreatedAt
      messages.value = [...olderMessageArray, ...messages.value]
    }

    hasMoreMessages.value = olderMessageArray.length === PAGE_SIZE
  } catch (error) {
    console.error('이전 메시지 로드 실패:', error)
  } finally {
    isLoadingMore.value = false
  }
}

// 스크롤 이벤트 핸들러
const handleScroll = async (event) => {
  const { scrollTop } = event.target
  if (scrollTop < 100 && !isLoadingMore.value && hasMoreMessages.value) {
    await loadMoreMessages()
  }
}

const scrollToBottom = async () => {
  try {
    await nextTick()
    if (messagesContainer.value && !isLoadingMore.value) {
      const container = messagesContainer.value
      if (container.scrollHeight > container.clientHeight) {
        container.scrollTop = container.scrollHeight
      }
    }
  } catch (error) {
    console.error('스크롤 이동 중 오류:', error)
  }
}

// API 통신
const loadItemInfo = async () => {
  try {
    const response = await axios.get(`/item/rent/${props.itemId}`)
    itemInfo.value = response.data
    console.log(itemInfo.value)
  } catch (error) {
    console.error('물품 정보 로드 실패:', error)
  }
}

// 메시지 전송 함수
const sendMessage = async () => {
  if (!newMessage.value.trim() && !selectedImage.value) return

  try {
    const formData = new FormData()
    if (selectedImage.value) {
      formData.append('image', selectedImage.value)
    }
    if (newMessage.value.trim()) {
      formData.append('messageContent', newMessage.value)
    }

    const endpoint = selectedImage.value
      ? `/chat/rooms/${props.roomId}/images`
      : `/chat/rooms/${props.roomId}/messages`

    const payload = selectedImage.value ? formData : { messageContent: newMessage.value }
    const config = selectedImage.value ? { headers: { 'Content-Type': 'multipart/form-data' } } : {}

    await axios.post(endpoint, payload, config)

    newMessage.value = ''
    clearPreview()
  } catch (error) {
    console.error('메시지 전송 실패:', error)
  }
}

// 이미지 관련 함수들
const handleImageSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  const maxSize = 5 * 1024 * 1024
  if (file.size > maxSize) {
    alert('파일 크기는 5MB를 초과할 수 없습니다.')
    clearPreview()
    return
  }

  selectedImage.value = file
  previewFileName.value = file.name
  previewImage.value = URL.createObjectURL(file)
}

// 이미지 로드 핸들러
const handleImageLoad = (event) => {
  const img = event.target
  if (img.src) {
    imageLoadStatus.value[img.src] = true
  }
}

// 이미지 에러 핸들러
const handleImageError = (event, type = 'default') => {
  event.target.src = type === 'profile' ? DefaultProfileImg : DefaultImage
}

// 이미지 뷰어 함수
const openImageViewer = (imageUrl) => {
  if (imageUrl) {
    viewerImage.value = imageUrl
    showImageViewer.value = true
  }
}

const closeImageViewer = () => {
  viewerImage.value = null
  showImageViewer.value = false
}

const clearPreview = () => {
  if (previewImage.value) {
    URL.revokeObjectURL(previewImage.value)
  }
  selectedImage.value = null
  previewImage.value = null
  previewFileName.value = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const formatPrice = (price) => {
  if (price == null || isNaN(price)) return '0'
  return Number(price).toLocaleString()
}

const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })
}

const formatFileSize = (size) => {
  if (!size) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB']
  const i = Math.floor(Math.log(size) / Math.log(k))
  return parseFloat((size / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 예약하기 버튼 핸들러 추가
const handleReservation = () => {
  // chatStore를 통해 채팅창 닫기
  chatStore.closeChat()

  router.push({
    name: 'Reservation',
    params: {
      lessorId: currentUserId.value,
      lesseeId: messages.value[messages.value.length - 1]?.senderId,
      itemId: props.itemId.toString(),
    },
  })
}

// 생명주기 훅
onMounted(async () => {
  try {
    isLoading.value = true
    await setupFirebaseAuth()
    await loadItemInfo()
    setupMessageListener()
  } catch (error) {
    console.error('초기 데이터 로드 실패:', error)
  } finally {
    isLoading.value = false
  }
})

// Cleanup
onUnmounted(() => {
  if (messageListener.value) {
    const roomRef = dbRef(database, `chatrooms/${props.roomId}/messages`)
    off(roomRef, messageListener.value)
    messageListener.value = null
  }
  clearPreview()
  imageCache.value.clear()
})
</script>

<style scoped>
.chat-room-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #fff;
}

.header {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
  background: white;
  justify-content: space-between; /* 추가 */
}

.back-button {
  background: none;
  border: none;
  font-size: 24px;
  padding: 0;
  margin-right: 15px;
  cursor: pointer;
}

.seller-name {
  font-size: 16px;
  font-weight: 500;
}

.item-info {
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.item-content {
  display: flex;
  align-items: center;
}

.item-img {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
  background-color: #f0f0f0;
}

/* 이미지 로딩 중 스켈레톤 효과 */
.image-loading {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    background-color: #f0f0f0;
  }
  50% {
    background-color: #e0e0e0;
  }
  100% {
    background-color: #f0f0f0;
  }
}

.item-details {
  margin-left: 12px;
}

.item-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 2px;
}

.item-area {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.item-price {
  font-size: 14px;
  font-weight: 600;
  color: #ff3b30;
}

.messages-container {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  background: #f8f8f8;
}

.loading-indicator {
  text-align: center;
  padding: 10px;
  color: #666;
  font-size: 14px;
}

.message-wrapper {
  display: flex;
  margin-bottom: 15px;
}

.my-message {
  justify-content: flex-end;
}

.other-message {
  justify-content: flex-start;
}

.sender-profile {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  margin-right: 8px;
}

.message-box {
  max-width: 70%;
  padding: 10px 12px;
  border-radius: 12px;
  position: relative;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.message-image-container {
  width: 100%;
  max-width: 240px;
  margin-bottom: 4px;
}

.message-image {
  width: 100%;
  max-width: 240px;
  border-radius: 8px;
  cursor: pointer;
}

.message-text {
  word-break: break-word;
  white-space: pre-wrap;
}

.my-box {
  background: #ff3b30;
  color: white;
  border-top-right-radius: 4px;
}

.other-box {
  background: #f1f1f1;
  color: black;
  border-top-left-radius: 4px;
}

.message-time {
  font-size: 12px;
  margin-top: 4px;
}

.my-time {
  text-align: right;
  color: #666;
}

.other-time {
  text-align: left;
  color: #666;
}

.image-preview {
  padding: 12px;
  border-top: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  background: white;
}

.preview-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.preview-info {
  flex: 1;
  overflow: hidden;
}

.preview-filename {
  display: block;
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.preview-size {
  display: block;
  font-size: 12px;
  color: #666;
}

.preview-close {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
}

.input-container {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  border-top: 1px solid #eee;
  background: white;
}

.hidden {
  display: none;
}

.attach-button {
  background: none;
  border: none;
  font-size: 20px;
  padding: 8px;
  cursor: pointer;
}

.message-input {
  flex: 1;
  border: none;
  padding: 8px 12px;
  margin: 0 8px;
  border-radius: 20px;
  background: #f1f1f1;
}

.message-input:focus {
  outline: none;
  background: #e9e9e9;
}

.send-button {
  background: none;
  border: none;
  color: #ff3b30;
  font-size: 24px;
  padding: 8px;
  cursor: pointer;
}

.send-button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

/* 스크롤바 스타일링 */
.messages-container::-webkit-scrollbar {
  width: 6px;
}

.messages-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.messages-container::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* 로딩 상태 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  color: #666;
}

.reservation-button {
  background: #ff3b30;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  margin-left: auto; /* 추가 */
}

.reservation-button:hover {
  background: #ff1a1a;
}
</style>
