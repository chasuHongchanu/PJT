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

    <!-- 지도 -->
    <div class="map-container">
      <GoogleMap :api-key="googleMapsApiKey" :spots="courseData.spots" :zoom="13" />
    </div>

    <!-- 코스 목록 -->
    <div class="spots-list">
      <div v-for="spot in courseData.spots" :key="spot.spotId" class="spot-item">
        <div class="spot-order">{{ spot.visitOrder }}</div>
        <div class="spot-info">
          <h3>{{ spot.spotName }}</h3>
          <p>{{ spot.address }}</p>
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
          <path
            d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"
          ></path>
        </svg>
      </button>
      <span class="like-count">{{ courseData.likeCount }}</span>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import GoogleMap from './GoogleMapComponent.vue' // 구글 맵 컴포넌트는 별도 구현 필요

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
    const googleMapsApiKey = 'YOUR_GOOGLE_MAPS_API_KEY' // 실제 키로 대체 필요

    const formatDate = (timestamp) => {
      return new Date(timestamp).toLocaleDateString()
    }

    return {
      googleMapsApiKey,
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
  gap: 8px;
  margin-bottom: 16px;
}

.profile-image {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  font-weight: 500;
}

.region {
  color: #666;
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
  margin-bottom: 20px;
}

.dot {
  margin: 0 8px;
}

.map-container {
  height: 300px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.spots-list {
  margin-bottom: 24px;
}

.spot-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.spot-order {
  width: 24px;
  height: 24px;
  background: #ff5a5a;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.content {
  line-height: 1.6;
  margin-bottom: 24px;
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
}

.like-button:hover {
  background: #f5f5f5;
}

.like-count {
  color: #666;
}
</style>
