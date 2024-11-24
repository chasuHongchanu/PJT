<template>
  <div class="article-card">
    <div class="article-images" v-if="article.articleImages?.length">
      <img
        :src="article.articleImages[currentImageIndex]"
        :alt="article.articleTitle"
        class="article-image"
      />
      <div v-if="article.articleImages.length > 1" class="image-indicators">
        <span
          v-for="(_, index) in article.articleImages"
          :key="index"
          :class="{ active: currentImageIndex === index }"
          @click="currentImageIndex = index"
        ></span>
      </div>
    </div>
    <div class="article-content">
      <h3 class="article-title">{{ article.articleTitle }}</h3>
      <p class="article-description">{{ truncatedContent }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const props = defineProps({
  article: {
    type: Object,
    required: true,
  },
});

const currentImageIndex = ref(0);

// content 글자수 제한하는 computed 속성
const truncatedContent = computed(() => {
  const content = props.article.articleContent || "";
  return content.length > 30 ? content.slice(0, 30) + "..." : content;
});
</script>

<style scoped>
.article-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.article-images {
  position: relative;
  aspect-ratio: 16/9;
  background: #f5f5f5;
}

.article-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-indicators {
  position: absolute;
  bottom: 12px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
}

.image-indicators span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.image-indicators span.active {
  background: white;
}

.article-content {
  padding: 20px;
}

.article-title {
  font-size: 20px; /* 제목 크기 증가 */
  font-weight: 600;
  margin: 0 0 12px 0; /* 제목과 내용 사이 간격 추가 */
  line-height: 1.4;
  color: #333;
}

.article-description {
  font-size: 14px; /* 내용 텍스트 크기 */
  color: #666;
  margin: 0;
  line-height: 1.5;
}
</style>
