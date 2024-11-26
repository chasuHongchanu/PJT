<template>
  <nav class="navbar">
    <!-- Logo Section -->
    <div class="navbar-logo" @click="goToHome">
      <img src="@/assets/logo.png" alt="Trend" class="logo-image" />
      <span class="logo-text">Trend</span>
    </div>

    <!-- Desktop Navigation Links -->
    <div class="navbar-center desktop-only">
      <router-link to="/items/view" class="nav-link">여행 물품 대여</router-link>
      <router-link to="/course/list" class="nav-link">여행 코스</router-link>
      <router-link to="/community" class="nav-link">커뮤니티</router-link>
    </div>

    <!-- Right Section -->
    <div class="navbar-right">
      <!-- Chat Button (Only Visible when Authenticated) -->
      <button v-if="isAuthenticated" class="icon-button" @click="toggleChat">
        <img src="@/assets/nav/chat-icon.svg" alt="Chat" class="icon" />
      </button>

      <!-- Profile Section (Desktop Only) -->
      <div v-if="isAuthenticated" class="profile-section desktop-only" @click="toggleProfilePopup">
        <img v-if="isAuthenticated" :src="profileImage" class="profile-image" alt="Profile" />
        <NavProfilePop :is-visible="isProfilePopupOpen" @close="closeProfilePopup" />
      </div>
      <button v-else class="profile-button desktop-only" @click="goToLogin">
        <p>로그인</p>
      </button>

      <!-- Mobile Menu Button -->
      <button class="icon-button mobile-only" @click="toggleMenu">
        <img src="@/assets/nav/menu-icon.svg" alt="Menu" class="icon" />
      </button>
    </div>
  </nav>

  <!-- Mobile Menu Container -->
  <Transition name="fade">
    <div v-if="isMenuOpen" class="mobile-menu-container">
      <!-- Backdrop -->
      <div class="mobile-menu-backdrop" @click="closeMenu"></div>

      <!-- Menu Content with Animation -->
      <Transition name="slide">
        <div v-if="isMenuOpen" class="mobile-menu-content">
          <SideMenu @close="closeMenu" />
        </div>
      </Transition>
    </div>
  </Transition>

  <!-- Chat Container -->
  <Transition name="chat-transition">
    <div v-if="isChatOpen" class="chat-overlay">
      <!-- Desktop Backdrop -->
      <div v-if="!isMobile" class="chat-backdrop" @click="toggleChat"></div>

      <div :class="['chat-container', { mobile: isMobile, desktop: !isMobile }]">
        <!-- Mobile Close Button -->
        <button v-if="isMobile" class="mobile-close-button" @click="toggleChat">
          <span class="close-icon">×</span>
        </button>

        <Transition name="fade" mode="out-in">
          <ChatRoom
            v-if="selectedRoomId"
            :key="'room-' + selectedRoomId"
            :room-id="selectedRoomId"
            :item-id="selectedItemId"
            @back="closeRoom"
          />
          <ChatList v-else :key="'list'" @select-room="selectRoom" />
        </Transition>
      </div>
    </div>
  </Transition>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import SideMenu from './menu/SideMenu.vue'
import NavProfilePop from './NavProfilePop.vue'
import ChatList from '@/components/chat/ChatList.vue'
import ChatRoom from '@/components/chat/ChatRoom.vue'
import { useChatStore } from '@/stores/chat'

