<template>
  <DefaultLayout>
    <div class="register-container">
      <h1>물품 등록</h1>

      <form @submit.prevent="handleSubmit" class="register-form">
        <!-- 제목 -->
        <div class="form-group">
          <label for="itemName">제목</label>
          <input
            id="itemName"
            v-model="formData.itemName"
            type="text"
            placeholder="물품 이름을 입력해주세요"
            required
          />
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
          <label>사진 등록 ({{ imageFiles.length }}/5)</label>
          <div class="image-upload-container">
            <div
              v-for="(image, index) in previewImages"
              :key="index"
              class="image-preview-wrapper"
            >
              <img :src="image" class="image-preview" />
              <button type="button" class="remove-image" @click="removeImage(index)">
                ×
              </button>
            </div>
            <label v-if="imageFiles.length < 5" class="upload-button">
              <input
                type="file"
                ref="fileInput"
                multiple
                accept="image/*"
                @change="handleImageUpload"
                class="hidden"
              />
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="upload-icon"
                viewBox="0 0 24 24"
              >
                <path
                  d="M12 5v13M5 12h14"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                />
              </svg>
              {{ imageFiles.length === 0 ? "0/5" : `${imageFiles.length}/5` }}
            </label>
          </div>
        </div>

        <!-- 가격 -->
        <div class="form-group">
          <label for="itemPrice">가격 (1일)</label>
          <input
            id="itemPrice"
            v-model="formData.itemPrice"
            type="number"
            placeholder="1일 대여 가격을 입력해주세요"
            required
          />
        </div>

        <!-- 카테고리 -->
        <div class="form-group">
          <label>카테고리</label>
          <div class="category-selects">
            <select v-model="selectedMainCategory" @change="updateSubCategories" required>
              <option value="">대분류 선택</option>
              <option
                v-for="category in categories"
                :key="category.name"
                :value="category"
              >
                {{ category.name }}
              </option>
            </select>

            <select
              v-if="subCategories.length"
              v-model="selectedSubCategory"
              @change="updateItems"
              required
            >
              <option value="">중분류 선택</option>
              <option
                v-for="category in subCategories"
                :key="category.name"
                :value="category"
              >
                {{ category.name }}
              </option>
            </select>

            <select v-if="items.length" v-model="selectedItem" required>
              <option value="">소분류 선택</option>
              <option v-for="item in items" :key="item" :value="item">
                {{ item }}
              </option>
            </select>
          </div>
        </div>

        <!-- 지역 선택 -->
        <div class="form-group">
          <label>지역</label>
          <div class="location-input">
            <input
              v-model="formData.address"
              type="text"
              readonly
              placeholder="지도에서 위치를 선택해주세요"
              required
            />
            <button type="button" class="map-button" @click="openMapModal">
              지도에서 선택하기
            </button>
          </div>
        </div>

        <!-- 대여 기간 -->
        <div class="form-group date-range">
          <label>대여 가능 기간</label>
          <div class="date-inputs">
            <input type="date" v-model="formData.availableRentalStartDate" required />
            <span>~</span>
            <input type="date" v-model="formData.availableRentalEndDate" required />
          </div>
        </div>

        <!-- 상세 설명 -->
        <div class="form-group">
          <label for="itemContent">상세 설명</label>
          <textarea
            id="itemContent"
            v-model="formData.itemContent"
            rows="5"
            placeholder="물품에 대한 상세한 설명을 입력해주세요"
            required
          ></textarea>
        </div>

        <!-- 등록 버튼 -->
        <button type="submit" class="submit-button" :disabled="isSubmitting">
          <div class="button-content">
            <div v-if="isSubmitting" class="loading-spinner"></div>
            <span>{{ isSubmitting ? "등록 중입니다..." : "등록하기" }}</span>
          </div>
        </button>
      </form>
    </div>

    <!-- 지도 모달 -->
    <Teleport to="body">
      <div v-if="showMapModal" class="modal-overlay">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h2>위치 선택</h2>
            <button class="close-button" @click="closeMapModal">&times;</button>
          </div>
          <div class="modal-body">
            <GoogleMap
              class="location-map"
              :api-key="apiKey"
              :center="mapCenter"
              :zoom="14"
              @click="handleMapClick"
            >
              <Marker
                v-if="selectedLocation"
                :options="{
                  position: selectedLocation,
                  title: '선택한 위치',
                }"
              />
            </GoogleMap>
          </div>
          <div class="modal-footer">
            <button
              class="confirm-button"
              @click="confirmLocation"
              :disabled="!selectedLocation"
            >
              위치 선택 완료
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </DefaultLayout>
</template>

