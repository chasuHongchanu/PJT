<template>
  <DefaultLayout>
    <div class="lease-container">
      <!-- 로딩 상태 -->
      <div v-if="isLoading" class="loading">로딩 중...</div>

      <!-- 로딩 완료 후 표시 -->
      <div v-else>
        <!-- 날짜 정보 -->
        <div class="date-range">
          {{
            formatDateRange(itemData?.tradeRentalStartDate, itemData?.tradeRentalEndDate)
          }}
        </div>

        <!-- 상품 정보 -->
        <section class="content-section">
          <h2 class="section-title">상품 정보</h2>
          <div class="content-wrapper">
            <ItemInfoComponent v-if="itemInfo" :item="itemInfo" />
            <PhotoUploadComponent
              v-if="showPhotoUpload"
              :images="itemImages"
              @update:images="handleImagesUpdate"
            />
          </div>
        </section>

        <!-- 결제 정보 -->
        <PaymentInfo
          v-if="Object.keys(paymentInfo).length"
          :payment-info="paymentInfo"
          :trade-id="tradeId"
        />

        <!-- 대여 시작 버튼 -->
        <button class="start-lease-button" @click="handleStartLease">대여 시작</button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { tradeApi } from "@/api/tradeApi";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ItemInfoComponent from "@/components/trade/ItemInfoComponent.vue";
import PaymentInfo from "@/components/trade/PaymentInfo.vue";
import PhotoUploadComponent from "@/components/trade/PhotoUploadComponent.vue";

const route = useRoute();
const router = useRouter();
const itemData = ref(null);
const itemImages = ref([]);
const isLoading = ref(true); // 로딩 상태
const tradeId = route.params.id;
const showPhotoUpload = computed(() => itemData.value?.status !== "대여 중");

const itemInfo = computed(() => {
  if (!itemData.value) return null;
  return {
    itemId: itemData.value.itemId,
    itemName: itemData.value.itemName,
    itemPrice: itemData.value.itemPrice,
    address: itemData.value.address,
    thumbnail: itemData.value.thumbnail,
  };
});

const paymentInfo = computed(() => {
  if (!itemData.value) return {};
  return {
    lessorNickname: itemData.value.lessorNickname,
    lesseeNickname: itemData.value.lesseeNickname,
    paymentAccountNumber: itemData.value.paymentAccountNumber,
    tradePrice: itemData.value.tradePrice,
    tradeDeposit: itemData.value.tradeDeposit,
    paymentStatus: itemData.value.paymentStatus,
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

const handleImagesUpdate = (newImages) => {
  itemImages.value = newImages;
};

const handleStartLease = async () => {
  if (!itemImages.value.length) {
    alert("물품 상태 사진을 등록해주세요.");
    return;
  }

  try {
    const formData = new FormData();
    formData.append("tradeId", tradeId);
    itemImages.value.forEach((image) => {
      formData.append("itemConditionImages", image);
    });

    await tradeApi.insertItemConditionImages(formData);

    router.push(`/trade/startDetail/${tradeId}`);
  } catch (error) {
    console.error("대여 시작 실패:", error);
    alert("대여 시작에 실패했습니다. 다시 시도해주세요.");
  }
};

onMounted(async () => {
  try {
    isLoading.value = true; // 로딩 시작
    const response = await tradeApi.getTradeDetail(tradeId);
    itemData.value = response.data; // 데이터 설정
  } catch (error) {
    console.error("데이터 로딩 실패:", error);
  } finally {
    isLoading.value = false; // 로딩 종료
  }
});
</script>

<style scoped>
.lease-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.date-range {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 32px 0;
  letter-spacing: -0.5px;
}
.content-section {
  background: white;
  border-radius: 12px;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding: 20px 20px 0;
}

.content-wrapper {
  padding: 0 20px 20px;
}

.start-lease-button {
  width: 30%;
  padding: 16px;
  background-color: #000;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  margin: 20px auto 0;
  display: block;
}

.start-lease-button:hover {
  opacity: 0.9;
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
