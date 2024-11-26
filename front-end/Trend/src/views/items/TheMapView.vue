<template>
  <MapLayout>
    <div class="map-view">
      <div class="map-container">
        <GoogleMap
          class="google-map"
          :api-key="apiKey"
          :center="mapCenter"
          :zoom="14"
          @ready="onMapReady"
          ref="mapRef"
        >
          <Marker
            v-for="marker in markers"
            :key="marker.id"
            :options="{
              position: marker.position,
              title: marker.title,
            }"
            @click="handleMarkerClick(marker)"
          />
        </GoogleMap>

        <!-- 마커 클릭 시 나타나는 플로팅 카드 -->
        <div v-if="selectedMarker" class="floating-card">
          <div class="card">
            <div class="card-image" v-if="selectedMarker.itemImage">
              <img :src="selectedMarker.itemImage" :alt="selectedMarker.itemName" />
            </div>
            <div class="card-content">
              <div class="card-header">
                <h3 class="card-title">{{ selectedMarker.itemName }}</h3>
                <button class="close-button" @click="closeInfoWindow">
                  <span>&times;</span>
                </button>
              </div>
              <div class="card-body">
                <p class="address">{{ selectedMarker.itemAddress }}</p>
                <p class="price">{{ formatPrice(selectedMarker.itemPrice) }}원</p>
              </div>
            </div>
          </div>
        </div>

        <div class="floating-search">
          <ItemSearchBox @search="handleSearch" />
        </div>

        <div class="bottom-buttons">
          <button class="filter-btn" @click="openFilterModal">필터</button>
          <button class="list-btn" @click="goToListPage">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="list-icon"
            >
              <line x1="8" y1="6" x2="21" y2="6"></line>
              <line x1="8" y1="12" x2="21" y2="12"></line>
              <line x1="8" y1="18" x2="21" y2="18"></line>
              <line x1="3" y1="6" x2="3.01" y2="6"></line>
              <line x1="3" y1="12" x2="3.01" y2="12"></line>
              <line x1="3" y1="18" x2="3.01" y2="18"></line>
            </svg>
            <span>목록보기</span>
          </button>
        </div>
      </div>

      <Teleport to="body">
        <FilterModal v-if="showFilterModal" @close="closeFilterModal" @apply="applyFilter" />
      </Teleport>
    </div>
  </MapLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useItemsStore } from '@/stores/items'
import { useRouter } from 'vue-router'
import { GoogleMap, Marker } from 'vue3-google-map'
import ItemSearchBox from '@/components/items/ItemSearchBox.vue'
import FilterModal from '@/components/items/FilterModal.vue'
import MapLayout from '@/layouts/MapLayout.vue'

const itemsStore = useItemsStore()
const router = useRouter()
const showFilterModal = ref(false)
const selectedMarker = ref(null)
const mapRef = ref(null)

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY

const mapCenter = computed(() => itemsStore.mapCenter || { lat: 37.5665, lng: 126.978 })

const markers = computed(() =>
  itemsStore.filteredItems.map((item) => ({
    id: item.itemId,
    position: {
      lat: parseFloat(item.latitude),
      lng: parseFloat(item.longitude),
    },
    title: item.itemName,
    itemName: item.itemName,
    itemImage: item.itemImage,
    itemAddress: item.address,
    itemPrice: item.itemPrice,
  })),
)

