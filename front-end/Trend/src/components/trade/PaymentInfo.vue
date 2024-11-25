<template>
  <section class="payment-info-section">
    <h2 class="section-title">결제 정보</h2>
    <div class="payment-details">
      <div class="info-row">
        <span class="label">가상계좌번호</span>
        <div class="value-wrapper">
          <div class="value-container">
            <span class="value">{{ paymentInfo.paymentAccountNumber }}</span>
            <span
              v-if="paymentInfo.paymentStatus"
              :class="['status-badge', getStatusClass(paymentInfo.paymentStatus)]"
            >
              {{ paymentInfo.paymentStatus }}
            </span>
          </div>
          <!-- 결제하기 버튼 -->
          <button v-if="true" class="pay-button" @click="pay">결제하기</button>
        </div>
      </div>
      <div class="info-row">
        <span class="label">보증금</span>
        <span class="value">{{ formatPrice(paymentInfo.tradeDeposit) }}원</span>
      </div>
      <div class="info-row">
        <span class="label">대여료</span>
        <span class="value">{{ formatPrice(paymentInfo.tradePrice) }}원</span>
      </div>
      <div class="info-row">
        <span class="label">임차인</span>
        <span class="value">{{ paymentInfo.lesseeNickname }}</span>
      </div>
      <div class="info-row">
        <span class="label">임대인</span>
        <span class="value">{{ paymentInfo.lessorNickname }}</span>
      </div>
    </div>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  paymentInfo: {
    type: Object,
    required: true,
  },
  tradeId: {
    type: String,
    required: true,
  },
})

const formatPrice = (price) => {
  return price?.toLocaleString() ?? '0'
}

const getStatusClass = (status) => {
  const statusMap = {
    입금완료: 'status-complete',
    미입금: 'status-pending',
    취소: 'status-cancelled',
  }
  return statusMap[status] || 'status-pending'
}

const pay = () => {
  router.push({ name: 'Pay', params: { id: props.tradeId } })
}
</script>

<style scoped>
.payment-info-section {
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

.payment-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label {
  color: #666;
  font-size: 14px;
}

.value-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-complete {
  background-color: #e6f3ff;
  color: #0066ff;
}

.status-pending {
  background-color: #fff3e6;
  color: #ff9900;
}

.status-cancelled {
  background-color: #ffe6e6;
  color: #ff0000;
}

.value-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.value-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pay-button {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  background-color: #ff6b6b;
  color: white;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.pay-button:hover {
  background-color: #ff5252;
}
</style>
