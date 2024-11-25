<template>
  <div class="item-detail">
    <div class="image-container" @click="goToDetailPage(itemId)">
      <img
        :src="imageUrl"
        :alt="itemName"
        class="item-image"
        @error="handleImageError"
        v-if="imageUrl"
      />
      <div v-else class="image-placeholder">
        <svg xmlns="http://www.w3.org/2000/svg" class="placeholder-icon" viewBox="0 0 24 24">
          <path
            d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"
          />
        </svg>
      </div>
    </div>
    <div class="item-info">
      <h3 class="item-name">{{ itemName }}</h3>
      <p class="item-price">{{ formatPrice(itemPrice) }}원</p>
      <div class="address-container">
        <svg xmlns="http://www.w3.org/2000/svg" class="location-icon" viewBox="0 0 24 24">
          <path
            d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zM12 11.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"
          />
        </svg>
        <p class="item-address">{{ address }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue'
import { storage } from '@/firebase'
import { ref as storageRef, getDownloadURL } from 'firebase/storage'
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  itemId: {
    type: Number,
    required: true,
  },
  itemImage: {
    type: String,
    required: true,
  },
  itemName: {
    type: String,
    required: true,
  },
  itemPrice: {
    type: Number,
    required: true,
  },
  address: {
    type: String,
    required: true,
  },
})

const imageUrl = ref(null)

const formatPrice = (price) => {
  return new Intl.NumberFormat('ko-KR').format(price)
}

const handleImageError = () => {
  console.error('Image failed to load')
  imageUrl.value = null
}

const goToDetailPage = (itemId) => {
  router.push({ name: 'ItemDetail', params: { id: itemId } })
}

onMounted(async () => {
  try {
    // Firebase Storage의 이미지 경로로부터 URL 가져오기
    const imageReference = storageRef(storage, `/${props.itemImage}`)
    const url = await getDownloadURL(imageReference)
    imageUrl.value = url
  } catch (error) {
    // console.error("Error loading image:", error);
    const imageReference = storageRef(storage, '/items/3/Precourse_수료증.png')
    const url = await getDownloadURL(imageReference)
    imageUrl.value = url
  }
})
</script>

<style scoped>
/* 기존 스타일 유지 */
.item-detail {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
  height: 100%;
}

.item-detail:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.15);
}

.image-container {
  position: relative;
  padding-top: 75%; /* 4:3 Aspect Ratio */
  overflow: hidden;
}

.item-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.item-detail:hover .item-image {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 12px;
  right: 12px;
}

.favorite-btn {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.favorite-btn:hover {
  background: white;
}

.heart-icon {
  width: 20px;
  height: 20px;
  fill: none;
  stroke: #ff3b30;
  stroke-width: 2;
  transition: fill 0.2s ease;
}

.favorite-btn:hover .heart-icon {
  fill: #ff3b30;
}

.item-info {
  padding: 16px;
}

.item-name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  font-size: 16px;
  font-weight: 700;
  color: #ff3b30;
  margin-bottom: 12px;
}

.address-container {
  display: flex;
  align-items: center;
  gap: 4px;
}

.location-icon {
  width: 16px;
  height: 16px;
  fill: #666;
}

.item-address {
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 이미지 플레이스홀더 스타일 추가 */
.image-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  width: 48px;
  height: 48px;
  fill: #ccc;
}
</style>
