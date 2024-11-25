<template>
  <DefaultLayout>
    <div class="reservation-container">
      <!-- 로딩 상태 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <p>데이터를 불러오는 중입니다...</p>
      </div>

      <!-- 데이터 로드 완료 후 표시 -->
      <template v-else-if="itemInfo">
        <!-- 대여 가능 기간 -->
        <div class="rental-period">
          {{
            formatDateRange(
              itemInfo.availableRentalStartDate,
              itemInfo.availableRentalEndDate
            )
          }}
        </div>

        <!-- 컴포넌트 사용 -->
        <ItemInfoComponent :item="itemInfo" />
        <ReservationInfoComponent
          :info="itemInfo"
          @update:reservation="updateReservation"
        />

        <!-- 예약 버튼 -->
        <div class="button-container">
          <button
            class="reserve-button"
            @click="submitReservation"
            :disabled="!isFormValid"
          >
            예약 변경
          </button>
        </div>
      </template>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import ItemInfoComponent from "@/components/trade/ItemInfoComponent.vue";
import ReservationInfoComponent from "@/components/trade/ReservationInfoComponent.vue";
import { tradeApi } from "@/api/tradeApi";
import DefaultLayout from "@/layouts/DefaultLayout.vue";

const route = useRoute();
const router = useRouter();
const tradeId = route.params.id;
const isLoading = ref(true);
const itemInfo = ref(null);
const reservationData = ref({
  rentalFee: "",
  deposit: "",
  startDate: "",
  endDate: "",
});

///////////////////////////////////// 채팅 연동 //////////////////////////////////////////
const formData = ref({
  lessorId: "user1",
  lesseeId: "user2",
  itemId: 4,
});
///////////////////////////////////// 채팅 연동 //////////////////////////////////////////

// 데이터 로드
onMounted(async () => {
  try {
    console.log(tradeId);
    const response = await tradeApi.getTradeDetail(tradeId);
    const data = response.data;
    itemInfo.value = data;
  } catch (error) {
    console.error("Error loading data:", error);
  } finally {
    isLoading.value = false;
  }
});

// 예약 정보 업데이트
const updateReservation = (data) => {
  reservationData.value = data;
};

// 유효성 검사
const isFormValid = computed(() => {
  const { rentalFee, deposit, startDate, endDate } = reservationData.value;
  return rentalFee && deposit && startDate && endDate;
});

// 날짜 포맷
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

// 예약 제출
const submitReservation = async () => {
  if (!isFormValid.value) return;

  try {
    const requestBody = {
      lessorId: formData.value.lessorId,
      lesseeId: formData.value.lesseeId,
      itemId: formData.value.itemId,
      tradeId: tradeId,
      tradePrice: reservationData.value.rentalFee,
      tradeDeposit: reservationData.value.deposit,
      tradeRentalStartDate: reservationData.value.startDate,
      tradeRentalEndDate: reservationData.value.endDate,
    };

    const response = await tradeApi.updateReservation(requestBody);
    alert("예약 변경되었습니다.");

    router.push({ name: "Detail", params: { id: tradeId } });
  } catch (error) {
    console.error("Error submitting reservation:", error);
    alert("예약 변경 중 오류가 발생했습니다.");
  }
};
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

.management-buttons {
  margin-bottom: 16px;
  padding: 16px;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: center;
  gap: 24px; /* 버튼 사이 간격 증가 */
}

.btn-cancel,
.btn-confirm {
  flex: 0 1 200px; /* flex-grow: 0, flex-shrink: 1, flex-basis: 200px */
  padding: 12px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
