<template>
  <DefaultLayout>
    <div class="reservation-detail">
      <div v-if="isLoading" class="loading">로딩 중...</div>

      <div v-else>
        <div class="date-range" v-if="reservationData">
          {{
            formatDateRange(
              reservationData.tradeRentalStartDate,
              reservationData.tradeRentalEndDate
            )
          }}
        </div>

        <div class="item-section">
          <h2 class="section-title">상품 정보</h2>
          <div class="item-wrapper">
            <ItemInfoComponent v-if="itemInfo" :item="itemInfo" />
            <div v-if="reservationData?.tradeState" class="status-badge">
              {{ reservationData.tradeState }}
            </div>
          </div>
        </div>

        <PaymentInfo
          v-if="Object.keys(paymentInfo).length"
          :payment-info="paymentInfo"
          :trade-id="tradeId"
          class="payment-section"
        />

        <!-- 추가된 버튼 컨테이너 -->
        <div class="button-container">
          <button class="btn-cancel" @click="goToEditPage">예약 변경</button>
          <button class="btn-confirm" @click="goToDetailPage">예약 취소</button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { tradeApi } from "@/api/tradeApi";
import ItemInfoComponent from "@/components/trade/ItemInfoComponent.vue";
import PaymentInfo from "@/components/trade/PaymentInfo.vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";

const route = useRoute();
const router = useRouter();
const reservationData = ref(null);
const paymentInfo = ref({});
const isLoading = ref(true);
const tradeId = computed(() => route.params.id);

// 상품 정보를 재구성하는 computed 속성
const itemInfo = computed(() => {
  if (!reservationData.value) return null;

  return {
    itemId: reservationData.value.itemId,
    itemName: reservationData.value.itemName,
    itemPrice: reservationData.value.itemPrice,
    address: reservationData.value.address,
    thumbnail: reservationData.value.thumbnail,
    lessorId: reservationData.value.lessorId,
    lesseeId: reservationData.value.lesseeId,
  };
});

const formatDateRange = (start, end) => {
  if (!start || !end) return "";
  const formatDate = (date) => {
    const dateObj = new Date(date);
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, "0");
    const day = String(dateObj.getDate()).padStart(2, "0");
    return `${year}. ${month}. ${day}`;
  };
  return `${formatDate(start)} - ${formatDate(end)}`;
};

const fetchReservationDetail = async () => {
  try {
    isLoading.value = true;
    const tradeId = route.params.id;
    const response = await tradeApi.getTradeDetail(tradeId);
    reservationData.value = response.data;
    paymentInfo.value = {
      lessorNickname: response.data.lessorNickname,
      lesseeNickname: response.data.lesseeNickname,
      paymentAccountNumber: response.data.paymentAccountNumber,
      tradePrice: response.data.tradePrice,
      tradeDeposit: response.data.tradeDeposit,
      tradeState: response.data.tradeState,
      paymentStatus: response.data.paymentStatus,
      lessorId: response.data.lessorId,
      lesseeId: response.data.lesseeId,
    };
  } catch (error) {
    console.error("예약 정보 로딩 실패:", error);
  } finally {
    isLoading.value = false;
  }
};

// 예약 변경 페이지로 이동
const goToEditPage = () => {
  router.push({ name: "ReservationUpdate", params: { id: tradeId.value } });
};

// 예약 상세 페이지로 이동
const goToDetailPage = () => {
  router.push({ name: "DetailReservation", params: { id: tradeId.value } });
};

onMounted(() => {
  fetchReservationDetail();
});
</script>

<style scoped>
.reservation-detail {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.date-range {
  text-align: center;
  font-size: 24px;
  font-weight: 500;
  color: #333;
  padding: 20px 20px 0;
}

.item-section {
  padding: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.item-wrapper {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.status-badge {
  background-color: #6b6b6b;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  margin-left: 16px;
  flex-shrink: 0;
}

.button-container {
  display: flex;
  gap: 12px;
  margin: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
}

.btn-cancel {
  background-color: #f5f5f5;
  color: #333;
}

.btn-confirm {
  background-color: #ffffff;
  color: #333;
  border: 1px solid #ddd;
}

.btn-cancel:hover,
.btn-confirm:hover {
  opacity: 0.9;
}

:deep(.item-image) {
  margin-right: 16px;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  font-size: 16px;
  color: #666;
}

/* PaymentInfo 컴포넌트와의 경계선 스타일 */
.payment-section {
  border-top: 1px solid #eee;
}
</style>
