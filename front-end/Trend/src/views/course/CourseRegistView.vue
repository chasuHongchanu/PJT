<template>
  <DefaultLayout>
    <div class="course-regist-container">
      <h1 class="page-title">코스 등록</h1>

      <!-- 제목 입력 -->
      <div class="input-group">
        <input
          v-model="courseTitle"
          type="text"
          class="title-input"
          placeholder="제목을 입력하세요"
        />
      </div>

      <!-- 주소 입력 -->
      <div class="input-group">
        <input
          v-model="region"
          type="text"
          class="region-input"
          placeholder="지역을 입력하세요"
        />
      </div>

      <!-- 관광지 선택 컴포넌트 -->
      <div class="spot-selector-wrapper">
        <SpotSelector v-model="selectedSpots" />
      </div>

      <!-- 본문 입력 -->
      <div class="input-group">
        <textarea
          v-model="courseContent"
          class="content-input"
          placeholder="코스에 대한 설명을 입력하세요"
          rows="6"
        ></textarea>
      </div>

      <!-- 버튼 그룹 -->
      <div class="button-group">
        <button class="cancel-button" @click="goToList">취소</button>
        <button class="submit-button" @click="submitCourse" :disabled="isSubmitting">
          등록
        </button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import SpotSelector from '@/components/course/SpotSelector.vue'
import axiosInstance from '@/api/axiosInstance'

export default {
  name: 'CourseRegistView',

  components: {
    DefaultLayout,
    SpotSelector
  },

  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    const courseTitle = ref('')
    const region = ref('')
    const courseContent = ref('')
    const selectedSpots = ref([])
    const isSubmitting = ref(false)

    // 코스 등록 요청
    const submitCourse = async () => {
      if (!validateForm()) return

      try {
        isSubmitting.value = true
        
        // FormData 생성
        const formData = new FormData()
        
        // 각 필드를 개별적으로 FormData에 추가
        formData.append('courseWriterId', authStore.userId)
        formData.append('courseTitle', courseTitle.value)
        formData.append('region', region.value)
        formData.append('courseContent', courseContent.value)
        
        // spotList를 JSON 문자열로 변환하여 추가
        const spotList = selectedSpots.value.map((spot, index) => ({
          spotId: spot.spotId,
          spotName: spot.spotName,
          visitOrder: index + 1,
          region: spot.region,
          latitude: spot.latitude,
          longitude: spot.longitude
        }))
        formData.append('spotListJson', JSON.stringify(spotList))
        
        // 이미지 리스트 추가 (빈 배열)
        formData.append('imageList', JSON.stringify([]))

        // Content-Type 헤더를 설정하지 않음 (브라우저가 자동으로 설정)
        await axiosInstance.post('/course', formData)
        router.push({ name: 'CourseList' })
        alert('코스 등록을 완료했습니다.')
      } catch (error) {
        console.error('Failed to submit course:', error)
        alert('코스 등록에 실패했습니다. 다시 시도해주세요.')
      } finally {
        isSubmitting.value = false
      }
    }

    // 폼 유효성 검사
    const validateForm = () => {
      if (!courseTitle.value.trim()) {
        alert('제목을 입력해주세요.')
        return false
      }
      if (!region.value.trim()) {
        alert('지역을 입력해주세요.')
        return false
      }
      if (selectedSpots.value.length === 0) {
        alert('최소 1개 이상의 관광지를 선택해주세요.')
        return false
      }
      if (!courseContent.value.trim()) {
        alert('코스 설명을 입력해주세요.')
        return false
      }
      return true
    }

    // 목록으로 이동
    const goToList = () => {
      router.push('/course/list')
    }

    return {
      courseTitle,
      region,
      courseContent,
      selectedSpots,
      isSubmitting,
      submitCourse,
      goToList
    }
  }
}
</script>

<style scoped>
.course-regist-container {
  max-width: 768px;
  margin: 0 auto;
  padding: 20px 16px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 24px;
}

.input-group {
  margin-bottom: 20px;
}

.title-input {
  width: 100%;
  padding: 12px;
  font-size: 18px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.region-input {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: vertical;
}

.spot-selector-wrapper {
  margin-bottom: 20px;
}

.content-input {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: vertical;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.cancel-button,
.submit-button {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.cancel-button {
  background: white;
  border: 1px solid #e0e0e0;
  color: #666;
}

.submit-button {
  background: #ff5a5a;
  border: none;
  color: white;
}

.submit-button:disabled {
  background: #cccccc;
  cursor: not-allowed;
}

/* 반응형 스타일 */
@media (max-width: 640px) {
  .course-regist-container {
    padding: 16px;
  }

  .page-title {
    font-size: 20px;
    margin-bottom: 20px;
  }

  .title-input {
    font-size: 16px;
  }

  .cancel-button,
  .submit-button {
    padding: 10px 20px;
    font-size: 14px;
  }
}
</style>