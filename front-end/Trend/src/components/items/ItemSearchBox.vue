<template>
  <div class="search-container">
    <input
      type="text"
      class="search-bar"
      placeholder="Search location..."
      v-model="searchText"
    />
    <button class="search-btn" @click="onSearch">
      <i class="fa fa-search"></i>
    </button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref } from "vue";

defineProps({
  placeholder: {
    type: String,
    default: "Search location...",
  },
});

const emit = defineEmits(["search"]);
const searchText = ref("");

const onSearch = () => {
  if (searchText.value.trim()) {
    emit("search", searchText.value.trim()); // 검색어 전달
  }
};
</script>

<style scoped>
.search-container {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 30px;
  padding: 5px 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  width: 90%; /* Default width for larger screens */
  max-width: 400px;
  position: absolute;
  top: 120px; /* Fixed vertical position */
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  transition: all 0.3s ease; /* Smooth resizing */
}

.search-bar {
  flex: 1;
  border: none;
  outline: none;
  padding: 8px 12px;
  font-size: 16px; /* Default font size for larger screens */
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

/* Media query for smaller screens (max-width: 768px) */
@media screen and (max-width: 768px) {
  .search-container {
    width: 80%; /* Reduce width for smaller screens */
    max-width: 300px; /* Reduce the maximum width */
    /* Top position remains fixed */
  }

  .search-bar {
    font-size: 14px; /* Reduce font size */
    padding: 6px 10px; /* Adjust padding for smaller inputs */
  }

  .search-btn {
    padding: 6px 12px; /* Adjust button padding */
  }
}

/* Media query for very small screens (max-width: 480px) */
@media screen and (max-width: 480px) {
  .search-container {
    width: 70%; /* Further reduce width */
    max-width: 250px; /* Further reduce maximum width */
    /* Top position remains fixed */
  }

  .search-bar {
    font-size: 12px; /* Reduce font size further */
    padding: 4px 8px; /* Adjust padding for very small inputs */
  }

  .search-btn {
    padding: 4px 8px; /* Adjust button padding for smaller buttons */
  }
}
</style>
