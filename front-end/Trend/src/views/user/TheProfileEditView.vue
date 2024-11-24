<template>
  <DefaultLayout>
    <div class="profile-edit-container">
      <h1 class="page-title">프로필 수정</h1>
      <form @submit.prevent="handleSubmit" class="profile-form">
        <!-- Profile Image Section -->
        <div class="profile-image-section">
          <div class="image-container">
            <img :src="previewImage || defaultProfileImage" alt="Profile" class="profile-image" />
            <input
              type="file"
              ref="fileInput"
              @change="handleImageChange"
              accept="image/*"
              class="hidden"
            />
            <button type="button" @click="$refs.fileInput.click()" class="upload-button">
              사진 업로드
            </button>
          </div>
        </div>

        <!-- Nickname -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>닉네임</label>
          </div>
          <input v-model="formData.userNickname" type="text" placeholder="닉네임을 입력해주세요" />
        </div>

        <!-- Introduction -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>한 줄 소개</label>
          </div>
          <input
            v-model="formData.userIntroduction"
            type="text"
            placeholder="자신을 소개해주세요"
          />
        </div>

        <!-- Password -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>비밀번호</label>
            <div class="input-message" :class="{ error: passwordError, success: passwordSuccess }">
              {{ passwordMessage }}
            </div>
          </div>
          <input
            v-model="formData.userPassword"
            type="password"
            placeholder="8~20자, 문자, 숫자, 특수문자 포함"
            @input="validatePassword"
          />
        </div>

        <!-- Password Confirm -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>비밀번호 확인</label>
            <div
              class="input-message"
              :class="{ error: passwordConfirmError, success: passwordConfirmSuccess }"
            >
              {{ passwordConfirmMessage }}
            </div>
          </div>
          <input
            v-model="passwordConfirm"
            type="password"
            placeholder="비밀번호를 다시 입력해주세요"
            @input="validatePasswordConfirm"
          />
        </div>

        <!-- Country -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>국가</label>
          </div>
          <select v-model="formData.country" class="select-box">
            <option value="">국가를 선택해주세요</option>
            <option value="argentina">아르헨티나</option>
            <option value="australia">호주</option>
            <option value="brazil">브라질</option>
            <option value="canada">캐나다</option>
            <option value="china">중국</option>
            <option value="france">프랑스</option>
            <option value="germany">독일</option>
            <option value="india">인도</option>
            <option value="indonesia">인도네시아</option>
            <option value="italy">이탈리아</option>
            <option value="japan">일본</option>
            <option value="mexico">멕시코</option>
            <option value="russia">러시아</option>
            <option value="saudi-arabia">사우디아라비아</option>
            <option value="south-africa">남아프리카 공화국</option>
            <option value="south-korea">대한민국</option>
            <option value="turkey">튀르키예</option>
            <option value="uk">영국</option>
            <option value="usa">미국</option>
            <option value="eu">유럽연합</option>
          </select>
        </div>

        <!-- Address -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>주소</label>
          </div>
          <div class="input-container">
            <input v-model="roadAddress" type="text" placeholder="도로명 주소" readonly />
            <button type="button" class="inner-button" @click="searchAddress">검색</button>
          </div>
          <input
            v-model="detailAddress"
            type="text"
            placeholder="상세 주소"
            class="detail-address"
            @input="updateFullAddress"
          />
        </div>

        <!-- Email -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>이메일</label>
            <div class="input-message" :class="{ error: emailError }">
              {{ emailMessage }}
            </div>
          </div>
          <div class="email-section">
            <div class="email-row">
              <input
                v-model="emailLocal"
                type="text"
                placeholder="이메일 주소"
                @input="validateEmail"
              />
              <span class="email-at">@</span>
              <select v-model="emailDomain" @change="handleDomainChange">
                <option value="">선택</option>
                <option value="naver.com">naver.com</option>
                <option value="gmail.com">gmail.com</option>
                <option value="daum.net">daum.net</option>
                <option value="direct">직접 입력</option>
              </select>
            </div>
            <input
              v-if="isCustomDomain"
              v-model="customDomain"
              type="text"
              placeholder="도메인 입력"
              class="custom-domain"
              @input="validateEmail"
            />
          </div>
        </div>

        <!-- Phone -->
        <div class="form-group">
          <div class="label-wrapper">
            <label>전화번호</label>
          </div>
          <input
            v-model="formData.userPhoneNumber"
            type="text"
            placeholder="'-'를 포함한 전화번호를 입력해주세요"
          />
        </div>

        <!-- Submit Button -->
        <button type="submit" class="submit-button" :disabled="!isFormValid || isSubmitting">
          수정 완료
        </button>
      </form>
    </div>
  </DefaultLayout>
</template>

