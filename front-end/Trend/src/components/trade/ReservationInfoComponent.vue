<template>
  <section class="reservation-info-section">
    <h2 class="section-title">예약 정보</h2>

    <div class="user-info">
      <div class="info-row">
        <span class="label">임차인</span>
        <span class="value">{{ info.lesseeNickname }}</span>
      </div>
      <div class="info-row">
        <span class="label">임대인</span>
        <span class="value">{{ info.lessorNickname }}</span>
      </div>
    </div>

    <div class="price-inputs">
      <div class="input-group">
        <label for="rentalFee">대여료</label>
        <div class="input-with-won">
          <input
            type="number"
            id="rentalFee"
            v-model="rentalFee"
            :placeholder="info.itemPrice?.toString()"
          />
          <span class="won-symbol">₩</span>
        </div>
      </div>

      <div class="input-group">
        <label for="deposit">보증금</label>
        <div class="input-with-won">
          <input type="number" id="deposit" v-model="deposit" />
          <span class="won-symbol">₩</span>
        </div>
      </div>
    </div>

    <div class="date-inputs">
      <label>대여기간</label>
      <div class="date-range">
        <input
          type="date"
          v-model="startDate"
          :min="info.availableRentalStartDate"
          :max="info.availableRentalEndDate"
        />
        <span class="date-separator">-</span>
        <input
          type="date"
          v-model="endDate"
          :min="startDate || info.availableRentalStartDate"
          :max="info.availableRentalEndDate"
        />
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'

const props = defineProps({
  info: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['update:reservation'])

// 날짜 포맷 변환 함수
function formatDateForInput(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toISOString().split('T')[0]
}

// 초기값 설정을 ref 선언 시 바로 수행
const rentalFee = ref(props.info.tradePrice)
const deposit = ref(props.info.tradeDeposit)
const startDate = ref(formatDateForInput(props.info.tradeRentalStartDate))
const endDate = ref(formatDateForInput(props.info.tradeRentalEndDate))

// 값이 변경될 때마다 부모 컴포넌트에 알림
watch([rentalFee, deposit, startDate, endDate], () => {
  emit('update:reservation', {
    rentalFee: rentalFee.value,
    deposit: deposit.value,
    startDate: startDate.value,
    endDate: endDate.value,
  })
})

// 컴포넌트 마운트 시 초기 데이터 전달
onMounted(() => {
  emit('update:reservation', {
    rentalFee: rentalFee.value,
    deposit: deposit.value,
    startDate: startDate.value,
    endDate: endDate.value,
  })
})
</script>

<style scoped>
.reservation-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.rental-period {
  font-size: 24px; /* 크기 증가 */
  font-weight: 500;
  color: #333;
  text-align: center;
  margin-bottom: 32px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.item-info-section,
.reservation-info-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
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
  font-size: 18px;
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

.user-info {
  margin-bottom: 24px;
}

.info-row {
  display: flex;
  margin-bottom: 12px;
}

.label {
  width: 80px;
  color: #666;
}

.value {
  flex: 1;
  font-weight: 500;
}

.input-group {
  margin-bottom: 16px;
}

.input-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
}

.input-with-won {
  position: relative;
  display: flex;
  align-items: center;
}

.input-with-won input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}

.won-symbol {
  position: absolute;
  right: 12px;
  color: #666;
}

.date-inputs {
  margin-top: 24px;
}

.date-range {
  display: flex;
  align-items: center;
  gap: 12px;
}

.date-range input {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.date-separator {
  color: #666;
}

.reserve-button {
  width: 30%; /* 너비 조정 */
  padding: 16px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.reserve-button:hover {
  background-color: #ff2d20;
}

.reserve-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

@media (max-width: 480px) {
  .item-details {
    flex-direction: column;
  }

  .item-image {
    width: 100%;
    height: 200px;
  }

  .date-range {
    flex-direction: column;
  }
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #ff3b30;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

.button-container {
  display: flex;
  justify-content: center;
}
</style>
