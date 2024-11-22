<template>
  <div class="modal-overlay" @click="$emit('close')">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <button class="close-btn" @click="$emit('close')">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            class="close-icon"
            fill="none"
          >
            <path
              d="M6 18L18 6M6 6l12 12"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
            />
          </svg>
        </button>
        <h2>필터</h2>
      </div>

      <div class="modal-body">
        <section class="filter-section">
          <h3>카테고리</h3>
          <div class="tab-group">
            <button
              v-for="category in ['대분류', '중분류', '소분류']"
              :key="category"
              :class="['tab-btn', { active: selectedCategory === category }]"
              @click="selectedCategory = category"
            >
              {{ category }}
            </button>
          </div>
        </section>

        <section class="filter-section">
          <h3>지역</h3>
          <div class="tab-group">
            <button
              v-for="region in ['국가', '지역', '도시']"
              :key="region"
              :class="['tab-btn', { active: selectedRegion === region }]"
              @click="selectedRegion = region"
            >
              {{ region }}
            </button>
          </div>
        </section>

        <section class="filter-section">
          <h3>가격</h3>
          <div class="price-range">
            <input
              type="number"
              v-model="minPrice"
              placeholder="최저"
              class="price-input"
              min="0"
            />
            <span class="range-separator">~</span>
            <input
              type="number"
              v-model="maxPrice"
              placeholder="최고"
              class="price-input"
              min="0"
            />
          </div>
        </section>
      </div>

      <div class="modal-footer">
        <button class="apply-btn" @click="applyFilters">검색</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedCategory = ref('대분류')
const selectedRegion = ref('국가')
const minPrice = ref('')
const maxPrice = ref('')

const emit = defineEmits(['close', 'apply'])

const applyFilters = () => {
  emit('apply', {
    category: selectedCategory.value,
    region: selectedRegion.value,
    price: {
      min: minPrice.value,
      max: maxPrice.value,
    },
  })
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-end;
  z-index: 2000;
}

.modal-content {
  background-color: white;
  border-radius: 20px 20px 0 0;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  padding: 24px;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  margin-bottom: 24px;
}

.modal-header h2 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  position: absolute;
  left: 0;
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
}

.close-icon {
  width: 20px;
  height: 20px;
  stroke: #333;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-section h3 {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #666;
}

.tab-group {
  display: flex;
  gap: 8px;
  background-color: #f5f5f5;
  padding: 4px;
  border-radius: 12px;
}

.tab-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-btn.active {
  background-color: white;
  color: #ff3b30;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.price-range {
  display: flex;
  align-items: center;
  gap: 12px;
}

.price-input {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s ease;
}

.price-input:focus {
  border-color: #ff3b30;
}

.range-separator {
  color: #666;
  font-weight: 500;
}

.modal-footer {
  margin-top: 32px;
}

.apply-btn {
  width: 100%;
  padding: 16px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.apply-btn:hover {
  background-color: #ff2d20;
  transform: translateY(-1px);
}

@media screen and (max-width: 768px) {
  .modal-content {
    padding: 20px;
  }

  .tab-btn {
    padding: 10px;
    font-size: 13px;
  }

  .price-input {
    padding: 10px;
    font-size: 13px;
  }
}
</style>
