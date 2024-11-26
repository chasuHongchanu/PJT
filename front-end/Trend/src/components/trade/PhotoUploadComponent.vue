<template>
  <div class="item-photo-section">
    <h3 class="photo-title">사용 전 사진을 등록해주세요</h3>
    <div class="image-upload-container">
      <!-- 기존 이미지 미리보기 -->
      <div
        v-for="(image, index) in previewImages"
        :key="index"
        class="image-preview-wrapper"
      >
        <img :src="image" class="image-preview" />
        <button class="remove-image" @click="removeImage(index)">×</button>
      </div>

      <!-- 이미지 업로드 버튼 -->
      <label v-if="imageFiles.length < 5" class="upload-button">
        <input
          type="file"
          ref="fileInput"
          multiple
          accept="image/*"
          @change="handleImageUpload"
          class="hidden"
        />
        <div class="upload-icon">
          <!-- 예시 이미지 아이콘 -->
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
          >
            <path d="M12 4v16m-8-8h16" stroke-width="2" stroke-linecap="round" />
          </svg>
        </div>
        {{ imageFiles.length }}/5
      </label>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  images: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["update:images"]);

const imageFiles = ref([]);
const previewImages = ref([]);
const fileInput = ref(null);

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

  // 부모 컴포넌트에 업데이트된 이미지 파일 배열 전달
  emit("update:images", imageFiles.value);

  // 파일 input 초기화
  event.target.value = "";
};

const removeImage = (index) => {
  imageFiles.value.splice(index, 1);
  previewImages.value.splice(index, 1);

  // 부모 컴포넌트에 업데이트된 이미지 파일 배열 전달
  emit("update:images", imageFiles.value);
};
</script>

<style scoped>
.item-photo-section {
  margin: 20px 0;
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
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
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

/* 호버 효과 */
.upload-button:hover {
  border-color: #ff3b30;
  color: #ff3b30;
}

.remove-image:hover {
  background: rgba(0, 0, 0, 0.7);
}

.photo-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 12px;
}
</style>