<script>
import { ref, computed, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import DefaultProfileImage from '@/assets/default-profile.svg'
import { userApi } from '@/api/userApi'
import { useAuthStore } from '@/stores/auth'
import { storage } from '@/../firebase'
import { ref as storageRef, uploadBytes, getDownloadURL } from 'firebase/storage'

export default {
  name: 'TheProfileEditView',
  components: {
    DefaultLayout,
  },

  async beforeRouteEnter(to, from, next) {
    // 직접 URL 접근 방지
    if (!from.name) {
      next('/user/profile/info')
    } else {
      next()
    }
  },

  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const defaultProfileImage = DefaultProfileImage

    // 폼 데이터
    const formData = ref({
      userPassword: '',
      userNickname: '',
      userAddress: '',
      userEmail: '',
      userPhoneNumber: '',
      userProfileImg: '',
      userIntroduction: '',
      country: '',
    })

    // 이메일 관련 상태
    const emailLocal = ref('')
    const emailDomain = ref('')
    const customDomain = ref('')
    const isCustomDomain = ref(false)
    const emailError = ref(false)
    const emailMessage = ref('')

    // 주소 관련 상태
    const roadAddress = ref('')
    const detailAddress = ref('')

    // 비밀번호 관련 상태
    const passwordConfirm = ref('')
    const passwordError = ref(false)
    const passwordSuccess = ref(false)
    const passwordMessage = ref('')
    const passwordConfirmError = ref(false)
    const passwordConfirmSuccess = ref(false)
    const passwordConfirmMessage = ref('')

    // 이미지 관련 상태
    const previewImage = ref(null)
    const selectedFile = ref(null)

    // 제출 상태
    const isSubmitting = ref(false)

    // 초기 데이터 로드
    const loadUserData = async () => {
      try {
        const userData = await userApi.getUserInfo()

        // 기본 정보 설정
        formData.value = {
          userNickname: userData.userNickname,
          userAddress: userData.userAddress,
          userEmail: userData.userEmail,
          userPhoneNumber: userData.userPhoneNumber,
          userProfileImg: userData.userProfileImg,
          userIntroduction: userData.userIntroduction,
          country: userData.country,
          userPassword: '',
        }

        // 주소 분리
        const addressParts = userData.userAddress.split(' ')
        detailAddress.value = addressParts.pop()
        roadAddress.value = addressParts.join(' ')

        // 이메일 분리
        const [local, domain] = userData.userEmail.split('@')
        emailLocal.value = local
        if (['naver.com', 'gmail.com', 'daum.net'].includes(domain)) {
          emailDomain.value = domain
        } else {
          emailDomain.value = 'direct'
          customDomain.value = domain
          isCustomDomain.value = true
        }

        // 프로필 이미지 설정
        if (userData.userProfileImg) {
          previewImage.value = await authStore.loadFirebaseImage(userData.userProfileImg)
        }
      } catch (error) {
        console.error('유저 정보 로드 실패:', error)
        router.push('/user/profile')
      }
    }

    // 이미지 처리
    const handleImageChange = async (event) => {
      const file = event.target.files[0]
      if (file) {
        selectedFile.value = file
        previewImage.value = URL.createObjectURL(file)
      }
    }

    // 이메일 도메인 처리
    const handleDomainChange = () => {
      isCustomDomain.value = emailDomain.value === 'direct'
      if (isCustomDomain.value) {
        customDomain.value = ''
      }
      validateEmail()
    }

    // 이메일 유효성 검사
    const validateEmail = () => {
      const domain = isCustomDomain.value ? customDomain.value : emailDomain.value
      if (!emailLocal.value || !domain) return

      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
      const fullEmail = `${emailLocal.value}@${domain}`

      if (!emailRegex.test(fullEmail)) {
        emailError.value = true
        emailMessage.value = '올바른 이메일 형식이 아닙니다.'
      } else {
        emailError.value = false
        emailMessage.value = ''
      }

      // 이메일 업데이트
      formData.value.userEmail = fullEmail
    }

    // 비밀번호 유효성 검사
    const validatePassword = () => {
      const pwdRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/

      if (!formData.value.userPassword) {
        passwordMessage.value = ''
        passwordError.value = false
        passwordSuccess.value = false
        return
      }

      if (formData.value.userPassword.length < 8 || formData.value.userPassword.length > 20) {
        passwordError.value = true
        passwordSuccess.value = false
        passwordMessage.value = '8~20자 이내로 문자, 숫자, 특수문자를 포함해야 합니다.'
      } else if (!pwdRegex.test(formData.value.userPassword)) {
        passwordError.value = true
        passwordSuccess.value = false
        passwordMessage.value = '8~20자 이내로 문자, 숫자, 특수문자를 포함해야 합니다.'
      } else {
        passwordError.value = false
        passwordSuccess.value = true
        passwordMessage.value = '사용 가능한 비밀번호 입니다.'
      }
      validatePasswordConfirm()
    }

    // 비밀번호 확인 검사
    const validatePasswordConfirm = () => {
      if (!passwordConfirm.value) {
        passwordConfirmMessage.value = ''
        passwordConfirmError.value = false
        passwordConfirmSuccess.value = false
        return
      }

      if (formData.value.userPassword === passwordConfirm.value) {
        passwordConfirmError.value = false
        passwordConfirmSuccess.value = true
        passwordConfirmMessage.value = '비밀번호가 일치합니다.'
      } else {
        passwordConfirmError.value = true
        passwordConfirmSuccess.value = false
        passwordConfirmMessage.value = '비밀번호가 일치하지 않습니다.'
      }
    }

    // 주소 업데이트
    const updateFullAddress = () => {
      formData.value.userAddress = `${roadAddress.value} ${detailAddress.value}`.trim()
    }

    // 주소 검색
    const searchAddress = () => {
      // 주소 검색 API 연동 로직
      console.log('주소 검색')
    }

    // 폼 유효성 검사 (이어서)
    const isFormValid = computed(() => {
      const requiredFields = [
        formData.value.userNickname,
        formData.value.userEmail,
        formData.value.userPhoneNumber,
        formData.value.country,
        formData.value.userAddress,
      ]

      // 모든 필수 필드가 채워져 있는지 확인
      const hasAllRequiredFields = requiredFields.every((field) => field)

      // 이메일 형식이 올바른지 확인
      const emailValid = !emailError.value

      // 비밀번호가 입력된 경우에만 유효성 검사
      const passwordValid = formData.value.userPassword
        ? !passwordError.value && !passwordConfirmError.value
        : true

      return hasAllRequiredFields && emailValid && passwordValid
    })

    // 폼 제출 처리
    const handleSubmit = async () => {
      if (isSubmitting.value) return
      isSubmitting.value = true

      try {
        // API 요청 데이터 준비
        const updateData = {
          userNickname: formData.value.userNickname,
          userAddress: formData.value.userAddress,
          userEmail: formData.value.userEmail,
          userPhoneNumber: formData.value.userPhoneNumber,
          userIntroduction: formData.value.userIntroduction,
          country: formData.value.country,
        }

        if (formData.value.userPassword) {
          updateData.userPassword = formData.value.userPassword
        }

        // API 호출
        const response = await userApi.updateUserInfo(updateData, selectedFile.value)

        // auth store 업데이트 - response에서 받은 정보로 업데이트
        await authStore.updateProfile({
          userNickname: response.userNickname,
          userProfileImgUrl: response.userProfileImgUrl,
        })

        alert('프로필이 성공적으로 수정되었습니다.')
        router.push('/user/profile/info')
      } catch (error) {
        console.error('프로필 수정 실패:', error)
        alert('프로필 수정에 실패했습니다. 다시 시도해주세요.')
      } finally {
        isSubmitting.value = false
      }
    }

    onBeforeMount(loadUserData)

    return {
      formData,
      emailLocal,
      emailDomain,
      customDomain,
      isCustomDomain,
      emailError,
      emailMessage,
      roadAddress,
      detailAddress,
      passwordConfirm,
      passwordError,
      passwordSuccess,
      passwordMessage,
      passwordConfirmError,
      passwordConfirmSuccess,
      passwordConfirmMessage,
      previewImage,
      defaultProfileImage,
      isSubmitting,
      isFormValid,
      handleImageChange,
      handleDomainChange,
      validateEmail,
      validatePassword,
      validatePasswordConfirm,
      updateFullAddress,
      searchAddress,
      handleSubmit,
    }
  },
}
</script>

