<template>
  <div class="info-window-container">
    <img :src="item.itemImage" :alt="item.itemName" class="item-image" />
    <div class="item-info">
      <h3 class="item-name">{{ item.itemName }}</h3>
      <p class="item-location">{{ item.itemAddress }}</p>
      <p class="item-price">{{ formatPrice(item.itemPrice) }}원</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const formatPrice = (price) => {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};
</script>

<style scoped>
.info-window-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  width: 250px;
}

.item-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.item-info {
  padding: 12px;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px 0;
  color: #333;
}

.item-location {
  font-size: 13px;
  color: #666;
  margin: 0 0 8px 0;
}

.item-price {
  font-size: 15px;
  font-weight: 600;
  color: #ff3b30;
  margin: 0;
}
</style>
