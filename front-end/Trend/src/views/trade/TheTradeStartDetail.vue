<template>
  <DefaultLayout>
    <div class="trade-container">
      <div v-if="isLoading" class="loading">로딩 중...</div>

      <div v-else>
        <div class="date-range">
          {{ formatDateRange(itemData?.tradeRentalStartDate, itemData?.tradeRentalEndDate) }}
        </div>

        <section class="content-section">
          <h2 class="section-title">상품 정보</h2>
          <div class="item-wrapper">
            <ItemInfoComponent v-if="itemInfo" :item="itemInfo" />
            <div class="status-badge">
              {{ itemData?.tradeState }}
            </div>
          </div>
        </section>

        <section class="content-section">
          <h2 class="section-title">대여 시점 사진</h2>
          <div class="image-grid">
            <div v-for="(image, index) in itemConditionImages" :key="index" class="image-item">
              <img :src="image" alt="대여 시점 사진" />
            </div>
          </div>
        </section>

        <PaymentInfo
          v-if="Object.keys(paymentInfo).length"
          :payment-info="paymentInfo"
          :trade-id="tradeId"
        />

        <button
          v-if="activeStartTrade"
          class="action-button disabled-review-button"
          disabled
          title="반납 완료 후 등록 가능합니다"
        >
          후기 등록
        </button>
        <button v-else-if="activeReturn" class="action-button" @click="handleReturn">
          반납 완료
        </button>
        <button v-else-if="activeRegistReview" class="action-button" @click="handleConfirmReturn">
          후기 등록
        </button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStorage, ref as storageRef, getDownloadURL } from 'firebase/storage'
import { tradeApi } from '@/api/tradeApi'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ItemInfoComponent from '@/components/trade/ItemInfoComponent.vue'
import PaymentInfo from '@/components/trade/PaymentInfo.vue'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const { userId } = storeToRefs(authStore)

const route = useRoute()
const router = useRouter()
const tradeId = route.params.id
const itemData = ref(null)
const itemConditionImages = ref([])
const isLoading = ref(true)
const lessorId = computed(() => itemData.value?.lessorId || null)
const lesseeId = computed(() => itemData.value?.lesseeId || null)

const activeStartTrade = computed(() => {
  return lesseeId.value === userId.value && itemData.value?.tradeState === '대여 중'
})

const activeReturn = computed(() => {
  return lessorId.value === userId.value && itemData.value?.tradeState === '대여 중'
})

const activeRegistReview = computed(() => {
  return lesseeId.value === userId.value && itemData.value?.tradeState === '반납 완료'
})

const itemInfo = computed(() => {
  if (!itemData.value) return null
  return {
    itemId: itemData.value.itemId,
    itemName: itemData.value.itemName,
    itemPrice: itemData.value.itemPrice,
    address: itemData.value.address,
    thumbnail: itemData.value.thumbnail,
  }
})

const paymentInfo = computed(() => {
  if (!itemData.value) return {}
  return {
    lessorNickname: itemData.value.lessorNickname,
    lesseeNickname: itemData.value.lesseeNickname,
    paymentAccountNumber: itemData.value.paymentAccountNumber,
    tradePrice: itemData.value.tradePrice,
    tradeDeposit: itemData.value.tradeDeposit,
    paymentStatus: itemData.value.paymentStatus,
  }
})

const formatDateRange = (start, end) => {
  if (!start || !end) return ''
  const formatDate = (date) => {
    const dateObj = new Date(date)
    return `${dateObj.getFullYear()}.${String(dateObj.getMonth() + 1).padStart(
      2,
      '0',
    )}.${String(dateObj.getDate()).padStart(2, '0')}`
  }
  return `${formatDate(start)} - ${formatDate(end)}`
}

const loadImages = async (imageUrls) => {
  const storage = getStorage()
  const loadedImages = await Promise.all(
    imageUrls.map(async (url) => {
      const imageRef = storageRef(storage, url)
      return await getDownloadURL(imageRef)
    }),
  )
  itemConditionImages.value = loadedImages
}

const handleReturn = async () => {
  await tradeApi.updateTradeStatusToReturn(tradeId)
  router.push({ name: 'ItemList', params: { id: userId } })
}

const handleConfirmReturn = async () => {
  router.push({ name: 'Review', params: { id: tradeId } })
}

onMounted(async () => {
  try {
    isLoading.value = true
    const response = await tradeApi.getTradeDetail(tradeId)
    itemData.value = response.data
    if (itemData.value.itemConditionImages?.length) {
      await loadImages(itemData.value.itemConditionImages)
    }
    console.log(itemData.value)
  } catch (error) {
    console.error('데이터 로딩 실패:', error)
  } finally {
    isLoading.value = false
  }
})
</script>

<style scoped>
.trade-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.date-range {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 32px 0;
  letter-spacing: -0.5px;
}

.content-section {
  background: white;
  border-radius: 12px;
  margin-bottom: 20px;
  padding: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.image-grid {
  gap: 0;
  display: flex;
  justify-content: flex-start;
}

.image-item {
  width: auto;
  margin: 0 4px;
}

.image-item img {
  width: 140px; /* width를 고정값으로 설정 */
  height: 140px;
  object-fit: cover;
  border: 1px solid #e5e7eb;
}

.action-button {
  width: 30%;
  padding: 16px;
  background-color: #000;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  margin: 20px auto 0;
  display: block;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  font-size: 16px;
  color: #666;
}

.status-badge {
  background-color: #6b6b6b;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  margin-left: 16px;
  flex-shrink: 0;
}

.item-wrapper {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.disabled-review-button {
  background-color: #e0e0e0 !important; /* 옅은 회색 */
  cursor: not-allowed !important;
  position: relative;
}

.disabled-review-button:hover::after {
  content: '반납 완료 후 등록 가능합니다';
  position: absolute;
  bottom: -40px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 14px;
  white-space: nowrap;
  z-index: 1000;
}
</style>