const formatPrice = (price) => {
  return price?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const handleMarkerClick = (marker) => {
  selectedMarker.value = marker

  if (mapRef.value) {
    const map = mapRef.value.map
    map.panTo(marker.position)
  }
}

const closeInfoWindow = () => {
  selectedMarker.value = null
}

const handleSearch = async (keyword) => {
  await itemsStore.searchItems(keyword)
}

const goToListPage = () => {
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

const applyFilter = async (filters) => {
  try {
    await itemsStore.filterItems(filters)
    if (itemsStore.filteredItems.length > 0) {
      const firstItem = itemsStore.filteredItems[0]
      mapCenter.value = {
        lat: parseFloat(firstItem.latitude),
        lng: parseFloat(firstItem.longitude),
      }
    }
  } catch (error) {
    console.error('Error applying filters:', error)
  } finally {
    closeFilterModal()
  }
}

const onMapReady = (mapInstance) => {
  const style = document.createElement('style')
  style.innerHTML = `
    .gm-fullscreen-control {
      display: none !important;
    }
    .gm-control-active {
      display: none !important;
    }
  `
  document.head.appendChild(style)

  mapInstance.setOptions({
    disableDefaultUI: true,
    mapTypeControl: false,
    fullscreenControl: false,
    mapTypeControlOptions: {
      // 이 옵션 추가
      mapTypeIds: [],
    },
  })
}

onMounted(async () => {
  try {
    await itemsStore.initializeItems()
    await itemsStore.restoreLastState()
  } catch (error) {
    console.error('Error loading markers:', error)
  }
})
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

.floating-card {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  width: 90%;
  max-width: 320px;
}

.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-content {
  padding: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  color: #666;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.card-body {
  color: #666;
}

.address {
  margin-bottom: 8px;
  font-size: 14px;
}

.price {
  font-size: 16px;
  font-weight: 600;
  color: #ff3b30;
}

.floating-search {
  position: fixed;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  width: 90%;
  max-width: 600px;
}

.bottom-buttons {
  position: fixed;
  bottom: 24px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  z-index: 1000;
  padding: 0 24px;
}

.filter-btn,
.list-btn {
  display: flex;
  align-items: center;
  justify-content: center; /* 중앙 정렬 추가 */
  gap: 8px;
  padding: 16px 32px; /* 패딩 증가 */
  border: none;
  border-radius: 30px; /* 더 둥글게 */
  font-size: 16px; /* 폰트 크기 증가 */
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 140px; /* 최소 너비 설정 */
}

.filter-btn {
  background-color: white;
  color: #333;
  position: fixed;
  left: 24px;
  bottom: 24px;
}

.list-btn {
  background-color: white;
  color: #ff3b30;
  display: flex;
  align-items: center;
  gap: 8px;
}

.list-btn i {
  font-size: 20px; /* 아이콘 크기 조정 */
}

@media screen and (max-width: 768px) {
  .floating-search {
    width: 85%;
    top: 70px;
  }

  .bottom-buttons {
    bottom: 16px;
    padding: 0 16px;
  }

  .filter-btn {
    left: 16px;
    bottom: 16px;
  }

  .filter-btn,
  .list-btn {
    padding: 14px 28px; /* 모바일에서도 충분히 큰 크기 유지 */
    font-size: 15px;
    min-width: 120px;
  }
}

@media screen and (max-width: 480px) {
  .floating-card {
    width: 180px;
  }

  .floating-search {
    width: 90%;
    top: 60px;
  }

  .filter-btn,
  .list-btn {
    padding: 12px 24px;
    font-size: 14px;
    min-width: 100px;
  }
}

.floating-card {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  width: 90%;
  max-width: 200px; /* 적절한 크기로 조정 */
}

.card {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-image {
  width: 100%;
  height: 120px; /* 이미지 높이 조정 */
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-content {
  padding: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.card-title {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.close-button {
  background: none;
  border: none;
  font-size: 18px;
  color: #666;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.card-body {
  color: #666;
}

.address {
  margin-bottom: 4px;
  font-size: 12px;
}

.price {
  font-size: 13px;
  font-weight: 600;
  color: #ff3b30;
}

.gm-style-mtc {
  /* 이 스타일 추가 */
  display: none !important;
}

/* style 부분 수정 */
.bottom-buttons {
  position: fixed;
  bottom: 24px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  z-index: 1000;
  padding: 0 24px;
}

.filter-btn,
.list-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px 32px;
  border: none;
  border-radius: 30px;
  font-size: 18px; /* 글씨 크기 증가 */
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 140px;
}

.filter-btn {
  background-color: white;
  color: #333;
  position: fixed;
  left: 24px;
  bottom: 24px;
}

.list-btn {
  background-color: white;
  color: #ff3b30;
  position: relative; /* 중앙 정렬을 위해 추가 */
  display: flex;
  align-items: center;
  justify-content: center; /* 중앙 정렬 */
  margin: 0 auto; /* 중앙 정렬 */
}

.list-icon {
  width: 24px;
  height: 24px;
  stroke: #ff3b30;
}

@media screen and (max-width: 768px) {
  .filter-btn,
  .list-btn {
    padding: 14px 28px;
    font-size: 17px; /* 모바일에서도 글씨 크게 유지 */
    min-width: 120px;
  }
}

@media screen and (max-width: 480px) {
  .filter-btn,
  .list-btn {
    padding: 12px 24px;
    font-size: 16px; /* 작은 화면에서도 글씨 크게 유지 */
    min-width: 100px;
  }
}

.list-btn {
  background-color: white;
  color: #ff3b30;
  position: relative;
  display: flex;
  align-items: center; /* 수직 중앙 정렬 */
  justify-content: center;
  gap: 6px; /* 간격 조정 */
  margin: 0 auto;
  line-height: 1; /* 줄 높이 조정 */
}

.list-text {
  display: flex;
  align-items: center;
  height: 20px; /* SVG 높이와 맞춤 */
}

.list-icon {
  width: 20px;
  height: 20px;
  stroke: #ff3b30;
  display: flex;
  align-items: center;
}
</style>