export default {
  name: 'NavigationBar',
  components: {
    SideMenu,
    NavProfilePop,
    ChatList,
    ChatRoom,
  },
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const { isAuthenticated, profileImage } = storeToRefs(authStore)

    const isMenuOpen = ref(false)
    const isProfilePopupOpen = ref(false)

    // 채팅 관련 상태
    const isMobile = ref(window.innerWidth < 768)
    const chatStore = useChatStore()
    const { isChatOpen, selectedRoomId, selectedItemId } = storeToRefs(chatStore)

    // 채팅 기능
    const toggleChat = () => {
      if (isChatOpen.value) {
        chatStore.closeChat()
      } else {
        chatStore.openChat()
      }
    }

    const selectRoom = (roomId, itemId) => {
      chatStore.selectRoom(roomId, itemId)
    }

    const closeRoom = () => {
      selectedRoomId.value = null
      selectedItemId.value = null
    }

    // 반응형 처리
    const handleResize = () => {
      isMobile.value = window.innerWidth < 768
      if (window.innerWidth >= 768 && isMenuOpen.value) {
        isMenuOpen.value = false
      }
    }

    const toggleProfilePopup = (e) => {
      e.stopPropagation()
      isProfilePopupOpen.value = !isProfilePopupOpen.value
    }

    const handleClickOutside = (e) => {
      if (isProfilePopupOpen.value) {
        closeProfilePopup()
      }
    }

    onMounted(() => {
      window.addEventListener('resize', handleResize)
      document.addEventListener('click', handleClickOutside)
    })

    onUnmounted(() => {
      window.removeEventListener('resize', handleResize)
      document.removeEventListener('click', handleClickOutside)
    })

    const closeProfilePopup = () => {
      isProfilePopupOpen.value = false
    }

    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value
    }

    const closeMenu = () => {
      isMenuOpen.value = false
    }

    const goToHome = () => router.push('/')
    const goToLogin = () => router.push('/auth/login')

    return {
      isAuthenticated,
      profileImage,
      isMenuOpen,
      toggleMenu,
      closeMenu,
      goToHome,
      goToLogin,
      isProfilePopupOpen,
      toggleProfilePopup,
      closeProfilePopup,
      isChatOpen,
      selectedRoomId,
      selectedItemId,
      isMobile,
      toggleChat,
      selectRoom,
      closeRoom,
    }
  },
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.navbar-logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-image {
  height: 24px;
  margin-right: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #ff5a5a;
}

.navbar-center {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 24px;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.nav-link:hover {
  background-color: #f5f5f5;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: auto;
}

.icon-button {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
}

.icon {
  width: 36px;
  height: 36px;
  stroke-width: 25;
}

.profile-section {
  cursor: pointer;
}

.profile-image {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

/* 모바일 메뉴 스타일 */
.mobile-menu-container {
  position: fixed;
  inset: 0;
  z-index: 999;
}

.mobile-menu-backdrop {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
}

.mobile-menu-content {
  position: absolute;
  top: 60px;
  right: 0;
  bottom: 0;
  width: 280px;
  background: white;
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
}

/* 채팅 컨테이너 스타일 */
.chat-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;
  pointer-events: none;
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  padding: 20px;
}

.chat-backdrop {
  position: fixed;
  inset: 0;
  background: transparent;
  pointer-events: auto;
}

.chat-container {
  position: relative;
  background: white;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  pointer-events: auto;
}

/* 모바일 버전 스타일 */
.chat-container.mobile {
  position: fixed;
  top: 60px; /* 네비게이션 바 높이만큼 띄우기 */
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%; /* 전체 너비 사용 */
  height: calc(100vh - 60px); /* 네비게이션 바 제외한 전체 높이 */
  margin: 0;
  border-radius: 0; /* 모서리 둥글게 제거 */
  box-shadow: none; /* 그림자 제거 */
}

/* 데스크톱 버전 스타일 */
.chat-container.desktop {
  width: 380px;
  height: 600px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  max-height: calc(100vh - 80px);
}

.mobile-close-button {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.1);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1001;
}

.close-icon {
  font-size: 24px;
  line-height: 1;
  color: #666;
}

/* 트랜지션 애니메이션 */
.fade-enter-active,
.fade-leave-active,
.chat-transition-enter-active,
.chat-transition-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.chat-transition-enter-from,
.chat-transition-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}

.slide-enter-to,
.slide-leave-from {
  transform: translateX(0);
}

/* 반응형 스타일 */
@media (min-width: 768px) {
  .mobile-only {
    display: none;
  }

  .desktop-only {
    display: flex;
  }
}

@media (max-width: 767px) {
  .mobile-only {
    display: block;
  }

  .desktop-only {
    display: none;
  }

  .chat-overlay {
    padding: 0;
    align-items: flex-start; /* 모바일에서는 상단 정렬 */
    justify-content: flex-start; /* 모바일에서는 좌측 정렬 */
  }

  .chat-transition-enter-from,
  .chat-transition-leave-to {
    transform: translateY(100%);
  }
}
</style>
