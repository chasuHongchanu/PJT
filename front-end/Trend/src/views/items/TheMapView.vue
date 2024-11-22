<template>
  <PageLayout>
    <div class="map-view">
      <div class="map-container">
        <GoogleMap v-if="isLoaded" class="google-map" :center="mapCenter" :zoom="14">
          <Marker
            v-for="item in searchResults"
            :key="item.id"
            :options="{
              position: { lat: item.latitude, lng: item.longitude },
              title: item.name,
            }"
          />
        </GoogleMap>

        <div class="floating-search">
          <ItemSearchBox @search="handleSearch" />
        </div>

        <!-- Fixed Bottom Buttons -->
        <div class="bottom-buttons">
          <button class="filter-btn" @click="openFilterModal">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              class="filter-icon"
              fill="none"
            >
              <path
                d="M3 6h18M6 12h12m-9 6h6"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
              />
            </svg>
            필터
          </button>
          <button class="list-btn" @click="goToListPage">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              class="list-icon"
              fill="none"
            >
              <path
                d="M4 6h16M4 12h16M4 18h16"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
              />
            </svg>
            목록보기
          </button>
        </div>
      </div>

      <!-- Filter Modal -->
      <Teleport to="body">
        <FilterModal v-if="showFilterModal" @close="closeFilterModal" @apply="applyFilter" />
      </Teleport>
    </div>
  </PageLayout>
</template>

<script setup>
import { useItemsStore } from '@/stores/items'
import { useRouter } from 'vue-router'
import { computed, ref, onMounted } from 'vue'
import { GoogleMap, Marker } from 'vue3-google-map'
import ItemSearchBox from '@/components/items/ItemSearchBox.vue'
import FilterModal from '@/components/items/FilterModal.vue'

const itemsStore = useItemsStore()
const router = useRouter()
const isLoaded = ref(false)
const showFilterModal = ref(false)

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY

const mapCenter = computed(
  () =>
    itemsStore.mapCenter || {
      lat: 37.5665,
      lng: 126.978,
    },
)

const searchResults = computed(() => itemsStore.searchResults)

onMounted(() => {
  if (window.google && window.google.maps) {
    isLoaded.value = true
  } else {
    console.error('Google Maps API is not loaded')
  }
})

const handleSearch = async (keyword) => {
  await itemsStore.searchItems({ keyword: keyword })
}

const goToListPage = () => {
  itemsStore.applySearchResults()
  router.push('/items/view')
}

const openFilterModal = () => {
  showFilterModal.value = true
  document.body.style.overflow = 'hidden'
}

const closeFilterModal = () => {
  showFilterModal.value = false
  document.body.style.overflow = ''
}

const applyFilter = (filters) => {
  console.log('Applied filters:', filters)
  closeFilterModal()
  // 필터 적용 로직 구현
}
</script>

<style scoped>
.map-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
  position: relative;
}

.map-container {
  flex: 1;
  width: 100%;
  position: relative;
}

.google-map {
  width: 100%;
  height: 100%;
}

.floating-search {
  position: absolute;
  top: 40px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  width: 90%;
  max-width: 600px;
}

.bottom-buttons {
  position: fixed;
  bottom: 24px;
  left: 24px;
  right: 24px;
  display: flex;
  justify-content: space-between;
  z-index: 1000;
}

.filter-btn,
.list-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.filter-btn {
  background-color: white;
  color: #333;
}

.list-btn {
  background-color: #333;
  color: white;
}

.filter-btn:hover,
.list-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.filter-icon,
.list-icon {
  width: 18px;
  height: 18px;
}

.list-icon {
  stroke: white;
}

.filter-icon {
  stroke: #333;
}

@media screen and (max-width: 768px) {
  .floating-search {
    width: 85%;
    top: 30px;
  }

  .bottom-buttons {
    bottom: 16px;
    left: 16px;
    right: 16px;
  }

  .filter-btn,
  .list-btn {
    padding: 10px 20px;
    font-size: 13px;
  }
}

@media screen and (max-width: 480px) {
  .floating-search {
    width: 90%;
    top: 20px;
  }

  .filter-btn,
  .list-btn {
    padding: 8px 16px;
    font-size: 12px;
  }

  .filter-icon,
  .list-icon {
    width: 16px;
    height: 16px;
  }
}
</style>
