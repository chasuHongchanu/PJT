<template>
  <div class="items-container">
    <h2 class="section-title">{{ title }}</h2>
    <div class="items-list">
      <div
        v-for="item in items"
        :key="item.itemId"
        class="item-card"
        @click="handleItemClick(item)"
        style="cursor: pointer"
      >
        <div class="item-image">
          <img :src="item.displayThumbnail" :alt="item.itemName" class="thumbnail" />
        </div>
        <div class="item-details">
          <h3 class="item-name">{{ item.itemName }}</h3>
          <p class="item-price">₩ {{ formatPrice(item.itemPrice) }}</p>
          <p class="item-address">{{ item.address }}</p>
          <div class="rental-period">
            <span>{{ formatDate(item.availableRentalStartDate) }}</span>
            <span class="date-separator">~</span>
            <span>{{ formatDate(item.availableRentalEndDate) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStorage, ref as storageRef, getDownloadURL } from 'firebase/storage'
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  fetchItems: {
    type: Function,
    required: true,
  },
  tabId: {
    // 새로 추가된 prop
    type: String,
    required: true,
  },
})

const items = ref([])

// Firebase 스토리지에서 이미지 URL을 가져오는 함수
const loadImageUrl = async (imagePath) => {
  if (!imagePath) return null
  try {
    const storage = getStorage()
    const imageRef = storageRef(storage, imagePath)
    return await getDownloadURL(imageRef)
  } catch (error) {
    console.error('Error loading image:', error)
    return null
  }
}

// 모든 아이템의 이미지 URL을 로드하는 함수
const loadItemsWithImages = async (itemsData) => {
  const itemsWithImages = await Promise.all(
    itemsData.map(async (item) => {
      const displayThumbnail = await loadImageUrl(item.thumbnail)
      return {
        ...item,
        displayThumbnail,
      }
    }),
  )
  return itemsWithImages
}

const formatPrice = (price) => {
  return parseInt(price).toLocaleString()
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
}

const handleItemClick = (item) => {
  if (item.paymentStatus === '입금 전') {
    router.push({
      name: 'Pay',
      params: { id: item.tradeId },
    })
  } else if (props.tabId === 'registered') {
    router.push({
      name: 'ItemDetail',
      params: { id: item.itemId },
    })
  } else if (props.tabId === 'renting' || props.tabId === 'rented') {
    // 임차 중인 물품이고 대여 전 상태일 때
    if (props.tabId === 'rented' && item.tradeState === '대여 전') {
      router.push({
        name: 'Start',
        params: { id: item.tradeId },
      })
    } else {
      router.push({
        name: 'StartDetail',
        params: { id: item.tradeId },
      })
    }
  }
}

onMounted(async () => {
  try {
    const response = await props.fetchItems(1)
    const itemsWithImages = await loadItemsWithImages(response.data)
    items.value = itemsWithImages
    console.log(items.value)
  } catch (error) {
    console.error('Failed to fetch items:', error)
  }
})
</script>

<style scoped>
.items-container {
  display: flex;
  flex-direction: column;
  height: 100%; /* 컨테이너 높이를 100%로 설정 */
  min-height: calc(100vh - 180px); /* 최소 높이 설정 (네비게이션 바, 탭, 패딩 등 고려) */
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: #333;
}

.items-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  flex: 1; /* 남은 공간을 모두 차지하도록 설정 */
}

.item-card {
  display: flex;
  padding: 1rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
}

.item-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 120px;
  height: 120px;
  flex-shrink: 0;
  margin-right: 1rem;
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.item-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.item-price {
  font-size: 1.2rem;
  font-weight: 700;
  color: #ff385c;
  margin-bottom: 0.5rem;
}

.item-address {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 0.5rem;
}

.rental-period {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 0.5rem;
}

.date-separator {
  margin: 0 0.5rem;
  color: #999;
}

.status-badge {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.8rem;
  font-weight: 500;
  align-self: flex-start;
}

.status-available {
  background-color: #22c55e;
  color: white;
}

.status-rented {
  background-color: #ff385c;
  color: white;
}

.status-reserved {
  background-color: #f59e0b;
  color: white;
}

.status-default {
  background-color: #6b7280;
  color: white;
}
</style>
