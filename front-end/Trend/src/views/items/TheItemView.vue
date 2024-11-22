<template>
  <PageLayout>
    <div class="items-view">
      <div class="main-content">
        <!-- Search Box -->
        <div class="search-wrapper">
          <ItemSearchBox
            v-model="searchQuery"
            @search="filterItems"
            placeholder="지역, 매물을 검색해보세요"
          />
        </div>

        <!-- Filter Actions -->
        <div class="filter-actions">
          <div class="filter-buttons">
            <button class="filter-btn">
              <svg xmlns="http://www.w3.org/2000/svg" class="filter-icon" viewBox="0 0 24 24">
                <path
                  d="M3 6h18M6 12h12m-9 6h6"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                />
              </svg>
              필터
            </button>
          </div>
          <button @click="registerItem" class="write-btn">글쓰기</button>
        </div>

        <!-- Loading State -->
        <div v-if="isLoading" class="loading-state">
          <div class="loading-spinner"></div>
          <p>데이터를 불러오는 중입니다...</p>
        </div>

        <!-- Items Grid -->
        <div v-else-if="filteredItems.length" class="grid-container">
          <ItemDetail
            v-for="item in filteredItems"
            :key="item.itemId"
            :itemImage="item.itemImage"
            :itemName="item.itemName"
            :itemPrice="item.itemPrice"
            :address="item.address"
          />
        </div>
        <div v-else class="empty-state">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="empty-icon"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            />
          </svg>
          <p class="empty-text">검색 결과가 없습니다.</p>
        </div>
      </div>

      <!-- Fixed Map Button -->
      <button class="map-button">
        <svg xmlns="http://www.w3.org/2000/svg" class="map-icon" viewBox="0 0 24 24">
          <path
            d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 13l5.447-2.724A1 1 0 0021 16.382V5.618a1 1 0 00-1.447-.894L15 7m0 13V7m0 0L9 4"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
        지도보기
      </button>
    </div>
  </PageLayout>
</template>

<script setup>
import { useItemsStore } from '@/stores/items'
import { computed, onMounted, ref } from 'vue'
import ItemDetail from '@/components/items/ItemDetail.vue'
import ItemSearchBox from '@/components/items/ItemSearchBox.vue'

const itemsStore = useItemsStore()
const searchQuery = ref('')
const filteredItems = computed(() => itemsStore.filteredItems)
const isLoading = ref(true)

onMounted(async () => {
  try {
    await itemsStore.fetchItems()
  } finally {
    isLoading.value = false
  }
})

const filterItems = () => {
  itemsStore.filteredItems = itemsStore.items.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.value.toLowerCase()),
  )
}

const registerItem = () => {
  alert('Redirect to item registration page.')
}
</script>

<style scoped>
.items-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
  position: relative;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

.filter-actions {
  margin: 16px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.filter-buttons {
  display: flex;
  gap: 8px;
}

.filter-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: white;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-btn:hover {
  background: #f5f5f5;
}

.filter-icon {
  width: 16px;
  height: 16px;
}

.write-btn {
  padding: 8px 24px;
  background: #ff3b30;
  color: white;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.write-btn:hover {
  background: #ff2d20;
  transform: translateY(-1px);
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  padding: 20px;
}

.map-button {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: #333;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease;
  z-index: 1000;
}

.map-button:hover {
  background: #222;
  transform: translateX(-50%) translateY(-2px);
}

.map-icon {
  width: 20px;
  height: 20px;
  stroke: currentColor;
}

/* Responsive Design */
@media screen and (max-width: 768px) {
  .grid-container {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }

  .filter-actions {
    padding: 0 16px;
  }

  .search-wrapper {
    padding: 0 16px;
  }
}

@media screen and (max-width: 480px) {
  .grid-container {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    padding: 0 12px;
  }

  .write-btn {
    padding: 6px 16px;
    font-size: 13px;
  }

  .filter-btn {
    padding: 6px 12px;
    font-size: 13px;
  }

  .search-wrapper {
    padding: 0 12px;
  }

  .loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 48px 0;
    color: #666;
  }

  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 3px solid #f3f3f3;
    border-top: 3px solid #ff3b30;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 16px;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
}
</style>
