<template>
  <DefaultLayout>
    <div class="edit-container">
      <!-- 로딩 상태 -->
      <div v-if="isLoading" class="loading-overlay">
        <div class="loading-spinner"></div>
        <p>데이터를 불러오는 중입니다...</p>
      </div>

      <!-- 수정 폼 -->
      <template v-else>
        <h1 class="edit-title">물품 수정</h1>

        <form @submit.prevent="handleSubmit" class="edit-form">
          <!-- 제목 입력 -->
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
            <label>사진 ({{ previewImages.length }}/5)</label>
            <div class="image-upload-container">
              <!-- 업로드된 이미지 미리보기 -->
              <div
                v-for="(image, index) in previewImages"
                :key="`preview-${index}`"
                class="image-preview-wrapper"
              >
                <img :src="image" class="image-preview" />
                <button type="button" class="remove-image" @click="removeImage(index)">×</button>
              </div>

              <!-- 이미지 추가 버튼 -->
              <label v-if="previewImages.length < 5" class="upload-button">
                <input
                  type="file"
                  ref="fileInput"
                  multiple
                  accept="image/*"
                  @change="handleImageUpload"
                  class="hidden"
                />
                <svg xmlns="http://www.w3.org/2000/svg" class="upload-icon" viewBox="0 0 24 24">
                  <path
                    d="M12 5v13M5 12h14"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                  />
                </svg>
                사진 추가
              </label>
            </div>
          </div>

          <!-- 가격 입력 -->
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

          <!-- 카테고리 선택 -->
          <div class="form-group">
            <label>카테고리</label>
            <div class="category-selects">
              <select v-model="selectedMainCategory" @change="updateSubCategories" required>
                <option value="">대분류 선택</option>
                <option v-for="category in categories" :key="category.name" :value="category">
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
                <option v-for="category in subCategories" :key="category.name" :value="category">
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

          <!-- 위치 선택 -->
          <div class="form-group">
            <label>위치</label>
            <div class="location-input">
              <input
                v-model="formData.address"
                type="text"
                readonly
                placeholder="지도에서 위치를 선택해주세요"
                required
              />
              <button type="button" class="map-button" @click="openMapModal">위치 수정하기</button>
            </div>
          </div>

          <!-- 대여 기간 -->
          <div class="form-group">
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

          <!-- 수정하기 버튼 -->
          <div class="button-group">
            <button type="button" class="cancel-button" @click="goBack">취소</button>
            <button type="submit" class="submit-button" :disabled="isSubmitting">
              <div class="button-content">
                <div v-if="isSubmitting" class="loading-spinner"></div>
                <span>{{ isSubmitting ? '수정 중...' : '수정하기' }}</span>
              </div>
            </button>
          </div>
        </form>
      </template>

      <!-- 지도 모달 -->
      <Teleport to="body">
        <div v-if="showMapModal" class="modal-overlay" @click="closeMapModal">
          <div class="modal-content" @click.stop>
            <div class="modal-header">
              <h2>위치 선택</h2>
              <button class="close-button" @click="closeMapModal">&times;</button>
            </div>
            <div class="modal-body">
              <!-- Google Maps 컴포넌트 수정 -->
              <div ref="mapContainer" style="width: 100%; height: 400px"></div>
            </div>
            <div class="modal-footer">
              <button class="confirm-button" @click="confirmLocation" :disabled="!selectedLocation">
                위치 선택 완료
              </button>
            </div>
          </div>
        </div>
      </Teleport>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { GoogleMap, Marker } from 'vue3-google-map'
import { Loader } from '@googlemaps/js-api-loader'
import { storage } from '@/firebase'
import { ref as storageRef, getDownloadURL } from 'firebase/storage'
import categoriesData from '@/data/categories.json'
import { itemApi } from '@/api/itemApi'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const apiKey = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY

// 상태 관리 스토어 초기화
const authStore = useAuthStore()

// 상태 관리
const isLoading = ref(true)
const isSubmitting = ref(false)
const itemId = route.params.id

// 폼 데이터
const formData = ref({
  itemId: null,
  itemName: '',
  itemPrice: '',
  itemMainCategory: '',
  itemSubCategory: '',
  itemSubsubCategory: '',
  latitude: null,
  longitude: null,
  address: '',
  itemContent: '',
  availableRentalStartDate: '',
  availableRentalEndDate: '',
})

// 이미지 관련
const fileInput = ref(null)
const imageFiles = ref([])
const previewImages = ref([])

// 카테고리 관련
const categories = ref(categoriesData.categories)
const subCategories = ref([])
const items = ref([])
const selectedMainCategory = ref(null)
const selectedSubCategory = ref(null)
const selectedItem = ref('')

// 지도 관련
const showMapModal = ref(false)
const mapCenter = ref({ lat: 37.5665, lng: 126.978 })
const selectedLocation = ref(null)

