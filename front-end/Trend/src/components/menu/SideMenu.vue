<template>
  <div class="side-menu">
    <!-- Auth Section -->
    <div v-if="isAuthenticated" class="menu-item profile-item profile" @click="goToProfile">
      <img :src="profileImage" class="profile-image" />
      <div class="profile-info">
        <span class="nickname">{{ userNickname }}</span>
        <span class="profile-role">마이페이지</span>
      </div>
      <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24">
        <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z" fill="currentColor"/>
      </svg>
    </div>
    <div v-else class="menu-item" @click="goToLogin">
      <span>로그인 / 회원가입</span>
      <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24">
        <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z" fill="currentColor"/>
      </svg>
    </div>

    <!-- Navigation Links -->
    <div class="menu-item" @click="goToRental">
      <span>여행 물품 대여</span>
      <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24">
        <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z" fill="currentColor"/>
      </svg>
    </div>
    
    <div class="menu-item" @click="goToCourse">
      <span>여행 코스</span>
      <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24">
        <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z" fill="currentColor"/>
      </svg>
    </div>
    
    <div class="menu-item" @click="goToCommunity">
      <span>커뮤니티</span>
      <svg class="arrow-icon" width="24" height="24" viewBox="0 0 24 24">
        <path d="M8.59 16.59L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.59Z" fill="currentColor"/>
      </svg>
    </div>

    <!-- Logout Button (Only show when authenticated) -->
    <div v-if="isAuthenticated" class="menu-item logout-item" @click="handleLogout">
      <span>로그아웃</span>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'


export default {
  name: 'SideMenu',
  setup(props, { emit }) {
    const router = useRouter()
    const authStore = useAuthStore()
    const { isAuthenticated, userNickname, profileImage } = storeToRefs(authStore)

    const goToLogin = () => {
      router.push('/auth/login')
      emit('close')
    }

    const goToProfile = () => {
      router.push('/user/profile/info')
      emit('close')
    }

    const goToRental = () => {
      router.push('/items/view')
      emit('close')
    }

    const goToCourse = () => {
      router.push('/course')
      emit('close')
    }

    const goToCommunity = () => {
      router.push('/community')
      emit('close')
    }

    const handleLogout = async () => {
      try {
        await authStore.logout()
        router.push('/auth/login')
        emit('close')
      } catch (error) {
        console.error('Logout failed:', error)
      }
    }

    return {
      isAuthenticated,
      userNickname,
      profileImage,
      goToLogin,
      goToProfile,
      goToRental,
      goToCourse,
      goToCommunity,
      handleLogout
    }
  }
}
</script>

<style scoped>
.side-menu {
  padding: 16px 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.profile-item {
  gap: 12px;
}

.profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-info {
  flex: 1;
}

.profile-name {
  display: block;
  font-weight: 500;
  color: #333;
}

.profile-role {
  display: block;
  font-size: 14px;
  color: #666;
}

.arrow-icon {
  color: #999;
  flex-shrink: 0;
}

.logout-item {
  margin-top: auto;
  color: #ff5a5a;
}

.logout-item:hover {
  background-color: #fff1f1;
}
</style>