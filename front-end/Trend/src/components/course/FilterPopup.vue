<template>
    <div class="filter-overlay" v-if="isVisible" @click="closeFilter">
      <div class="filter-container" @click.stop>
        <!-- 헤더 -->
        <div class="filter-header">
          <button class="close-button" @click="closeFilter">
            <span>×</span>
          </button>
          <span class="header-title">필터</span>
        </div>
  
        <!-- 필터 컨텐츠 -->
        <div class="filter-content">
          <!-- 지역 필터 -->
          <div class="filter-section">
            <h3 class="filter-title">지역</h3>
            <div class="select-group">
              <select v-model="selectedCountry" class="filter-select">
                <option value="">국가</option>
                <option v-for="country in countries" :key="country" :value="country">
                  {{ country }}
                </option>
              </select>
              <select v-model="selectedRegion" class="filter-select">
                <option value="">지역</option>
                <option v-for="region in regions" :key="region" :value="region">
                  {{ region }}
                </option>
              </select>
              <select v-model="selectedCity" class="filter-select">
                <option value="">도시</option>
                <option v-for="city in cities" :key="city" :value="city">
                  {{ city }}
                </option>
              </select>
            </div>
          </div>
  
          <!-- 정렬 기준 필터 -->
          <div class="filter-section">
            <h3 class="filter-title">정렬 기준</h3>
            <div class="sort-buttons">
              <button
                v-for="sort in sortOptions"
                :key="sort.value"
                :class="['sort-button', { active: selectedSort === sort.value }]"
                @click="selectedSort = sort.value"
              >
                {{ sort.label }}
              </button>
            </div>
          </div>
  
          <!-- 날짜 필터 -->
          <div class="filter-section">
            <h3 class="filter-title">날짜</h3>
            <div class="date-inputs">
              <input 
                type="date" 
                v-model="startDate"
                class="date-input"
              >
              <span class="date-separator">-</span>
              <input 
                type="date" 
                v-model="endDate"
                class="date-input"
              >
            </div>
          </div>
        </div>
  
        <!-- 적용 버튼 -->
        <button class="apply-button" @click="applyFilter">적용</button>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue'
  
  export default {
    name: 'FilterPopup',
    
    props: {
      isVisible: {
        type: Boolean,
        required: true
      }
    },
  
    emits: ['close', 'apply'],
  
    setup(props, { emit }) {
      // 더미 데이터
      const countries = ['한국', '일본', '미국']
      const regions = ['서울', '도쿄', '뉴욕']
      const cities = ['강남구', '신주쿠', '맨해튼']
      const sortOptions = [
        { label: '최신순', value: 'latest' },
        { label: '추천순', value: 'recommended' },
        { label: '조회수', value: 'views' }
      ]
  
      // 선택된 값들
      const selectedCountry = ref('')
      const selectedRegion = ref('')
      const selectedCity = ref('')
      const selectedSort = ref('latest') // 기본값: 최신순
      const startDate = ref('')
      const endDate = ref('')
  
      const closeFilter = () => {
        emit('close')
      }
  
      const applyFilter = () => {
        emit('apply', {
          country: selectedCountry.value,
          region: selectedRegion.value,
          city: selectedCity.value,
          sort: selectedSort.value,
          dateRange: {
            start: startDate.value,
            end: endDate.value
          }
        })
        closeFilter()
      }
  
      return {
        // 데이터
        countries,
        regions,
        cities,
        sortOptions,
        // 선택된 값들
        selectedCountry,
        selectedRegion,
        selectedCity,
        selectedSort,
        startDate,
        endDate,
        // 메서드
        closeFilter,
        applyFilter
      }
    }
  }
  </script>
  
  <style scoped>
  .filter-overlay {
    position: fixed;
    inset: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .filter-container {
    width: 100%;
    max-width: 360px;
    background: white;
    border-radius: 12px;
    margin: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .filter-header {
    position: relative;
    text-align: center;
    padding: 16px;
    border-bottom: 1px solid #eee;
  }
  
  .close-button {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    font-size: 24px;
    color: #666;
    cursor: pointer;
    padding: 4px;
  }
  
  .header-title {
    font-size: 16px;
    font-weight: 500;
  }
  
  .filter-content {
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 24px;
  }
  
  .filter-section {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .filter-title {
    font-size: 14px;
    font-weight: 500;
    color: #333;
  }
  
  .select-group {
    display: flex;
    gap: 8px;
  }
  
  .filter-select {
    flex: 1;
    padding: 8px;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    font-size: 14px;
    color: #333;
  }
  
  .sort-buttons {
    display: flex;
    gap: 8px;
  }
  
  .sort-button {
    flex: 1;
    padding: 8px;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    background: white;
    font-size: 14px;
    color: #666;
    cursor: pointer;
  }
  
  .sort-button.active {
    background: #ff5a5a;
    color: white;
    border-color: #ff5a5a;
  }
  
  .date-inputs {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .date-input {
    flex: 1;
    padding: 8px;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    font-size: 14px;
  }
  
  .date-separator {
    color: #666;
  }
  
  .apply-button {
    width: calc(100% - 40px);
    margin: 0 20px 20px;
    padding: 12px;
    background: #ff5a5a;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
  }
  
  .apply-button:hover {
    background: #ff4141;
  }
  </style>