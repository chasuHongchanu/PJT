<template>
  <DefaultLayout>
    <div class="profile-page">
      <!-- 판매자 정보 -->
      <ItemLessorInfo
        v-if="lessorData.lessorId"
        :lessor-id="lessorData.lessorId"
        :profile-img="lessorData.lessorProfileImg"
        :nickname="lessorData.lessorNickname"
        :reputation="lessorData.lessorReputation"
        :review-count="lessorData.lessorReviewCount"
        :trade-count="lessorData.lessorTradeCount"
      />

      <!-- 거래 후기 -->
      <section class="section-container">
        <div class="section-header">
          <h2>거래 후기</h2>
          <div class="slide-controls">
            <button @click="prevReview" :disabled="reviewIndex === 0">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="arrow-icon">
                <path d="M15 18l-6-6 6-6" stroke="currentColor" fill="none" stroke-width="2" />
              </svg>
            </button>
            <button @click="nextReview" :disabled="reviewIndex >= reviews.length - 1">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="arrow-icon">
                <path d="M9 6l6 6-6 6" stroke="currentColor" fill="none" stroke-width="2" />
              </svg>
            </button>
          </div>
        </div>
        <TradeReview v-if="reviews.length" :review="reviews[reviewIndex]" class="slide-item" />
        <div v-else class="empty-state">아직 거래 후기가 없습니다.</div>
      </section>

      <!-- 판매 물품 컴포넌트 -->
      <LessorItems :lessor-id="route.params.id" />
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ItemLessorInfo from '@/components/items/ItemLessorInfo.vue'
import TradeReview from '@/components/items/TradeReview.vue'
import LessorItems from '@/components/items/LessorItems.vue'

const route = useRoute()
const lessorData = ref({
  lessorId: '',
  lessorProfileImg: '',
  lessorNickname: '',
  lessorReputation: 0,
  lessorReviewCount: 0,
  lessorTradeCount: 0,
})
const reviews = ref([])
const articles = ref([])

// 슬라이드 인덱스
const reviewIndex = ref(0)
const articleIndex = ref(0)

// 슬라이드 컨트롤
const prevReview = () => {
  if (reviewIndex.value > 0) reviewIndex.value--
}

const nextReview = () => {
  if (reviewIndex.value < reviews.value.length - 1) reviewIndex.value++
}

const prevArticle = () => {
  if (articleIndex.value > 0) articleIndex.value--
}

const nextArticle = () => {
  if (articleIndex.value < articles.value.length - 1) articleIndex.value++
}

onMounted(async () => {
  try {
    // 판매자 정보 로드
    const lessorResponse = await fetch(
      `http://localhost:8080/api/item/rent/lessor/${route.params.id}`,
    )
    lessorData.value = await lessorResponse.json()

    // 게시글 로드
    const articlesResponse = await fetch(
      `http://localhost:8080/api/item/rent/lessor/${route.params.id}/articles`,
    )
    const articlesData = await articlesResponse.json()
    articles.value = Array.isArray(articlesData.data) ? articlesData.data : []

    // 거래 후기 로드
    const reviewsResponse = await fetch(
      `http://localhost:8080/api/item/rent/lessor/${route.params.id}/reviews`,
    )
    const reviewsData = await reviewsResponse.json()
    reviews.value = Array.isArray(reviewsData.data) ? reviewsData.data : []
  } catch (error) {
    console.error('Error loading lessor data:', error)
  }
})
</script>

<style scoped>
.profile-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.section-container {
  margin-top: 32px;
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.slide-controls {
  display: flex;
  gap: 8px;
}

.slide-controls button {
  width: 32px;
  height: 32px;
  border: 1px solid #eee;
  border-radius: 50%;
  background: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.slide-controls button:hover:not(:disabled) {
  background: #f5f5f5;
}

.slide-controls button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.arrow-icon {
  width: 20px;
  height: 20px;
}

.slide-item {
  transition: all 0.3s ease;
}

.items-grid {
  display: flex;
  flex-direction: column;
  gap: 0; /* 각 아이템 카드의 margin-bottom으로 간격 조절 */
}

.more-button {
  margin-top: 8px;
  width: 100%;
  padding: 12px;
  background: white;
  border: 1px solid #eee;
  border-radius: 8px;
  color: #666;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.more-button:hover {
  background: #f5f5f5;
}

.empty-state {
  text-align: center;
  color: #666;
  padding: 32px;
  background: #f9f9f9;
  border-radius: 8px;
}

@media (max-width: 768px) {
  .items-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .items-grid {
    grid-template-columns: 1fr;
  }
}

.loading-state {
  text-align: center;
  padding: 20px;
  color: #666;
}

.items-grid {
  display: flex;
  flex-direction: column;
  gap: 0;
}
</style>
