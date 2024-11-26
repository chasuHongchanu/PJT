<template>
  <div class="lessor-info">
    <div class="profile-section">
      <img :src="profileImg" :alt="nickname" class="profile-image" />
      <div class="profile-details">
        <h1 class="nickname">{{ nickname }}</h1>
        <div class="reputation">
          <ReputationIcons :score="reputation" />
          <span class="reputation-score">
            {{ reputation ? reputation.toFixed(1) : '0.0' }}
          </span>
        </div>
      </div>
    </div>

    <div class="stats">
      <div class="stat-item">
        <span class="stat-label">후기</span>
        <span class="stat-value">{{ reviewCount }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">물품</span>
        <span class="stat-value">{{ tradeCount }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import ReputationIcons from '@/components/items/icons/ReputationIcons.vue'

const props = defineProps({
  lessorId: String,
  profileImg: String,
  nickname: String,
  reputation: Number,
  reviewCount: Number,
  tradeCount: Number,
})
</script>

<style scoped>
.lessor-info {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.profile-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.profile-image {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 16px;
}

.profile-details {
  flex: 1;
}

.nickname {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
}

.reputation {
  display: flex;
  align-items: center;
  gap: 8px;
}

.reputation-icon {
  width: 24px;
  height: 24px;
}

.reputation-score {
  color: #666;
  font-weight: 500;
}

.stats {
  display: flex;
  justify-content: center;
  align-items: center;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: #eee;
}
</style>
