<template>
    <Transition name="popup">
      <div v-if="isVisible" class="popup-container">
        <div class="popup-content">
          <button 
            class="popup-item" 
            :class="{ 'current-page': isProfilePage }"
            @click="goToMyPage"
            :disabled="isProfilePage"
          >
            <span>마이페이지</span>
          </button>
          <button class="popup-item text-red" @click="handleLogout">
            <span>로그아웃</span>
          </button>
        </div>
      </div>
    </Transition>
  </template>
  
  <script>
  import { computed } from 'vue'
  import { useRouter } from 'vue-router'
  import { useAuthStore } from '@/stores/auth'
  
  export default {
    name: 'NavProfilePop',
    props: {
      isVisible: {
        type: Boolean,
        required: true
      }
    },
    emits: ['close'],
    setup(props, { emit }) {
      const router = useRouter()
      const authStore = useAuthStore()
  
      const isProfilePage = computed(() => {
        return router.currentRoute.value.name === 'ProfileInfo'
      })
  
      const goToMyPage = async () => {
        if (!isProfilePage.value) {
          await router.push({ name: 'ProfileInfo' })
        }
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
        isProfilePage,
        goToMyPage,
        handleLogout
      }
    }
  }
  </script>
  
  <style scoped>
  .popup-container {
    position: fixed;
    top: 60px;
    right: 20px;
    z-index: 1100;
  }
  
  .popup-content {
    width: 160px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    overflow: hidden;
  }
  
  .popup-item {
    width: 100%;
    padding: 12px 16px;
    text-align: left;
    background: none;
    border: none;
    font-size: 14px;
    color: #333;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .popup-item:not(:disabled):hover {
    background-color: #f5f5f5;
  }
  
  .popup-item:not(:last-child) {
    border-bottom: 1px solid #eee;
  }
  
  .popup-item:disabled {
    cursor: default;
    opacity: 0.7;
  }
  
  .current-page {
    background-color: #f5f5f5;
    color: #999;
  }
  
  .text-red {
    color: #ff5a5a;
  }
  
  /* Animation */
  .popup-enter-active,
  .popup-leave-active {
    transition: opacity 0.2s, transform 0.2s;
  }
  
  .popup-enter-from,
  .popup-leave-to {
    opacity: 0;
    transform: translateY(-8px);
  }
  </style>