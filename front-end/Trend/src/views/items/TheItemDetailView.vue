<template>
  <DefaultLayout>
    <div class="item-detail">
      <!-- 데이터 로드 전 로딩 상태 -->
      <div v-if="!itemDetail" class="loading-state">
        <p>로딩 중...</p>
      </div>

      <!-- 데이터 로드 후 -->
      <div v-else>
        <!-- 이미지 슬라이더 -->
        <div class="image-slider">
          <div v-if="firebaseImageUrls.length" class="slider-container">
            <button v-if="currentSlide > 0" class="slide-button prev" @click="prevSlide">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M15 18l-6-6 6-6" />
              </svg>
            </button>

            <button
              v-if="currentSlide < firebaseImageUrls.length - 1"
              class="slide-button next"
              @click="nextSlide"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M9 18l6-6-6-6" />
              </svg>
            </button>

            <div class="slide-track" :style="slideTrackStyle">
              <div v-for="(image, index) in firebaseImageUrls" :key="index" class="slide">
                <img
                  :src="image"
                  :alt="itemDetail.itemName"
                  class="slide-image"
                  @load="handleImageLoad"
                />
              </div>
            </div>

            <!-- 좋아요 버튼 -->
            <button class="like-button" @click="toggleLike" :class="{ liked: isLiked }">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                :fill="isLiked ? 'red' : 'none'"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                />
              </svg>
            </button>

            <!-- 슬라이더 인디케이터 -->
            <div class="slide-indicators">
              <span
                v-for="(_, index) in firebaseImageUrls"
                :key="index"
                :class="{ active: currentSlide === index }"
                @click="goToSlide(index)"
              />
            </div>
          </div>

          <div v-else class="loading-placeholder">
            <p>이미지를 불러오는 중입니다...</p>
          </div>
        </div>

        <!-- 물품 정보 섹션 수정 -->
        <div class="item-info-section">
          <!-- 판매자 정보 -->
          <div class="lessor-info">
            <div class="lessor-left">
              <img
                :src="itemDetail.lessorProfileImage"
                :alt="itemDetail.lessorNickname"
                class="lessor-profile"
              />
              <div class="lessor-details">
                <h3>{{ itemDetail.lessorNickname }}</h3>
                <ReputationIcons :score="itemDetail.lessorReputation" />
              </div>
            </div>
            <!-- 작성자와 현재 사용자가 같으면 수정하기, 다르면 채팅하기 버튼 표시 -->
            <button v-if="isAuthor" class="edit-button" @click="goToEdit">
              수정하기
            </button>
            <button v-else class="chat-button" @click="startChat">채팅하기</button>
          </div>

          <!-- 물품 상세 정보 -->
          <div class="item-details">
            <h1 class="item-name">{{ itemDetail.itemName }}</h1>
            <div class="price-info">
              <span class="price">{{ formatPrice(itemDetail.itemPrice) }}원</span>
              <span class="per-day">/일</span>
            </div>

            <div class="item-meta">
              <p class="category">
                {{ itemDetail.mainCategory }} > {{ itemDetail.subCategory }}
                {{ itemDetail.subSubCategory ? `> ${itemDetail.subSubCategory}` : "" }}
              </p>
              <p class="location">{{ itemDetail.address }}</p>
            </div>

            <div class="rental-dates">
              {{ formatDate(itemDetail.availableRentalStartDate) }} -
              {{ formatDate(itemDetail.availableRentalEndDate) }}
            </div>

            <div class="item-content">
              {{ itemDetail.itemContent }}
            </div>

            <div class="item-stats">
              <div class="stat-item">
                <svg class="stat-icon" viewBox="0 0 24 24">
                  <path
                    d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                  />
                </svg>
                <span>{{ likeCount }}</span>
              </div>
              <div class="stat-item">
                <svg class="stat-icon" viewBox="0 0 24 24">
                  <path
                    d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"
                  />
                </svg>
                <span>{{ itemDetail.viewCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 추천 상품 섹션 -->
        <section class="recommendations">
          <div class="similar-items">
            <h2>비슷한 상품</h2>
            <ItemRecommend :category="itemDetail.mainCategory" />
          </div>

          <div class="nearby-items">
            <h2>근처 다른 상품</h2>
            <ItemRecommend :address="itemDetail.address" />
          </div>
        </section>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ItemRecommend from "@/components/items/ItemRecommend.vue";
import ReputationIcons from "@/components/items/icons/ReputationIcons.vue";
import { storage } from "@/firebase";
import { ref as storageRef, getDownloadURL } from "firebase/storage";

const route = useRoute();
const router = useRouter();
const itemDetail = ref(null);
const currentSlide = ref(0);
const isLiked = ref(false);
const firebaseImageUrls = ref([]); // Firebase Storage URL 배열
const isLoadingImages = ref(false); // 이미지 로딩 상태
const loadedImages = ref(0);
const likeCount = ref(0); // 실제 데이터로 대체 필요

const itemId = ref(route.params.id); // URL의 id 파라미터 받아오기
const currentUserId = "user1";
// 실제 구현 시에는 아래와 같이 사용
// const currentUserId = getCurrentUserId(); // 예: request.session.userId 또는 localStorage에서 가져오기

// 작성자 여부 확인
const isAuthor = computed(() => {
  return itemDetail.value?.lessorId === currentUserId;
});

// 수정 페이지로 이동
const goToEdit = () => {
  router.push({
    name: "Update",
    params: { id: itemId.value },
  });
};
// 슬라이드 트랙 스타일 계산
const slideTrackStyle = computed(() => ({
  transform: `translateX(-${currentSlide.value * 100}%)`,
  transition: "transform 0.3s ease-in-out",
}));

// 이미지 로드 완료 핸들러
const handleImageLoad = () => {
  loadedImages.value++;
};

// 슬라이드 네비게이션
const nextSlide = () => {
  if (currentSlide.value < firebaseImageUrls.value.length - 1) {
    currentSlide.value++;
  }
};

const prevSlide = () => {
  if (currentSlide.value > 0) {
    currentSlide.value--;
  }
};

const formatPrice = (price) => {
  return price.toLocaleString();
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};

const goToSlide = (index) => {
  currentSlide.value = index;
};

const toggleLike = () => {
  isLiked.value = !isLiked.value;
};

// Firebase에서 이미지 URL을 가져오는 함수
const fetchFirebaseImageUrls = async (imagePaths) => {
  try {
    const urls = await Promise.all(
      imagePaths.map(async (path) => {
        const imageReference = storageRef(storage, path);
        return await getDownloadURL(imageReference);
      })
    );
    return urls;
  } catch (error) {
    console.error("Error fetching Firebase URLs:", error);
    return [];
  }
};

const startChat = () => {
  // 채팅 시작 로직 구현
  console.log("Start chat with:", itemDetail.value?.lessorId);
};

onMounted(async () => {
  try {
    console.log("Fetching item details...");
    const response = await fetch(`http://localhost:8080/api/item/rent/${itemId.value}`);
    const data = await response.json();
    itemDetail.value = data;

    console.log("Fetched item detail: ", data);

    // itemImageNames에서 Firebase Storage URL 변환
    if (itemDetail.value.itemImageNames?.length) {
      isLoadingImages.value = true; // 이미지 로딩 상태 활성화
      const imagePaths = itemDetail.value.itemImageNames; // Firebase Storage 경로 배열
      firebaseImageUrls.value = await fetchFirebaseImageUrls(imagePaths);
      isLoadingImages.value = false; // 로딩 완료
    }
  } catch (error) {
    console.error("Error fetching item details:", error);
    isLoadingImages.value = false; // 로딩 실패 시 로딩 상태 해제
  }
});
</script>

<style scoped>
.item-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.image-slider {
  position: relative;
  width: 100%;
  height: 400px;
  overflow: hidden;
  border-radius: 12px;
  margin-bottom: 24px;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.slide-track {
  display: flex;
  width: 100%;
  height: 100%;
}

.slide {
  flex: 0 0 100%;
  height: 100%;
}

.slide-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.slide-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  transition: all 0.2s ease;
}

.slide-button:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-50%) scale(1.1);
}

