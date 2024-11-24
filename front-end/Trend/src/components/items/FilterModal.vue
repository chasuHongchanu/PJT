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
          <div class="custom-select-group">
            <div class="custom-select" :class="{ active: selectedCategory }">
              <select v-model="selectedCategory" @change="updateSubCategories">
                <option disabled value="">대분류 선택</option>
                <option
                  v-for="category in categories"
                  :key="category.name"
                  :value="category"
                >
                  {{ category.name }}
                </option>
              </select>
            </div>

            <div
              v-if="subCategories.length"
              class="custom-select"
              :class="{ active: selectedSubCategory }"
            >
              <select v-model="selectedSubCategory" @change="updateItems">
                <option disabled value="">중분류 선택</option>
                <option
                  v-for="subCategory in subCategories"
                  :key="subCategory.name"
                  :value="subCategory"
                >
                  {{ subCategory.name }}
                </option>
              </select>
            </div>

            <div
              v-if="items.length"
              class="custom-select"
              :class="{ active: selectedItem }"
            >
              <select v-model="selectedItem">
                <option disabled value="">소분류 선택</option>
                <option v-for="item in items" :key="item" :value="item">
                  {{ item }}
                </option>
              </select>
            </div>
          </div>
        </section>

        <section class="filter-section">
          <h3>지역</h3>
          <div class="custom-select-group">
            <div class="custom-select" :class="{ active: selectedRegion }">
              <select v-model="selectedRegion" @change="updateCities">
                <option disabled value="">국가 선택</option>
                <option v-for="region in regions" :key="region.name" :value="region">
                  {{ region.name }}
                </option>
              </select>
            </div>

            <div
              v-if="cities.length"
              class="custom-select"
              :class="{ active: selectedCity }"
            >
              <select v-model="selectedCity" @change="updateAreas">
                <option disabled value="">도시 선택</option>
                <option v-for="city in cities" :key="city.name" :value="city">
                  {{ city.name }}
                </option>
              </select>
            </div>

            <div
              v-if="areas.length"
              class="custom-select"
              :class="{ active: selectedArea }"
            >
              <select v-model="selectedArea">
                <option disabled value="">지역 선택</option>
                <option v-for="area in areas" :key="area.name" :value="area">
                  {{ area.name }}
                </option>
              </select>
            </div>
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
import { ref } from "vue";
import categoriesData from "@/data/categories.json";
import regionsData from "@/data/regions.json";

// Categories
const categories = ref(categoriesData.categories);
const subCategories = ref([]);
const items = ref([]);
const selectedCategory = ref(null);
const selectedSubCategory = ref(null);
const selectedItem = ref("");

// Regions
const regions = ref(regionsData.regions);
const cities = ref([]);
const areas = ref([]);
const selectedRegion = ref(null);
const selectedCity = ref(null);
const selectedArea = ref("");

// Price
const minPrice = ref("");
const maxPrice = ref("");

// Emits
const emit = defineEmits(["close", "apply"]);

// Category Logic
const updateSubCategories = () => {
  subCategories.value = selectedCategory.value?.subCategories || [];
  selectedSubCategory.value = null;
  items.value = [];
};

const updateItems = () => {
  items.value = selectedSubCategory.value?.items || [];
  selectedItem.value = "";
};

// Region Logic
const updateCities = () => {
  cities.value = selectedRegion.value?.subRegions || [];
  selectedCity.value = null;
  areas.value = [];
};

const updateAreas = () => {
  areas.value = selectedCity.value?.areas || [];
  selectedArea.value = "";
};

// Apply Filters
const applyFilters = () => {
  emit("apply", {
    category: {
      main: selectedCategory?.value?.name || null,
      sub: selectedSubCategory?.value?.name || null,
      item: selectedItem.value || null,
    },
    region: {
      main: selectedRegion?.value?.name || null,
      city: selectedCity?.value?.name || null,
      area: selectedArea?.value || null,
    },
    price: {
      min: minPrice.value,
      max: maxPrice.value,
    },
  });
};
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
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background-color: white;
  border-radius: 20px;
  width: 40%;
  max-height: 80vh;
  overflow-y: auto;
  padding: 24px;
  animation: slideIn 0.3s ease-out;
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

.custom-select-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.custom-select {
  position: relative;
  width: 100%;
}

.custom-select select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  appearance: none;
  background: white;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.custom-select select:focus {
  outline: none;
  border-color: #b1d3f3;
}

.custom-select.active select {
  border-color: #b1d3f3;
}

.custom-select::after {
  content: "";
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top-color: #b1d3f3;
  pointer-events: none;
}

.custom-select.active::after {
  border-top-color: #ff3b30;
}

.price-range {
  display: flex;
  align-items: center;
  gap: 12px;
}

.price-input {
  flex: 1;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
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
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.apply-btn {
  width: 200px;
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

@keyframes slideIn {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media screen and (max-width: 768px) {
  .modal-content {
    width: 90%;
    padding: 20px;
  }
}
</style>
