<!-- src/views/user/TheProfileInfoView.vue -->
<template>
  <DefaultLayout>
    <div v-if="loading" class="loading">Loading...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="profile-container">
      <ProfileCard :profileData="profileData" />
      <div class="divider"></div>
      <ReviewCarousel :reviews="reviews" />
    </div>
  </DefaultLayout>
</template>

<script>
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ProfileCard from '@/components/user/ProfileCard.vue'
import ReviewCarousel from '@/components/user/ReviewCarousel.vue'
import { userApi } from '@/api/userApi'
import { useAuthStore } from '@/stores/auth'
import { firebaseUtils } from '@/utils/firebaseUtils'
import { onBeforeMount, ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'TheProfileInfoView',
  components: {
    DefaultLayout,
    ProfileCard,
    ReviewCarousel,
  },
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()
    const loading = ref(true)
    const error = ref(null)
    const profileData = ref({
      name: '',
      email: '',
      location: '',
      imageUrl: '',
      introduction: '',
      rating: 0,
      createdAt: '',
    })
    const reviews = ref([/* ... */])

    const fetchUserInfo = async () => {
      try {
        // 인증 초기화가 완료될 때까지 대기
        if (!authStore.isInitialized) {
          await authStore.initializeAuth()
        }

        // 인증 상태 확인
        if (!authStore.isAuthenticated) {
          router.push('/login')
          return
        }

        const userData = await userApi.getUserInfo()
        const imageUrl = await firebaseUtils.getImageUrl(userData.userProfileImg)

        profileData.value = {
          name: userData.userNickname,
          email: userData.userEmail,
          location: `${userData.country} ${userData.userAddress}`,
          imageUrl: imageUrl,
          introduction: userData.userIntroduction,
          rating: userData.userRating,
          createdAt: new Date(userData.userCreatedAt).toLocaleDateString(),
        }
      } catch (error) {
        console.error('유저 정보 조회 실패:', error)
        if (error.response?.status === 401) {
          try {
            await authStore.refreshToken()
            // 토큰 갱신 성공 시 다시 시도
            return await fetchUserInfo()
          } catch (refreshError) {
            router.push('/login')
          }
        } else {
          error.value = '유저 정보를 불러오는데 실패했습니다.'
        }
      } finally {
        loading.value = false
      }
    }

    onBeforeMount(fetchUserInfo)

    return {
      loading,
      error,
      profileData,
      reviews,
    }
  },
}
</script>

<style scoped>
.loading, .error {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  font-size: 1.2rem;
  color: #666;
}

.error {
  color: #dc3545;
}
.profile-container {
  width: 100%;
  padding: 20px;
  background-color: var(--color-white);
}

.profile-page {
  width: 90%; /* 양쪽에 5%씩 여백 */
  max-width: 600px; /* 최대 너비 설정 */
  margin: 0 auto;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #eee;
  margin: 24px 0;
}

/* 태블릿 이상 디바이스에서의 여백 조정 */
@media (min-width: 768px) {
  .profile-container {
    padding: 24px;
  }

  .profile-page {
    width: 85%; /* 더 큰 화면에서는 여백을 더 늘림 */
  }
}

/* 작은 모바일 화면에서의 여백 조정 */
@media (max-width: 375px) {
  .profile-container {
    padding: 16px;
  }

  .profile-page {
    width: 95%; /* 매우 작은 화면에서는 여백을 줄임 */
  }
}
</style>
