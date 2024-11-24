<template>
  <section class="item-info-section">
    <h2 class="section-title">상품 정보</h2>
    <div class="item-details">
      <!-- 이미지가 로드될 때까지 로딩 상태를 표시 -->
      <div class="image-container">
        <img v-if="imageSrc" :src="imageSrc" :alt="item.itemName" class="item-image" />
        <p v-else>이미지를 불러오는 중...</p>
      </div>
      <div class="item-text">
        <h3 class="item-name">{{ item.itemName }}</h3>
        <p class="item-price">{{ item.itemPrice?.toLocaleString() }}원</p>
        <p class="item-address">{{ item.address }}</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { storage } from "@/firebase";
import { ref as firebaseRef, getDownloadURL } from "firebase/storage";

// Props 정의
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const imageSrc = ref("");

// Firebase에서 이미지 다운로드 URL 가져오기
const fetchImageSrc = async (path) => {
  try {
    const imageRef = firebaseRef(storage, `/${path}`);
    const url = await getDownloadURL(imageRef);
    imageSrc.value = url;
  } catch (error) {
    console.error("이미지 로드 실패:", error);
    const imageRef = firebaseRef(storage, "/items/3/Precourse_수료증.png");
    const url = await getDownloadURL(imageRef);
    imageSrc.value = url;
  }
};

// 컴포넌트 마운트 시 로직
onMounted(() => {
  if (props.item && props.item.thumbnail) {
    fetchImageSrc(props.item.thumbnail); // Props를 통해 전달된 item의 thumbnail 사용
  }
});
</script>

<style scoped>
.item-info-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.item-details {
  display: flex;
  gap: 16px;
}

.item-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
}

.item-text {
  flex: 1;
}

.item-name {
  font-size: 20px; /* 크기 증가 */
  font-weight: 500;
  margin: 0 0 8px 0;
}

.item-price {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px 0;
}

.item-address {
  font-size: 14px;
  color: #666;
  margin: 0;
}
</style>