<script setup>
import { ref, reactive } from "vue";
import { GoogleMap, Marker } from "vue3-google-map";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import categoriesData from "@/data/categories.json";
import { useRouter } from "vue-router";

// Google Maps API 키
const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY;

// 폼 데이터
const formData = reactive({
  itemName: "",
  itemPrice: "",
  itemMainCategory: "",
  itemSubCategory: "",
  itemSubsubCategory: "",
  latitude: null,
  longitude: null,
  address: "",
  itemContent: "",
  availableRentalStartDate: "",
  availableRentalEndDate: "",
});

const router = useRouter();
// 이미지 업로드 관련
const fileInput = ref(null);
const imageFiles = ref([]);
const previewImages = ref([]);
// 등록 관련
const isSubmitting = ref(false);

const triggerImageUpload = () => {
  fileInput.value.click();
};

const handleImageUpload = (event) => {
  const files = Array.from(event.target.files);
  const remainingSlots = 5 - imageFiles.value.length;
  const filesToAdd = files.slice(0, remainingSlots);

  filesToAdd.forEach((file) => {
    imageFiles.value.push(file);
    const reader = new FileReader();
    reader.onload = (e) => {
      previewImages.value.push(e.target.result);
    };
    reader.readAsDataURL(file);
  });

  // 파일 input 초기화
  event.target.value = "";
};

const removeImage = (index) => {
  imageFiles.value.splice(index, 1);
  previewImages.value.splice(index, 1);
};

// 카테고리 관련
const categories = ref(categoriesData.categories);
const subCategories = ref([]);
const items = ref([]);
const selectedMainCategory = ref("");
const selectedSubCategory = ref("");
const selectedItem = ref("");

const updateSubCategories = () => {
  if (selectedMainCategory.value) {
    subCategories.value = selectedMainCategory.value.subCategories || [];
    selectedSubCategory.value = "";
    items.value = [];
    selectedItem.value = "";
    formData.itemMainCategory = selectedMainCategory.value.name;
  }
};

const updateItems = () => {
  if (selectedSubCategory.value) {
    items.value = selectedSubCategory.value.items || [];
    selectedItem.value = "";
    formData.itemSubCategory = selectedSubCategory.value.name;
  }
};

// 지도 관련
const showMapModal = ref(false);
const mapCenter = ref({ lat: 37.5665, lng: 126.978 });
const selectedLocation = ref(null);

// 지도 모달 열기
const openMapModal = () => {
  showMapModal.value = true;
  document.body.style.overflow = "hidden"; // 배경 스크롤 방지
};

// 지도 모달 닫기
const closeMapModal = () => {
  showMapModal.value = false;
  document.body.style.overflow = ""; // 배경 스크롤 복구
};

// 지도 클릭 핸들러
const handleMapClick = (event) => {
  const lat = event.latLng.lat();
  const lng = event.latLng.lng();
  selectedLocation.value = { lat, lng };
};

