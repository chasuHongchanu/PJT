<template>
  <PageLayout>
    <div class="map-view">
      <!-- Search bar -->
      <ItemSearchBox @search="handleSearch" />

      <!-- Google Map -->
      <GoogleMap class="map-container" :api-key="apiKey" :center="center" :zoom="14">
        <Marker
          v-for="item in items"
          :key="item.id"
          :options="{
            position: { lat: item.latitude, lng: item.longitude },
            title: item.name,
          }"
        />
      </GoogleMap>

      <!-- Footer -->
      <footer class="footer">
        <button class="list-btn" @click="goToListPage">목록보기</button>
      </footer>
    </div>
  </PageLayout>
</template>

<script setup>
import { ref } from "vue";
import { GoogleMap, Marker } from "vue3-google-map";
import ItemSearchBox from "@/components/items/ItemSearchBox.vue";
import PageLayout from "@/components/layout/PageLayout.vue";
import { useRouter } from "vue-router";

const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;

// 지도 중심점 및 마커 데이터
const center = ref({ lat: 37.5665, lng: 126.978 });
const items = ref([]);

// Vue Router 사용
const router = useRouter();

// 검색 처리 함수
const handleSearch = async (searchText) => {
  try {
    // 백엔드 API 호출
    const response = await fetch(
      `https://api.example.com/search?query=${encodeURIComponent(searchText)}`
    );
    const data = await response.json();

    if (data && data.results) {
      // 첫 번째 검색 결과를 중심점으로 설정
      const firstResult = data.results[0];
      center.value = {
        lat: firstResult.latitude,
        lng: firstResult.longitude,
      };

      // 검색 결과를 items로 설정 (마커 추가)
      items.value = data.results.map((result, index) => ({
        id: index,
        name: result.name,
        latitude: result.latitude,
        longitude: result.longitude,
      }));
    }
  } catch (error) {
    console.error("Error fetching search results:", error);
  }
};

// 목록 페이지 이동 함수
const goToListPage = () => {
  router.push("/items/view");
};
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
</style>
