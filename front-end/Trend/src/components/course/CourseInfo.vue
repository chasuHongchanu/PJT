<template>
  <div class="course-info">
    <!-- 작성자 정보 -->
    <div class="writer-info">
      <img :src="courseData.writerProfileImg" alt="Profile" class="profile-image">
      <span class="nickname">{{ courseData.writerNickname }}</span>
    </div>

    <!-- 지역 정보 -->
    <div class="region">{{ courseData.region }}</div>

    <!-- 제목 -->
    <h1 class="title">{{ courseData.courseTitle }}</h1>

    <!-- 메타 정보 -->
    <div class="meta-info">
      <span>{{ formatDate(courseData.courseCreatedAt) }}</span>
      <span class="dot">·</span>
      <span>조회 {{ courseData.viewCount }}</span>
    </div>

    <!-- 코스 정보 영역 -->
    <div class="course-content-area">
      <!-- 지도 -->
      <div class="map-container">
        <GoogleMap
          :api-key="googleMapsApiKey"
          :spots="sortedSpots"
          :connect-markers="true"
          :zoom="13"
        />
      </div>

      <!-- 코스 목록 -->
      <div class="spots-list">
        <h2 class="section-title">코스 목록</h2>
        <div class="spots">
          <div v-for="spot in sortedSpots" :key="spot.spotId" class="spot-item">
            <div class="spot-order">{{ spot.visitOrder }}</div>
            <div class="spot-info">
              <h3 class="spot-name">{{ spot.spotName }}</h3>
              <p class="spot-address">{{ spot.address }}</p>

            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 내용 -->
    <div class="content">
      {{ courseData.courseContent }}
    </div>

    <!-- 좋아요 버튼 -->
    <div class="like-section">
      <button class="like-button" @click="$emit('like-clicked')">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
        </svg>
      </button>
      <span class="like-count">{{ courseData.likeCount }}</span>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import GoogleMap from './GoogleMapComponent.vue'

export default {
  name: 'CourseInfo',

  components: {
    GoogleMap,
  },

  props: {
    courseData: {
      type: Object,
      required: true,
    },
  },

  setup(props) {
    const googleMapsApiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY

    // visitOrder 기준으로 정렬된 spots
    const sortedSpots = computed(() => {
      if (!props.courseData.spots) return []
      return [...props.courseData.spots].sort((a, b) => a.visitOrder - b.visitOrder)
    })

    const formatDate = (timestamp) => {
      if (!timestamp) return ''
      return new Date(timestamp).toLocaleDateString()
    }

    return {
      googleMapsApiKey,
      sortedSpots,
      formatDate,
    }
  },
}
</script>

<style scoped>
.course-info {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.writer-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  font-size: 16px;
  font-weight: 500;
}

.region {
  color: #666;
  font-size: 16px;
  margin-bottom: 8px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
}

.meta-info {
  color: #666;
  font-size: 14px;
  margin-bottom: 24px;
}

.dot {
  margin: 0 8px;
}

.course-content-area {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-bottom: 24px;
}

.map-container {
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.spots-list {
  background: #f8f8f8;
  border-radius: 8px;
  padding: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.spots {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.spot-item {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: white;
  border-radius: 8px;
  align-items: center;
}

.spot-order {
  width: 28px;
  height: 28px;
  background: #ff5a5a;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
}

.spot-info {
  flex: 1;
}

.spot-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.spot-address {
  font-size: 14px;
  color: #666;
}

.content {
  line-height: 1.6;
  margin: 24px 0;
  white-space: pre-wrap;
}

.like-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.like-button {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 1px solid #e0e0e0;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.like-button:hover {
  background: #f5f5f5;
}

.like-count {
  color: #666;
  font-size: 14px;
}
</style>