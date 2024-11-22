<!-- src/views/user/UserProfileEdit.vue -->
<template>
  <DefaultLayout>
    <div class="profile-edit-container">
      <h1 class="page-title">프로필 수정</h1>
      
      <!-- Profile Image Section -->
      <div class="profile-image-section">
        <div class="image-container">
          <img 
            :src="previewImage || '/default-profile.jpg'" 
            alt="Profile"
            class="profile-image"
          />
          <input
            type="file"
            ref="fileInput"
            @change="handleImageChange"
            accept="image/*"
            class="hidden"
          />
          <button 
            @click="$refs.fileInput.click()"
            class="upload-button"
          >
            사진 업로드
          </button>
        </div>
      </div>

      <!-- Profile Form -->
      <form @submit.prevent="handleSubmit" class="profile-form">
        <!-- Nickname -->
        <div class="form-group">
          <label>닉네임</label>
          <input
            v-model="userInfo.nickname"
            type="text"
          />
        </div>

        <!-- Birthday -->
        <div class="form-group">
          <label>내생일 날짜</label>
          <input
            v-model="userInfo.birthday"
            type="text"
          />
        </div>

        <!-- Verification Status -->
        <div class="form-group">
          <label>비밀번호 확인</label>
          <input
            v-model="userInfo.verificationStatus"
            type="password"
          />
        </div>

        <!-- Email -->
        <div class="form-group">
          <label>이메일 주소</label>
          <div class="email-container">
            <input
              v-model="userInfo.email"
              type="email"
              class="email-input"
            />
            <span class="email-divider">@</span>
            <input
              v-model="userInfo.emailDomain"
              type="text"
              class="domain-input"
            />
          </div>
        </div>

        <!-- Address -->
        <div class="form-group">
          <label>주소</label>
          <div class="address-container">
            <input
              v-model="userInfo.address"
              type="text"
              readonly
              class="address-input"
            />
            <button 
              type="button"
              @click="searchAddress"
              class="search-button"
            >
              검색
            </button>
          </div>
          <input
            v-model="userInfo.detailAddress"
            type="text"
            placeholder="상세주소"
            class="detail-address-input"
          />
        </div>

        <!-- Phone -->
        <div class="form-group">
          <label>연락처</label>
          <input
            v-model="userInfo.phone"
            type="tel"
          />
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          class="submit-button"
        >
          수정 완료
        </button>
      </form>
    </div>
  </DefaultLayout>
</template>

<script>
import DefaultLayout from '@/layouts/DefaultLayout.vue'

export default {
  name: 'UserProfileEdit',
  components: {
    DefaultLayout
  },
  data() {
    return {
      userInfo: {
        profileImage: '',
        nickname: '테스트 사용자',
        birthday: '1990-01-01',
        verificationStatus: '',
        email: 'test',
        emailDomain: 'example.com',
        address: '서울시 강남구',
        detailAddress: '테헤란로 123',
        phone: '010-1234-5678'
      },
      previewImage: null
    }
  },
  methods: {
    handleImageChange(event) {
      const file = event.target.files[0]
      if (file) {
        this.previewImage = URL.createObjectURL(file)
      }
    },
    
    searchAddress() {
      alert('주소 검색 기능은 아직 구현되지 않았습니다.')
    },
    
    handleSubmit() {
      alert('프로필 수정이 완료되었습니다. (테스트)')
      console.log('제출된 데이터:', this.userInfo)
    }
  }
}
</script>

<style scoped>
/* 기존 스타일 유지하되 컨테이너 스타일 수정 */
.profile-edit-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  background-color: #ffffff;
  border-radius: 8px;
}

.page-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

.profile-image-section {
  text-align: center;
  margin-bottom: 30px;
}

.image-container {
  display: inline-block;
}

.profile-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
  border: 2px solid #eee;
}

.upload-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.upload-button:hover {
  background-color: #0056b3;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}

.form-group input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
}

.email-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.email-input, .domain-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.email-divider {
  color: #666;
}

.address-container {
  display: flex;
  gap: 10px;
}

.address-input {
  flex: 1;
  background-color: #f8f8f8;
}

.detail-address-input {
  margin-top: 10px;
}

.search-button {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 0 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.search-button:hover {
  background-color: #5a6268;
}

.submit-button {
  background-color: #ff5a5a;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  margin-top: 20px;
}

.submit-button:hover {
  background-color: #ff4141;
}

.hidden {
  display: none;
}

input[readonly] {
  background-color: #f8f8f8;
  cursor: default;
}
</style>