.slide-button.prev {
  left: 16px;
}

.slide-button.next {
  right: 16px;
}

.slide-button svg {
  width: 24px;
  height: 24px;
}

.loading-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  color: #666;
}

.like-button {
  position: absolute;
  top: 16px;
  right: 16px;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s ease;
}

.like-button svg {
  width: 24px;
  height: 24px;
}

.like-button:hover {
  transform: scale(1.1);
}

.like-button.liked svg {
  fill: #ff3b30;
}

.lessor-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
}

.lessor-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.chat-button {
  padding: 8px 20px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.chat-button:hover {
  background-color: #ff2d20;
}

.lessor-profile {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 12px;
}

.reputation {
  position: relative;
  display: inline-block;
}

.stars {
  position: relative;
  color: #ddd;
}

.stars span {
  position: absolute;
  left: 0;
  color: #ffd700;
  overflow: hidden;
}

.item-details {
  padding: 24px 0;
}

.item-name {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
}

.price-info {
  margin: 16px 0;
}

.price {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.per-day {
  font-size: 14px;
  color: #666;
  margin-left: 4px;
}

.item-meta {
  color: #666;
  font-size: 14px;
  margin-bottom: 24px;
}

.rental-period,
.item-description {
  margin-top: 24px;
}

.rental-period h3,
.item-description h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

section.similar-items,
section.nearby-items {
  margin-top: 40px;
}

section h2 {
  font-size: 20px;
  margin-bottom: 16px;
}

.rental-dates {
  font-size: 18px;
  color: #333;
  margin: 24px 0;
}

.item-content {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  margin: 24px 0;
}

.item-stats {
  display: flex;
  gap: 24px;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.stat-icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

.recommendations {
  margin-top: 48px;
}

.recommendations h2 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
}

.similar-items,
.nearby-items {
  margin-bottom: 40px;
}

.edit-button {
  padding: 8px 20px;
  background-color: #4caf50; /* 수정하기 버튼은 다른 색상 사용 */
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.edit-button:hover {
  background-color: #45a049;
}
</style>