// 초기 데이터 로드 함수
const loadItemData = async () => {
  try {
    const response = await itemApi.getItemForEdit(itemId)
    const data = response.data

    // 응답 데이터 검증
    if (!data) {
      throw new Error('No data received from server')
    }

    // 기본 데이터 설정
    formData.value = {
      ...data,
      itemId: parseInt(itemId),
      availableRentalStartDate: formatDate(data.availableRentalStartDate),
      availableRentalEndDate: formatDate(data.availableRentalEndDate),
    }

    // 카테고리 설정
    if (data.itemMainCategory) {
      const mainCat = categories.value.find((cat) => cat.name === data.itemMainCategory)
      if (mainCat) {
        selectedMainCategory.value = mainCat
        subCategories.value = mainCat.subCategories || []

        await nextTick()

        if (data.itemSubCategory) {
          const subCat = subCategories.value.find((cat) => cat.name === data.itemSubCategory)
          if (subCat) {
            selectedSubCategory.value = subCat
            items.value = subCat.items || []

            await nextTick()

            if (data.itemSubsubCategory) {
              const item = items.value.find((i) => i === data.itemSubsubCategory)
              if (item) {
                selectedItem.value = item
              }
            }
          }
        }
      }
    }

    // 이미지 처리
    if (Array.isArray(data.itemImageNames) && data.itemImageNames.length > 0) {
      previewImages.value = await Promise.all(
        data.itemImageNames.map(async (path) => {
          try {
            return await getDownloadURL(storageRef(storage, path))
          } catch (error) {
            console.error('Error loading image:', error)
            return null
          }
        }),
      ).then((urls) => urls.filter((url) => url !== null))
    }
  } catch (error) {
    console.error('Error loading item:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요합니다.')
      router.push('/auth/login')
    } else {
      alert('물품 정보를 불러오는데 실패했습니다.')
    }
  } finally {
    isLoading.value = false
  }
}

// 날짜 형식을 변환하는 유틸리티 함수 추가
const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.split(' ')[0] // YYYY-MM-DD 부분만 추출
}

// 이미지 핸들러
const handleImageUpload = (event) => {
  const files = Array.from(event.target.files)
  const remainingSlots = 5 - previewImages.value.length
  const filesToAdd = files.slice(0, remainingSlots)

  filesToAdd.forEach((file) => {
    imageFiles.value.push(file)
    const reader = new FileReader()
    reader.onload = (e) => {
      previewImages.value.push(e.target.result)
    }
    reader.readAsDataURL(file)
  })

  event.target.value = ''
}

const removeImage = (index) => {
  imageFiles.value.splice(index, 1)
  previewImages.value.splice(index, 1)
}

// 카테고리 핸들러
const updateSubCategories = () => {
  if (selectedMainCategory.value) {
    subCategories.value = selectedMainCategory.value.subCategories || []
    selectedSubCategory.value = null
    items.value = []
    selectedItem.value = ''
    formData.value.itemMainCategory = selectedMainCategory.value.name
  }
}

const updateItems = () => {
  if (selectedSubCategory.value) {
    items.value = selectedSubCategory.value.items || []
    selectedItem.value = ''
    formData.value.itemSubCategory = selectedSubCategory.value.name
  }
}

// Google Maps 관련
const mapContainer = ref(null)
const map = ref(null)
const marker = ref(null)
const loader = new Loader({
  apiKey: import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY,
  version: 'weekly',
})

// Google Maps 초기화
const initMap = async () => {
  try {
    await loader.load()
    const { Map } = await google.maps.importLibrary('maps')
    const { Marker } = await google.maps.importLibrary('marker')

    map.value = new Map(mapContainer.value, {
      center: mapCenter.value,
      zoom: 14,
    })

    if (selectedLocation.value) {
      marker.value = new Marker({
        position: selectedLocation.value,
        map: map.value,
        title: '선택한 위치',
      })
    }

    map.value.addListener('click', (e) => {
      const location = {
        lat: e.latLng.lat(),
        lng: e.latLng.lng(),
      }
      selectedLocation.value = location

      if (marker.value) {
        marker.value.setPosition(location)
      } else {
        marker.value = new Marker({
          position: location,
          map: map.value,
          title: '선택한 위치',
        })
      }
    })
  } catch (error) {
    console.error('Error initializing map:', error)
  }
}

// 지도 모달 관련 함수 수정
const openMapModal = async () => {
  showMapModal.value = true
  document.body.style.overflow = 'hidden'
  // 모달이 열린 후 지도 초기화
  await nextTick()
  if (!map.value) {
    await initMap()
  }
}

const closeMapModal = () => {
  showMapModal.value = false
  document.body.style.overflow = ''
}