// 위치 선택 완료
const confirmLocation = async () => {
  if (selectedLocation.value) {
    try {
      const { lat, lng } = selectedLocation.value;
      formData.latitude = lat;
      formData.longitude = lng;

      // 구글 Geocoding API를 사용하여 주소 가져오기
      const response = await fetch(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${apiKey}&language=ko`
      );
      const data = await response.json();

      if (data.results[0]) {
        formData.address = data.results[0].formatted_address;
      }

      closeMapModal();
    } catch (error) {
      console.error("Error getting address:", error);
    }
  }
};

// 폼 제출 핸들러
const handleSubmit = async () => {
  if (isSubmitting.value) return; // 중복 제출 방지

  try {
    isSubmitting.value = true; // 로딩 시작
    formData.itemSubsubCategory = selectedItem.value;

    const formDataToSend = new FormData();
    Object.keys(formData).forEach((key) => {
      if (key !== "itemImages") {
        formDataToSend.append(key, formData[key]);
      }
    });

    imageFiles.value.forEach((file) => {
      formDataToSend.append("itemImages", file);
    });

    const response = await fetch("http://localhost:8080/api/item/rent", {
      method: "POST",
      body: formDataToSend,
    });

    if (response.ok) {
      // 성공 처리
      console.log("Item registered successfully");
      // 등록 완료 후 처리 (예: 페이지 이동)
      router.push(`/items/view`);
    }
  } catch (error) {
    console.error("Error registering item:", error);
  } finally {
    isSubmitting.value = false; // 로딩 종료
  }
};
</script>

<style scoped>
.register-container h1 {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 32px;
  color: #333;
}

.register-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

label {
  font-weight: 500;
  color: #333;
}

input,
select,
textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.image-upload-container {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.image-preview-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
}

.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.remove-image {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
}

.upload-button {
  width: 100px;
  height: 100px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #666;
}

.upload-icon {
  width: 24px;
  height: 24px;
  margin-bottom: 4px;
}

.hidden {
  display: none;
}

.category-selects {
  display: flex;
  gap: 12px;
}

.category-selects select {
  flex: 1;
}

.location-input {
  display: flex;
  gap: 12px;
}

.location-input input {
  flex: 1;
}

.map-button {
  padding: 0 20px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.date-range {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.date-inputs {
  display: flex;
  align-items: center;
  gap: 12px;
}

.date-inputs input {
  flex: 1;
}

.submit-button {
  width: 30%; /* 너비 줄임 */
  padding: 16px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin: 24px auto 0; /* 중앙 정렬을 위해 margin auto 추가 */
  display: block; /* 중앙 정렬을 위해 block으로 변경 */
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 800px;
  height: 80vh;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 스크롤 방지 */
}

.modal-header {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.modal-header h2 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 4px;
}

.modal-body {
  flex: 1;
  position: relative;
  height: 500px; /* 명시적인 높이 지정 */
  padding: 16px;
}

.location-map {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.modal-footer {
  padding: 16px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: center;
}

.confirm-button {
  padding: 12px 24px;
  background-color: #ff3b30;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.confirm-button:hover {
  background-color: #ff2d20;
}

/* 반응형 스타일 */
@media screen and (max-width: 768px) {
  .register-container {
    padding: 16px;
  }

  .category-selects {
    flex-direction: column;
  }

  .location-input {
    flex-direction: column;
  }

  .map-button {
    width: 100%;
    padding: 12px;
  }

  .date-inputs {
    flex-direction: column;
  }

  .modal-content {
    width: 95%;
    max-height: 95vh;
  }
}

/* 입력 필드 포커스 스타일 */
input:focus,
select:focus,
textarea:focus {
  outline: none;
  border-color: #ff3b30;
  box-shadow: 0 0 0 2px rgba(255, 59, 48, 0.1);
}

/* 버튼 호버 효과 */
.submit-button:hover,
.map-button:hover {
  background-color: #ff2d20;
  transform: translateY(-1px);
}

/* 업로드 버튼 호버 효과 */
.upload-button:hover {
  border-color: #ff3b30;
  color: #ff3b30;
}

/* 에러 상태 스타일 */
.error-input {
  border-color: #ff3b30;
}

.error-message {
  color: #ff3b30;
  font-size: 12px;
  margin-top: 4px;
}

/* 이미지 프리뷰 애니메이션 */
.image-preview-wrapper {
  transition: transform 0.2s;
}

.image-preview-wrapper:hover {
  transform: scale(1.05);
}

/* 로딩 상태 스타일 */
.loading {
  opacity: 0.7;
  pointer-events: none;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #ff3b30;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 날짜 입력 필드 커스텀 스타일 */
input[type="date"] {
  appearance: none;
  -webkit-appearance: none;
  padding: 12px;
  background-color: white;
}

input[type="date"]::-webkit-calendar-picker-indicator {
  cursor: pointer;
  opacity: 0.6;
}

input[type="date"]::-webkit-calendar-picker-indicator:hover {
  opacity: 1;
}

/* 숫자 입력 필드 화살표 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* 텍스트 영역 리사이즈 방지 */
textarea {
  resize: vertical;
  min-height: 100px;
}

/* 선택 불가능한 옵션 스타일 */
option[disabled] {
  color: #999;
}

/* 모달 애니메이션 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-content {
  animation: modal-in 0.3s ease-out;
}

@keyframes modal-in {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 커스텀 스크롤바 */
.register-container {
  scrollbar-width: thin;
  scrollbar-color: #ff3b30 #f0f0f0;
}

.register-container::-webkit-scrollbar {
  width: 8px;
}

.register-container::-webkit-scrollbar-track {
  background: #f0f0f0;
  border-radius: 4px;
}

.register-container::-webkit-scrollbar-thumb {
  background: #ff3b30;
  border-radius: 4px;
}

.register-container::-webkit-scrollbar-thumb:hover {
  background: #ff2d20;
}

.submit-button {
  /* 기존 스타일 유지 */
  position: relative;
  overflow: hidden;
}

.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top-color: #ffffff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.submit-button:disabled {
  background-color: #ff3b3099;
  cursor: not-allowed;
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
