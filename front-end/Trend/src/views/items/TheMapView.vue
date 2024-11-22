<template>
  <PageLayout>
    <div class="map-view">
      <ItemSearchBox @search="handleSearch" />

      <GoogleMap class="map-container" :api-key="apiKey" :center="mapCenter" :zoom="14">
        <Marker
          v-for="item in searchResults"
          :key="item.id"
          :options="{
            position: { lat: item.latitude, lng: item.longitude },
            title: item.name,
          }"
        />
      </GoogleMap>

      <footer class="footer">
        <button class="list-btn" @click="goToListPage">목록보기</button>
      </footer>
    </div>
  </PageLayout>
</template>

<script setup>
import { useItemsStore } from '@/stores/items'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const itemsStore = useItemsStore()
const router = useRouter()

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY

const mapCenter = computed(() => itemsStore.mapCenter)
const searchResults = computed(() => itemsStore.searchResults)

// 검색 처리
const handleSearch = async (keyword) => {
  await itemsStore.searchItems({ keyword: keyword })
}

// 목록 보기로 이동
const goToListPage = () => {
  itemsStore.applySearchResults() // 검색 결과를 목록에 반영
  router.push('/items/view')
}
</script>

<style scoped>
.map-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.search-container {
  display: flex;
  align-items: center;
  padding: 10px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-bar {
  flex: 1;
  border: none;
  outline: none;
  padding: 8px 12px;
  font-size: 16px;
  border-radius: 20px 0 0 20px;
}

.search-btn {
  background: #ff3b30;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
}

.map-container {
  flex: 1;
  width: 100%;
}

.footer {
  display: flex;
  justify-content: center;
  padding: 10px;
  background: white;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}

.list-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  background-color: #ff3b30;
  color: white;
  font-size: 14px;
  cursor: pointer;
}

.gm-fullscreen-control {
  display: none !important;
}
</style>
