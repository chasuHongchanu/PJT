<template>
  <nav class="navbar">
    <!-- Logo Section -->
    <div class="navbar-logo" @click="goToHome">
      <img src="@/assets/logo.png" alt="Trend" class="logo-image" />
      <span class="logo-text">Trend</span>
    </div>

    <!-- Desktop Navigation Links -->
    <div class="navbar-center desktop-only">
      <router-link to="/rental" class="nav-link">여행 물품 대여</router-link>
      <router-link to="/course" class="nav-link">여행 코스</router-link>
      <router-link to="/community" class="nav-link">커뮤니티</router-link>
    </div>

    <!-- Right Section -->
    <div class="navbar-right">
      <!-- Chat Button (Always Visible) -->
      <button class="icon-button" @click="openChat">
        <img src="@/assets/nav/chat-icon.svg" alt="Chat" class="icon" />
      </button>

      <!-- Profile Section (Desktop Only) -->
      <div
        v-if="isAuthenticated"
        class="profile-section desktop-only"
        @click="goToProfile"
      >
        <img
          :src="userProfile?.profileImage || '/default-profile.jpg'"
          alt="Profile"
          class="profile-image"
        />
      </div>
      <button v-else class="profile-button desktop-only" @click="goToLogin">
        <p>로그인</p>
      </button>

      <!-- Mobile Menu Button -->
      <button class="icon-button" @click="toggleMenu">
        <img src="@/assets/nav/menu-icon.svg" alt="Menu" class="icon" />
      </button>
    </div>
  </nav>

  <!-- Mobile Side Menu -->
  <div v-show="isMenuOpen" class="mobile-menu-backdrop">
    <Transition name="slide">
      <div v-show="isMenuOpen" class="mobile-menu-content">
        <SideMenu @close="closeMenu" />
      </div>
    </Transition>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import SideMenu from './menu/SideMenu.vue'

export default {
  name: 'NavigationBar',
  components: {
    SideMenu
  },
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const { isAuthenticated, userProfile } = storeToRefs(authStore)
    const isMenuOpen = ref(false)

    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value
    }

    const closeMenu = () => {
      isMenuOpen.value = false
    }

    const goToHome = () => router.push('/')
    const goToLogin = () => router.push('/auth/login')
    const goToProfile = () => router.push('/user/profile/info')
    const openChat = () => {
      // 채팅 팝업 로직 구현
      console.log('Open chat')
    }

    return {
      isAuthenticated,
      userProfile,
      isMenuOpen,
      toggleMenu,
      closeMenu,
      goToHome,
      goToLogin,
      goToProfile,
      openChat
    }
  }
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
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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

/* 중앙 정렬된 네비게이션 링크 */
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
  width: 24px;
  height: 24px;
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

/* 배경 스타일 */
.mobile-menu-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

/* 메뉴 콘텐츠 스타일 */
.mobile-menu-content {
  position: fixed;
  top: 60px;
  right: 0;
  bottom: 0;
  width: 280px;
  background: white;
  box-shadow: -2px 0 4px rgba(0,0,0,0.1);
  z-index: 1000;
}

/* 메뉴 슬라이드 애니메이션 */
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}

/* Responsive Styles */
@media (min-width: 768px) {
  .mobile-only {
    display: none;
  }

  .desktop-only {
    display: flex;
  }

  /* 데스크톱에서 메뉴 버튼 숨기기 */
  .icon-button:last-child {
    display: none;
  }
}

@media (max-width: 767px) {
  .mobile-only {
    display: block;
  }

  .desktop-only {
    display: none;
  }
}
</style>