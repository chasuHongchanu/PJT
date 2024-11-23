<template>
  <DefaultLayout>
    <div class="map-view">
      <!-- Google 지도 -->
      <div class="map-container">
        <GoogleMap
          class="google-map"
          :api-key="apiKey"
          :center="mapCenter"
          :zoom="14"
          @ready="onMapReady"
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
          <!-- 커스텀 정보창 -->
          <div
            v-if="selectedMarker"
            class="custom-info-window"
            :style="{
              position: 'absolute',
              top: selectedMarker.position.top + 'px',
              left: selectedMarker.position.left + 'px',
            }"
          >
            <ItemInfoWindow :item="selectedMarker" />
            <button class="close-btn" @click="closeInfoWindow">닫기</button>
          </div>
        </GoogleMap>

        <!-- 검색 및 필터 UI -->
        <div class="floating-search">
          <ItemSearchBox @search="handleSearch" />
        </div>

        <div class="bottom-buttons">
          <button class="filter-btn" @click="openFilterModal">필터</button>
          <button class="list-btn" @click="goToListPage">목록보기</button>
        </div>
      </div>

      <!-- 필터 모달 -->
      <Teleport to="body">
        <FilterModal
          v-if="showFilterModal"
          @close="closeFilterModal"
          @apply="applyFilter"
        />
      </Teleport>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useItemsStore } from "@/stores/items";
import { useRouter } from "vue-router";
import { GoogleMap, Marker } from "vue3-google-map";
import ItemSearchBox from "@/components/items/ItemSearchBox.vue";
import FilterModal from "@/components/items/FilterModal.vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ItemInfoWindow from "@/components/items/ItemInfoWindow.vue";

const itemsStore = useItemsStore();
const router = useRouter();
const showFilterModal = ref(false);
const selectedMarker = ref(null);

// Google Maps API 키
const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;

// 지도 중심
const mapCenter = computed(() => itemsStore.mapCenter || { lat: 37.5665, lng: 126.978 });

// 마커 데이터 (Pinia의 filteredItems 기반)
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
  }))
);

// 마커 클릭 핸들러
const handleMarkerClick = (marker) => {
  selectedMarker.value = marker;
};

// 정보창 닫기
const closeInfoWindow = () => {
  selectedMarker.value = null;
};

// 검색 실행
const handleSearch = async (keyword) => {
  await itemsStore.searchItems(keyword);
};

// 목록 페이지로 이동
const goToListPage = () => {
  router.push("/items/view");
};

// 필터 모달 열기
const openFilterModal = () => {
  showFilterModal.value = true;
  document.body.style.overflow = "hidden";
};

// 필터 모달 닫기
const closeFilterModal = () => {
  showFilterModal.value = false;
  document.body.style.overflow = "";
};

// 필터 적용
const applyFilter = async (filters) => {
  try {
    await itemsStore.filterItems(filters); // Pinia의 filterItems 호출
    if (itemsStore.filteredItems.length > 0) {
      const firstItem = itemsStore.filteredItems[0];
      mapCenter.value = {
        lat: parseFloat(firstItem.latitude),
        lng: parseFloat(firstItem.longitude),
      };
    }
  } catch (error) {
    console.error("Error applying filters:", error);
  } finally {
    closeFilterModal();
  }
};

// 지도 준비 완료
const onMapReady = (mapInstance) => {
  const style = document.createElement("style");
  style.innerHTML = `
    .gm-fullscreen-control {
      display: none !important;
    }
    .gm-control-active {
      display: none !important;
    }
  `;
  document.head.appendChild(style);

  mapInstance.setOptions({
    disableDefaultUI: true,
    mapTypeControl: false,
    fullscreenControl: false,
  });
};

onMounted(async () => {
  try {
    await itemsStore.initializeItems(); // 최초 데이터 로드
    await itemsStore.restoreLastState(); // 마지막 검색/필터 상태 복원
  } catch (error) {
    console.error("Error loading markers:", error);
  }
});
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
  position: fixed;
  left: 24px;
  bottom: 24px;
}

.list-btn {
  background-color: white;
  color: #ff3b30;
}

.filter-btn:hover,
.list-btn:hover {
  transform: translateY(-2px);
}

.filter-btn:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.list-btn:hover {
  box-shadow: 0 6px 16px rgba(255, 59, 48, 0.15);
  background-color: #fff5f5;
}

.filter-icon,
.list-icon {
  width: 18px;
  height: 18px;
}

.list-icon {
  stroke: #ff3b30;
}

.filter-icon {
  stroke: #333;
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
    padding: 10px 20px;
    font-size: 13px;
  }
}

@media screen and (max-width: 480px) {
  .floating-search {
    width: 90%;
    top: 60px;
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

:deep(.gm-style .gm-style-iw-c) {
  padding: 0 !important;
  border-radius: 12px !important;
}

:deep(.gm-style .gm-style-iw-d) {
  overflow: hidden !important;
  padding: 0 !important;
}

:deep(.gm-style .gm-style-iw-t::after) {
  background: linear-gradient(
    45deg,
    rgba(255, 255, 255, 1) 50%,
    rgba(255, 255, 255, 0) 51%,
    rgba(255, 255, 255, 0) 100%
  ) !important;
  box-shadow: none !important;
}

:deep(.gm-ui-hover-effect) {
  top: 0 !important;
  right: 0 !important;
  background: rgba(255, 255, 255, 0.8) !important;
  border-radius: 50% !important;
  margin: 4px !important;
}

.custom-info-window {
  position: absolute;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  padding: 12px;
  z-index: 1000;
}
</style>
