<template>
  <DefaultLayout>
    <div class="payment-container">
      <div v-if="isLoading" class="loading">로딩 중...</div>

      <div v-else class="content-wrapper">
        <!-- 날짜 표시 -->
        <div class="date-range">
          {{ formatDateRange(itemInfo?.tradeRentalStartDate, itemInfo?.tradeRentalEndDate) }}
        </div>

        <!-- 상품 정보 -->
        <ItemInfoComponent v-if="itemInfo" :item="itemInfo" />

        <!-- 결제 정보 -->
        <PaymentInfo v-if="Object.keys(paymentInfo).length" :payment-info="paymentInfo" />

        <!-- 결제하기 섹션 -->
        <section class="payment-section">
          <h2 class="section-title">결제하기</h2>

          <div class="payment-info">
            <div class="info-row">
              <span class="label">결제수단</span>
              <span class="value">계좌이체</span>
            </div>

            <div class="info-row">
              <span class="label">내 계좌</span>
              <div class="account-display">
                <span class="bank-label">(국민)</span>
                <span class="account-number">000000-00-000000</span>
              </div>
            </div>

            <div class="info-row">
              <span class="label">이체금액</span>
              <div class="amount-input">
                <input
                  type="text"
                  v-model="transferAmount"
                  placeholder="이체 금액을 입력해주세요"
                />
                <span class="won-symbol">₩</span>
              </div>
            </div>
          </div>

          <button class="payment-button" :disabled="!transferAmount" @click="handlePayment">
            결제하기
          </button>
        </section>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { tradeApi } from '@/api/tradeApi'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ItemInfoComponent from '@/components/trade/ItemInfoComponent.vue'
import PaymentInfo from '@/components/trade/PaymentInfo.vue'

const route = useRoute()
const router = useRouter()
const isLoading = ref(true)
const itemInfo = ref(null)
const transferAmount = ref('')
const paymentInfo = ref({})

const formatDateRange = (start, end) => {
  if (!start || !end) return ''
  const formatDate = (date) => {
    const dateObj = new Date(date)
    const year = dateObj.getFullYear()
    const month = String(dateObj.getMonth() + 1).padStart(2, '0')
    const day = String(dateObj.getDate()).padStart(2, '0')
    return `${year}. ${month}. ${day}`
  }
  return `${formatDate(start)} - ${formatDate(end)}`
}

onMounted(async () => {
  try {
    const tradeId = route.params.id
    const response = await tradeApi.getTradeDetail(tradeId)
    itemInfo.value = response.data
    paymentInfo.value = {
      lessorNickname: response.data.lessorNickname,
      lesseeNickname: response.data.lesseeNickname,
      paymentAccountNumber: response.data.paymentAccountNumber,
      tradePrice: response.data.tradePrice,
      tradeDeposit: response.data.tradeDeposit,
      paymentStatus: response.data.paymentStatus,
    }
  } catch (error) {
    console.error('데이터 로딩 실패:', error)
  } finally {
    isLoading.value = false
  }
})

const handlePayment = async () => {
  try {
    alert('결제가 완료되었습니다.')
    router.push({ name: 'Detail', params: { id: route.params.id } })
  } catch (error) {
    console.error('결제 처리 실패:', error)
    alert('결제 처리 중 오류가 발생했습니다.')
  }
}
</script>

<style scoped>
.payment-container {
  padding: 20px;
}

.content-wrapper {
  max-width: 600px;
  margin: 0 auto;
}

.date-range {
  text-align: center;
  font-size: 24px; /* 크기 증가 */
  font-weight: 500;
  color: #333;
  margin-bottom: 24px;
}

.payment-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-top: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.payment-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.label {
  color: #666;
  font-size: 14px;
}

.account-display {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
}

.bank-label {
  color: #666;
  font-size: 14px;
}

.amount-input {
  position: relative;
  display: flex;
  align-items: center;
}

.amount-input input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  width: 200px;
  text-align: right;
}

.won-symbol {
  position: absolute;
  right: 12px;
  color: #666;
}

.payment-button {
  width: 100%;
  padding: 16px;
  margin-top: 24px;
  background-color: #ff424d;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.payment-button:hover {
  background-color: #ff2c38;
}

.payment-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  font-size: 16px;
  color: #666;
}
</style>
