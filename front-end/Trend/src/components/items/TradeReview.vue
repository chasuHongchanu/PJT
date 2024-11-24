<template>
  <div class="review-card">
    <div class="review-header">
      <img
        :src="review.lesseeProfileImage"
        :alt="review.lesseeNickname"
        class="reviewer-image"
      />
      <div class="reviewer-info">
        <span class="reviewer-name">{{ review.lesseeNickname }}</span>
        <span class="review-date">{{ formatDate(review.reviewCreatedAt) }}</span>
      </div>
    </div>
    <p class="review-content">{{ review.reviewContent }}</p>
  </div>
</template>

<script setup>
const props = defineProps({
  review: {
    type: Object,
    required: true,
  },
});

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString(); // 원하는 형식으로 날짜를 포맷
};
</script>

<style scoped>
.review-card {
  background: #f9f9f9;
  border-radius: 12px;
  padding: 16px;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.reviewer-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}

.reviewer-info {
  display: flex;
  flex-direction: column;
}

.reviewer-name {
  font-weight: 500;
  margin-bottom: 2px;
}

.review-date {
  font-size: 12px;
  color: #666;
}

.review-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  margin: 0;
}
</style>