const confirmLocation = async () => {
  if (selectedLocation.value) {
    try {
      const { lat, lng } = selectedLocation.value
      formData.value.latitude = lat
      formData.value.longitude = lng

      const response = await fetch(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${apiKey}&language=ko`,
      )
      const data = await response.json()
      if (data.results[0]) {
        formData.value.address = data.results[0].formatted_address
      }
      closeMapModal()
    } catch (error) {
      console.error('Error getting address:', error)
    }
  }
}

// 폼 제출 핸들러
const handleSubmit = async () => {
  if (isSubmitting.value) return

  try {
    isSubmitting.value = true

    const formDataToSend = new FormData()

    // 기본 데이터 추가
    const requestData = {
      ...formData.value,
      userId: authStore.userId,
      itemStatus: formData.value.itemStatus || 'AVAILABLE',
      itemImageNames: [], // 기존 이미지 이름 배열
    }

    // FormData에 JSON 데이터 추가
    Object.keys(requestData).forEach((key) => {
      if (key !== 'itemImages') {
        formDataToSend.append(key, requestData[key])
      }
    })

    // 새로운 이미지 파일 추가
    imageFiles.value.forEach((file) => {
      formDataToSend.append('itemImages', file)
    })

    await itemApi.updateItem(formDataToSend)

    alert('물품이 성공적으로 수정되었습니다.')
    router.push(`/items/view`)
  } catch (error) {
    console.error('Error updating item:', error)
    if (error.response?.status === 401) {
      router.push('/auth/login')
    } else {
      alert('물품 수정 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
  } finally {
    isSubmitting.value = false
  }
}

// 뒤로가기
const goBack = () => {
  router.push({
    name: 'ItemDetail',
    params: { id: itemId },
  })
}

// 컴포넌트 마운트
onMounted(() => {
  loadItemData()
  console.log(formData)
})

// 지도 모달 감시
watch(showMapModal, async (newValue) => {
  if (newValue && mapContainer.value && !map.value) {
    await initMap()
  }
})

// watch 추가하여 카테고리 값이 변경될 때마다 formData 업데이트
watch(selectedMainCategory, (newValue) => {
  if (newValue) {
    formData.value.itemMainCategory = newValue.name
    subCategories.value = newValue.subCategories || []
    selectedSubCategory.value = null
    items.value = []
    selectedItem.value = ''
  }
})

watch(selectedSubCategory, (newValue) => {
  if (newValue) {
    formData.value.itemSubCategory = newValue.name
    items.value = newValue.items || []
    selectedItem.value = ''
  }
})

watch(selectedItem, (newValue) => {
  if (newValue) {
    formData.value.itemSubsubCategory = newValue
  }
})

watch(selectedMainCategory, (newValue) => {
  if (!newValue) return // 초기화되지 않은 상태에서는 실행하지 않음
  subCategories.value = newValue.subCategories || []
  selectedSubCategory.value = null
  items.value = []
  selectedItem.value = ''
})
</script>

<style scoped>
.edit-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.edit-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 32px;
  color: #333;
}

.edit-form {
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
  font-size: 16px;
}

input,
select,
textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.2s;
}

input:focus,
select:focus,
textarea:focus {
  outline: none;
  border-color: #ff3b30;
  box-shadow: 0 0 0 2px rgba(255, 59, 48, 0.1);
}

textarea {
  resize: vertical;
  min-height: 120px;
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
  border-radius: 8px;
  overflow: hidden;
}

.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  transition: background-color 0.2s;
}

.remove-image:hover {
  background: rgba(0, 0, 0, 0.7);
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
  transition: all 0.2s;
}

.upload-button:hover {
  border-color: #ff3b30;
  color: #ff3b30;
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
  transition: background-color 0.2s;
}

.map-button:hover {
  background-color: #ff2d20;
}

.date-inputs {
  display: flex;
  align-items: center;
  gap: 12px;
}

.date-inputs input {
  flex: 1;
}

.button-group {
  display: flex;
  gap: 12px;
  justify-content: center;
  margin-top: 24px;
}

.submit-button,
.cancel-button {
  padding: 16px 32px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-button {
  background-color: #ff3b30;
  color: white;
}

.cancel-button {
  background-color: #f5f5f5;
  color: #666;
}

.submit-button:hover {
  background-color: #ff2d20;
  transform: translateY(-1px);
}

.cancel-button:hover {
  background-color: #ebebeb;
  transform: translateY(-1px);
}

.submit-button:disabled {
  background-color: #ff3b3099;
  cursor: not-allowed;
  transform: none;
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
  border: 2px solid #fff;
  border-top: 2px solid transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-overlay .loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #ff3b30;
  border-top: 4px solid transparent;
  margin-bottom: 16px;
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
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
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
}

.modal-body {
  flex: 1;
  min-height: 400px;
  padding: 16px;
}

.location-map {
  width: 100%;
  height: 100%;
  min-height: 400px;
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

.confirm-button:disabled {
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

@media screen and (max-width: 768px) {
  .edit-container {
    padding: 16px;
  }

  .category-selects {
    flex-direction: column;
  }

  .location-input {
    flex-direction: column;
  }

  .date-inputs {
    flex-direction: column;
  }

  .map-button {
    width: 100%;
    padding: 12px;
  }

  .button-group {
    flex-direction: column;
  }

  .submit-button,
  .cancel-button {
    width: 100%;
  }
}
</style>
