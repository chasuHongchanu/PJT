<template>
  <div class="search-container">
    <input type="text" class="search-bar" :placeholder="placeholder" v-model="searchText" />
    <button class="search-btn" @click="onSearch">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="search-icon"
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
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue'

defineProps({
  placeholder: {
    type: String,
    default: 'Search location...',
  },
})

const emit = defineEmits(['search'])
const searchText = ref('')

const onSearch = () => {
  if (searchText.value.trim()) {
    emit('search', searchText.value.trim())
  }
}
</script>

<style scoped>
.search-container {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 30px;
  padding: 5px 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 400px;
  margin: 0 auto;
  transition: all 0.3s ease;
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
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s ease;
}

.search-btn:hover {
  background: #ff2d20;
}

.search-icon {
  width: 20px;
  height: 20px;
}

@media screen and (max-width: 768px) {
  .search-container {
    width: 80%;
    max-width: 300px;
  }

  .search-bar {
    font-size: 14px;
    padding: 6px 10px;
  }

  .search-btn {
    padding: 6px 12px;
  }

  .search-icon {
    width: 16px;
    height: 16px;
  }
}

@media screen and (max-width: 480px) {
  .search-container {
    width: 70%;
    max-width: 250px;
  }

  .search-bar {
    font-size: 12px;
    padding: 4px 8px;
  }

  .search-btn {
    padding: 4px 8px;
  }

  .search-icon {
    width: 14px;
    height: 14px;
  }
}
</style>
