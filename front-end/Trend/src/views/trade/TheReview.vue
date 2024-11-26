<template>
  <DefaultLayout>
    <div class="review-container">
      <div v-if="isLoading" class="loading">로딩 중...</div>
      <div v-else>
        <h1 class="title">상품 후기를 남겨주세요</h1>

        <div class="item-info">
          <img :src="itemData.thumbnail" alt="상품 이미지" class="item-image" />
          <div class="item-details">
            <div class="item-name">{{ itemData.itemName }}</div>
            <div class="item-price">{{ formatPrice(itemData.itemPrice) }}₩</div>
            <div class="item-address">{{ itemData.address }}</div>
          </div>
        </div>

        <div class="rating-section">
          <div class="rating-label">상품에 대한 별점을 남겨주세요</div>
          <div class="star-rating">
            <span
              v-for="index in 5"
              :key="index"
              class="star"
              :class="{ active: index <= rating }"
              @click="setRating(index)"
            >
              ★
            </span>
          </div>
        </div>

        <div class="content-section">
          <div class="content-label">본문 입력</div>
          <textarea
            v-model="reviewContent"
            class="review-content"
            placeholder="후기를 작성해주세요"
          ></textarea>
        </div>

        <button @click="submitReview" class="submit-button">등록</button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { tradeApi } from '@/api/tradeApi'
import { getStorage, ref as storageRef, getDownloadURL } from 'firebase/storage'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

const route = useRoute()
const router = useRouter()
const itemData = ref({})
const rating = ref(0)
const reviewContent = ref('')
const isLoading = ref(true)
const tradeId = route.params.id
const authStore = useAuthStore()
const { userId } = storeToRefs(authStore)

const getItemData = async () => {
  try {
    isLoading.value = true
    const response = await tradeApi.getItemInfoForReview(tradeId)
    const storage = getStorage()
    const imageRef = storageRef(storage, response.data.thumbnail)
    const imageUrl = await getDownloadURL(imageRef)

    itemData.value = {
      ...response.data,
      thumbnail: imageUrl,
    }
  } catch (error) {
    console.error('상품 정보 로딩 실패:', error)
  } finally {
    isLoading.value = false
  }
}
const setRating = (value) => {
  rating.value = value
}

const formatPrice = (price) => {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const submitReview = async () => {
  if (rating.value === 0) {
    alert('별점을 선택해주세요.')
    return
  }

  if (!reviewContent.value.trim()) {
    alert('후기 내용을 입력해주세요.')
    return
  }

  try {
    const requestBody = {
      tradeId: tradeId,
      rating: rating.value,
      reviewContent: reviewContent.value,
    }
    console.log(requestBody)
    await tradeApi.insertReview(requestBody)
    alert('후기가 등록되었습니다.')
    router.push({ name: 'ItemList', params: { id: userId } })
  } catch (error) {
    console.error('후기 등록 실패:', error)
    alert('후기 등록에 실패했습니다.')
  }
}

onMounted(() => {
  getItemData()
})
</script>

<style scoped>
.review-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 24px;
}

.title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
}

.item-info {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 24px;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 16px;
}

.item-details {
  flex: 1;
}

.item-name {
  font-weight: 600;
  margin-bottom: 4px;
}

.item-price {
  color: #666;
  margin-bottom: 4px;
}

.item-address {
  font-size: 14px;
  color: #888;
}

.rating-section {
  margin-bottom: 24px;
}

.rating-label {
  margin-bottom: 12px;
  font-weight: 500;
}

.star-rating {
  display: flex;
  gap: 8px;
}

.star {
  font-size: 28px;
  cursor: pointer;
  color: #ddd;
  transition: color 0.2s;
}

.star.active {
  color: #ffd700;
}

.content-section {
  margin-bottom: 24px;
}

.content-label {
  margin-bottom: 12px;
  font-weight: 500;
}

.review-content {
  width: 100%;
  height: 200px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: none;
  font-size: 16px;
}

.submit-button {
  width: 30%;
  padding: 16px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s;
  display: block;
  margin: 0 auto;
}

.submit-button:hover {
  opacity: 0.9;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  font-size: 16px;
  color: #666;
}
</style>