<style scoped>
.profile-edit-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 40px 20px;
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
  background-color: #4a90e2;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.upload-button:hover {
  background-color: #357abd;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.label-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  min-width: fit-content;
}

.input-message {
  font-size: 12px;
  margin-left: 8px;
}

.error {
  color: #ff5a5a;
}

.success {
  color: #4a90e2;
}

input,
select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

input:focus,
select:focus {
  outline: none;
  border-color: #4a90e2;
}

.input-container {
  position: relative;
}

.inner-button {
  position: absolute;
  right: 4px;
  top: 50%;
  transform: translateY(-50%);
  padding: 6px 12px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #333;
  font-size: 14px;
  cursor: pointer;
}

.inner-button:hover {
  background-color: #f5f5f5;
}

.email-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.email-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.email-row input,
.email-row select {
  flex: 1;
}

.email-at {
  color: #666;
  padding: 0 4px;
}

.custom-domain {
  width: 100%;
}

.detail-address {
  margin-top: 8px;
}

.submit-button {
  margin-top: 16px;
  padding: 16px;
  background-color: #ff5a5a;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.submit-button:hover {
  background-color: #ff4141;
}

.submit-button:disabled {
  background-color: #ffaeae;
  cursor: not-allowed;
}

.hidden {
  display: none;
}

@media (max-width: 480px) {
  .profile-edit-container {
    padding: 20px 16px;
  }
}
</style>
