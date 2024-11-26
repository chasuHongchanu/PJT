<template>
  <section class="section-container">
    <div class="section-header">
      <h2>판매 물품</h2>
    </div>
    <div class="items-container">
      <div class="items-grid">
        <ItemCard v-for="item in items" :key="item.itemId" :item="item" />
        <!-- 무한 스크롤 트리거 요소 -->
        <div v-if="!isLoading && hasMore" ref="infiniteScrollTrigger"></div>
      </div>
      <!-- 로딩 상태 표시 -->
      <div v-if="isLoading" class="loading-state">로딩 중...</div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import ItemCard from '@/components/items/ItemCard.vue'

const props = defineProps({
  lessorId: {
    type: String,
    required: true,
  },
})

const items = ref([])
const currentPage = ref(1)
const isLoading = ref(false)
const hasMore = ref(true)
const infiniteScrollTrigger = ref(null)

const loadMoreItems = async () => {
  console.log('Loading items for page:', currentPage.value)
  console.log('Current items count:', items.value.length)
  console.log('Has more:', hasMore.value)

  if (isLoading.value) return

  try {
    isLoading.value = true
    const response = await fetch(
      `http://localhost:8080/api/item/rent/lessor/${props.lessorId}/items?page=${currentPage.value}&size=5`,
    )
    const data = await response.json()

    if (data.data && Array.isArray(data.data)) {
      items.value = [...items.value, ...data.data]

      // 전체 데이터 개수와 비교하여 더 정확하게 hasMore 판단
      hasMore.value = items.value.length < data.totalCount // 백엔드에서 totalCount를 제공한다고 가정
      // 또는
      hasMore.value = data.data.length > 0 // 데이터가 있는지 여부로만 판단

      if (hasMore.value) {
        currentPage.value++
      }
    }
    console.log('Response data:', data)
  } catch (error) {
    console.error('Error loading more items:', error)
  } finally {
    isLoading.value = false
  }
}
// Intersection Observer 설정
let observer = null

const setupInfiniteScroll = () => {
  const options = {
    root: document.querySelector('.items-container'),
    rootMargin: '50px', // 여유 공간을 더 늘립니다
    threshold: 0.1, // 임계값을 낮춰서 더 일찍 감지하도록 합니다
  }

  observer = new IntersectionObserver(([entry]) => {
    if (entry.isIntersecting && hasMore.value && !isLoading.value) {
      loadMoreItems()
    }
  }, options)

  if (infiniteScrollTrigger.value) {
    observer.observe(infiniteScrollTrigger.value)
  }
}

onMounted(async () => {
  await loadMoreItems()
  setupInfiniteScroll()
})

onUnmounted(() => {
  if (observer && infiniteScrollTrigger.value) {
    observer.unobserve(infiniteScrollTrigger.value)
    observer = null
  }
})

watch(
  () => items.value.length,
  (newLength) => {
    console.log('Items length changed:', newLength)
    if (newLength > 0 && hasMore.value) {
      // Intersection Observer 재설정
      setupInfiniteScroll()
    }
  },
)
</script>

<style scoped>
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

.items-container {
  height: 500px; /* 컨테이너 높이를 더 크게 설정 */
  min-height: 300px; /* 최소 높이 설정 */
  overflow-y: auto;
}

/* 스크롤바 스타일링 */
.items-container::-webkit-scrollbar {
  width: 6px;
}

.items-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.items-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.items-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.items-grid {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.loading-state {
  text-align: center;
  padding: 20px;
  color: #666;
}
</style>
