// src/components/course/SpotSelector.vue
<template>
  <div class="spot-selector">
    <!-- 검색 영역 -->
    <div class="search-area">
      <div class="search-input-wrapper">
        <input
          v-model="searchKeyword"
          type="text"
          class="search-input"
          placeholder="관광지를 검색하세요"
        />
        <!-- 검색 버튼은 선택적으로 유지 -->
        <button class="search-button" @click="searchSpots">
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
          >
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
        </button>
      </div>

      <!-- 검색 결과 목록 -->
      <div v-if="searchResults.length > 0" class="search-results">
        <div
          v-for="spot in searchResults"
          :key="spot.spotId"
          class="search-result-item"
          @click="selectSpot(spot)"
        >
          <div class="spot-name">{{ spot.spotName }}</div>
          <div class="spot-address">{{ spot.address }}</div>
        </div>
      </div>
    </div>

    <!-- 지도 -->
    <div class="map-container">
      <GoogleMap
        :api-key="googleMapsApiKey"
        :spots="selectedSpots"
        :connect-markers="true"
        ref="mapComponent"
      />
    </div>

    <!-- 선택된 코스 목록 -->
    <div class="selected-spots-section">
      <h2 class="section-title">코스 목록</h2>
      <div class="selected-spots">
        <draggable v-model="selectedSpots" item-key="spotId" handle=".handle" @end="handleDragEnd">
          <template #item="{ element, index }">
            <div class="spot-item">
              <div class="handle">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <line x1="3" y1="12" x2="21" y2="12"></line>
                  <line x1="3" y1="6" x2="21" y2="6"></line>
                  <line x1="3" y1="18" x2="21" y2="18"></line>
                </svg>
              </div>
              <div class="spot-order">{{ index + 1 }}</div>
              <div class="spot-info">
                <div class="spot-name">{{ element.spotName }}</div>
                <div class="spot-address">{{ element.address }}</div>
              </div>
              <button class="remove-button" @click="removeSpot(index)">
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
                >
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
            </div>
          </template>
        </draggable>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import draggable from 'vuedraggable'
import GoogleMap from './GoogleMapComponent.vue'
import axiosInstance from '@/api/axiosInstance'

export default {
  name: 'SpotSelector',

  components: {
    draggable,
    GoogleMap,
  },

  props: {
    modelValue: {
      type: Array,
      default: () => [],
    },
  },

  emits: ['update:modelValue'],

  setup(props, { emit }) {
    const googleMapsApiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY
    const searchKeyword = ref('')
    const searchResults = ref([])
    const selectedSpots = ref(props.modelValue)
    const mapComponent = ref(null)

    // 실시간 검색을 위한 watch 추가
    watch(
      searchKeyword,
      async (newValue) => {
        if (newValue.trim()) {
          try {
            const response = await axiosInstance.get('/course/spot', {
              params: { keyWord: newValue },
            })
            searchResults.value = response.data
          } catch (error) {
            console.error('Failed to search spots:', error)
            searchResults.value = []
          }
        } else {
          searchResults.value = []
        }
      },
      { debounce: 300 },
    ) // 타이핑 후 300ms 뒤에 실행

    // 검색 결과 조회
    const searchSpots = async () => {
      if (!searchKeyword.value.trim()) return
      try {
        const response = await axiosInstance.get('/course/spot', {
          params: { keyWord: searchKeyword.value },
        })
        searchResults.value = response.data
      } catch (error) {
        console.error('Failed to search spots:', error)
        searchResults.value = []
      }
    }

    // 관광지 선택
    const selectSpot = (spot) => {
      selectedSpots.value = [
        ...selectedSpots.value,
        {
          ...spot,
          visitOrder: selectedSpots.value.length + 1,
        },
      ]
      searchResults.value = []
      searchKeyword.value = ''
    }

    // 관광지 제거
    const removeSpot = (index) => {
  const newSpots = [...selectedSpots.value];
  newSpots.splice(index, 1);

  // visitOrder 업데이트
  selectedSpots.value = newSpots.map((spot, idx) => ({
    ...spot,
    visitOrder: idx + 1,
  }));

  // 변경된 데이터 즉시 전달
  emit('update:modelValue', selectedSpots.value);
};

    // 드래그 앤 드롭 완료 처리
    const handleDragEnd = () => {
      selectedSpots.value = [...selectedSpots.value].map((spot, idx) => ({
        ...spot,
        visitOrder: idx + 1,
      }))
    }

    // selectedSpots 변경 감지
    watch(
      selectedSpots,
      (newValue) => {
        emit('update:modelValue', newValue)
      },
      { deep: true },
    )

    return {
      googleMapsApiKey,
      searchKeyword,
      searchResults,
      selectedSpots,
      mapComponent,
      searchSpots,
      selectSpot,
      removeSpot,
      handleDragEnd,
    }
  },
}
</script>

<style scoped>
.spot-selector {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.search-area {
  position: relative;
}

.search-input-wrapper {
  display: flex;
  gap: 8px;
}

.search-input {
  flex: 1;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
}

.search-button {
  padding: 8px 16px;
  background: #ff5a5a;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-top: 4px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 1000;
}

.search-result-item {
  padding: 12px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}

.search-result-item:hover {
  background: #f5f5f5;
}

.map-container {
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
}

.selected-spots-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.selected-spots {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.spot-item {
  display: flex;
  align-items: center;
  padding: 12px;
  gap: 12px;
  background: white;
  border-bottom: 1px solid #f5f5f5;
}

.handle {
  cursor: move;
  color: #666;
}

.spot-order {
  width: 24px;
  height: 24px;
  background: #ff5a5a;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.spot-info {
  flex: 1;
}

.spot-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.spot-address {
  font-size: 14px;
  color: #666;
}

.remove-button {
  padding: 8px;
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
}

.remove-button:hover {
  color: #ff5a5a;
}
</style>
