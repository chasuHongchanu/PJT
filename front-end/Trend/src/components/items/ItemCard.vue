<template>
  <div class="item-card" @click="goToItemDetail">
    <div class="item-image-container">
      <img :src="item.itemImage" :alt="item.itemName" class="item-image" />
    </div>
    <div class="item-info">
      <h3 class="item-name">{{ item.itemName }}</h3>
      <p class="item-price">₩ {{ formatPrice(item.itemPrice) }}</p>
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  item: {
    type: Object,
    required: true,
    default: () => ({
      itemId: '',
      itemImage: '',
      itemName: '알 수 없음',
      itemPrice: 0,
    }),
  },
})

const formatPrice = (price) => {
  return price.toLocaleString()
}

const goToItemDetail = () => {
  router.push({
    name: 'ItemDetail',
    params: { id: props.item.itemId },
  })
}
</script>

<style scoped>
.item-card {
  display: flex;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  padding: 12px;
  margin-bottom: 12px;
  transition: transform 0.2s;
  border: none; /* border 제거 */
  height: 110px;
}

/* hover 효과도 그림자 대신 다른 효과 적용 */
.item-card:hover {
  transform: translateY(-2px);
  background-color: #f8f9fa; /* hover 시 배경색 살짝 변경 */
}
.item-image-container {
  width: 86px;
  height: 86px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
}

.item-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  padding: 4px 0 4px 16px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.3;
}

.item-price {
  font-size: 14px;
  color: #555;
  margin: 0;
  font-weight: 500;
}
</style>
