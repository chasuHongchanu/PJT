<template>
  <div class="similar-items-grid">
    <div v-for="item in items" :key="item.itemId" class="item-card">
      <img
        v-if="item.imageUrl"
        :src="item.imageUrl"
        :alt="item.itemName"
        class="item-image"
      />
      <div class="item-info">
        <h3 class="item-name">{{ item.itemName }}</h3>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from "vue";
import { storage } from "@/firebase";
import { ref as storageRef, getDownloadURL } from "firebase/storage";

// Props 정의
const props = defineProps({
  category: {
    type: String,
    default: "",
  },
  address: {
    type: String,
    default: "",
  },
});

const items = ref([]);

// Firebase Storage에서 이미지 URL 가져오기
const fetchFirebaseImageUrls = async (items) => {
  try {
    // 모든 이미지 경로에 대해 Firebase Storage URL 생성
    const enrichedItems = await Promise.all(
      items.map(async (item) => {
        if (!item.itemImage) return item; // 이미지 경로가 없으면 그대로 반환
        const imageRef = storageRef(storage, item.itemImage);
        const imageUrl = await getDownloadURL(imageRef);
        return { ...item, imageUrl };
      })
    );
    return enrichedItems;
  } catch (error) {
    console.error("Error fetching Firebase URLs:", error);
    return items;
  }
};

onMounted(async () => {
  try {
    // API 호출: category나 address를 기반으로 URL 결정
    let queryParam = "";
    let url = "";

    if (props.category) {
      queryParam = `category=${props.category}`;
      url = `http://localhost:8080/api/item/rent/similar/1`;
    } else if (props.address) {
      queryParam = `address=${props.address}`;
      url = `http://localhost:8080/api/item/rent/peripheral/1`;
    }

    const response = await fetch(url);
    const data = await response.json();

    // Firebase Storage URL 가져오기
    items.value = await fetchFirebaseImageUrls(data);
  } catch (error) {
    console.error("Error fetching similar items:", error);
  }
});
</script>

<style scoped>
.similar-items-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 16px;
}

.item-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  cursor: pointer;
}

.item-card:hover {
  transform: translateY(-4px);
}

.item-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.item-info {
  padding: 12px;
}

.item-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .similar-items-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .similar-items-grid {
    grid-template-columns: 1fr;
  }
}
</style>
