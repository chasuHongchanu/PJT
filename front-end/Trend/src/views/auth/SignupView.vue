<template>
  <div class="signup-container">
    <!-- 로고 섹션 수정 -->
    <div class="logo-section">
      <div class="logo-wrapper">
        <!-- 이 div 추가 -->
        <img src="@/assets/logo.png" alt="Trend" class="logo-image" />
        <h1 class="logo-text">Trend</h1>
      </div>
    </div>

    <form @submit.prevent="handleSignup" class="signup-form">
      <!-- 아이디 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>아이디</label>
          <div class="input-message" :class="{ error: idError, success: idSuccess }">
            {{ idMessage }}
          </div>
        </div>
        <div class="input-container">
          <input
            v-model="userId"
            type="text"
            placeholder="아이디 입력 (6~20자)"
            @input="validateId"
          />
          <button
            type="button"
            class="inner-button"
            @click="checkIdDuplicate"
            :disabled="!isIdValid"
          >
            중복 확인
          </button>
        </div>
      </div>

      <!-- 비밀번호 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>비밀번호</label>
          <div
            class="input-message"
            :class="{ error: passwordError, success: passwordSuccess }"
          >
            {{ passwordMessage }}
          </div>
        </div>
        <input
          v-model="password"
          type="password"
          placeholder="비밀번호 입력 (문자, 숫자, 특수문자 포함)"
          @input="validatePassword"
        />
      </div>

      <!-- 비밀번호 확인 -->
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
          placeholder="비밀번호를 다시 입력해주세요."
          @input="validatePasswordConfirm"
        />
      </div>

      <!-- 닉네임 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>닉네임</label>
        </div>
        <input v-model="nickname" type="text" placeholder="닉네임을 입력해주세요." />
      </div>

      <!-- 이메일 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>이메일</label>
          <div class="input-message" :class="{ error: emailError }">
            {{ emailMessage }}
          </div>
        </div>
        <div class="email-section">
          <div class="email-row">
            <input v-model="emailLocal" type="text" placeholder="이메일 주소" />
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
            @input="validateEmail"
            class="custom-domain"
          />
        </div>
      </div>

      <!-- 전화번호 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>전화번호</label>
        </div>
        <input
          v-model="phoneNumber"
          type="text"
          placeholder="'-'를 포함한 전화번호를 입력해주세요."
        />
      </div>

      <!-- 국가 선택 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>국가</label>
        </div>
        <select v-model="country" class="select-box">
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

      <!-- 주소 -->
      <div class="form-group">
        <div class="label-wrapper">
          <label>주소</label>
        </div>
        <div class="input-container">
          <input v-model="address" type="text" placeholder="도로명 주소" />
          <button type="button" class="inner-button" @click="searchAddress">검색</button>
        </div>
        <input
          v-model="addressDetail"
          type="text"
          placeholder="상세 주소"
          class="detail-address"
        />
      </div>

      <!-- 회원가입 버튼 -->
      <button
        type="submit"
        class="signup-button"
        :disabled="!isFormValid || !isIdChecked || idDuplicated || isSubmitting"
      >
        회원가입
      </button>
    </form>
  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useRouter, onBeforeRouteLeave } from "vue-router";
import { authApi } from "@/api/authApi";

export default {
  name: "SignupView",
  setup() {
    // 라우터 객체 가져오기
    const router = useRouter();

    // 입력 필드
    const userId = ref("");
    const password = ref("");
    const passwordConfirm = ref("");
    const nickname = ref("");
    const emailLocal = ref("");
    const emailDomain = ref("");
    const customDomain = ref("");
    const phoneNumber = ref("");
    const country = ref("");
    const address = ref("");
    const addressDetail = ref("");

    // 유효성 검사 상태
    const idError = ref(false);
    const idSuccess = ref(false);
    const idMessage = ref("");
    const isIdValid = ref(false);
    const isIdChecked = ref(false);
    const idDuplicated = ref(false);

    const passwordError = ref(false);
    const passwordSuccess = ref(false);
    const passwordMessage = ref("");

    const passwordConfirmError = ref(false);
    const passwordConfirmSuccess = ref(false);
    const passwordConfirmMessage = ref("");

    const emailError = ref(false);
    const emailMessage = ref("");
    const isCustomDomain = ref(false);

    const isSubmitting = ref(false);

    // 아이디 유효성 검사
    const validateId = () => {
      const length = userId.value.length;
      if (length < 6 || length > 20) {
        idError.value = true;
        idSuccess.value = false;
        idMessage.value = "아이디는 6~20자 이내입니다.";
        isIdValid.value = false;
      } else {
        idError.value = false;
        idMessage.value = "";
        isIdValid.value = true;
      }
      isIdChecked.value = false;
    };

    // ID 중복 검사
    const checkIdDuplicate = async () => {
      try {
        const response = await authApi.checkDuplicateId(userId.value);
        idDuplicated.value = response.data.isDuplicated;
        if (idDuplicated.value) {
          idError.value = true;
          idSuccess.value = false;
          idMessage.value = "사용할 수 없는 아이디입니다.";
        } else {
          idError.value = false;
          idSuccess.value = true;
          idMessage.value = "사용할 수 있는 아이디입니다.";
        }
        isIdChecked.value = true;
      } catch (error) {
        console.error("아이디 중복 확인 실패:", error);
        alert("아이디 중복 확인 중 오류가 발생했습니다.");
      }
    };

    // 비밀번호 유효성 검사
    const validatePassword = () => {
      const pwdRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;
      if (password.value.length < 8 || password.value.length > 20) {
        passwordError.value = true;
        passwordSuccess.value = false;
        passwordMessage.value = "8~20자 이내의 비밀번호를 입력해주세요.";
      } else if (!pwdRegex.test(password.value)) {
        passwordError.value = true;
        passwordSuccess.value = false;
        passwordMessage.value = "문자, 숫자, 특수문자가 모두 포함되어야 합니다.";
      } else {
        passwordError.value = false;
        passwordSuccess.value = true;
        passwordMessage.value = "사용할 수 있는 비밀번호입니다.";
      }
      validatePasswordConfirm();
    };

    // 비밀번호 확인 검사
    const validatePasswordConfirm = () => {
      if (!passwordConfirm.value) {
        passwordConfirmMessage.value = "";
        return;
      }
      if (password.value === passwordConfirm.value) {
        passwordConfirmError.value = false;
        passwordConfirmSuccess.value = true;
        passwordConfirmMessage.value = "비밀번호가 일치합니다.";
      } else {
        passwordConfirmError.value = true;
        passwordConfirmSuccess.value = false;
        passwordConfirmMessage.value = "비밀번호가 일치하지 않습니다.";
      }
    };

    // 이메일 도메인 처리
    const handleDomainChange = () => {
      isCustomDomain.value = emailDomain.value === "direct";
      if (isCustomDomain.value) {
        customDomain.value = "";
      }
      validateEmail();
    };

    // 이메일 유효성 검사
    const validateEmail = () => {
      const domain = isCustomDomain.value ? customDomain.value : emailDomain.value;
      if (!emailLocal.value || !domain) return;

      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      const fullEmail = `${emailLocal.value}@${domain}`;

      if (!emailRegex.test(fullEmail)) {
        emailError.value = true;
        emailMessage.value = "올바른 이메일 형태가 아닙니다.";
      } else {
        emailError.value = false;
        emailMessage.value = "";
      }
    };

    // 주소 검색
    const searchAddress = () => {
      // 주소 검색 API 연동 로직
      console.log("주소 검색");
    };

    // 폼 유효성 검사
    const isFormValid = computed(() => {
      const emailDomainValue = isCustomDomain.value
        ? customDomain.value
        : emailDomain.value;
      return (
        userId.value &&
        password.value &&
        passwordConfirm.value &&
        nickname.value &&
        emailLocal.value &&
        emailDomainValue &&
        phoneNumber.value &&
        country.value &&
        address.value &&
        addressDetail.value &&
        !passwordError.value &&
        !passwordConfirmError.value &&
        !emailError.value
      );
    });

    const resetForm = () => {
      userId.value = "";
      password.value = "";
      passwordConfirm.value = "";
      nickname.value = "";
      emailLocal.value = "";
      emailDomain.value = "";
      customDomain.value = "";
      phoneNumber.value = "";
      country.value = "";
      address.value = "";
      addressDetail.value = "";
      idError.value = false;
      idSuccess.value = false;
      idMessage.value = "";
      isIdValid.value = false;
      isIdChecked.value = false;
      idDuplicated.value = false;
      passwordError.value = false;
      passwordSuccess.value = false;
      passwordMessage.value = "";
      passwordConfirmError.value = false;
      passwordConfirmSuccess.value = false;
      passwordConfirmMessage.value = "";
      emailError.value = false;
      emailMessage.value = "";
    };

    // 라우트 변경 시 상태 초기화
    onBeforeRouteLeave((to, from, next) => {
      resetForm();
      next();
    });

    // 회원가입 처리
    const handleSignup = async () => {
      if (isSubmitting.value) return; // 중복 호출 방지

      // 폼 유효성 검사
      if (!isFormValid.value) {
        let message = "다음 항목을 입력해주세요:\n";
        if (!userId.value) message += "- 아이디\n";
        if (!password.value) message += "- 비밀번호\n";
        if (!passwordConfirm.value) message += "- 비밀번호 확인\n";
        if (!nickname.value) message += "- 닉네임\n";
        if (!emailLocal.value || !emailDomain.value) message += "- 이메일\n";
        if (!phoneNumber.value) message += "- 전화번호\n";
        if (!country.value) message += "- 국가\n";
        if (!address.value || !addressDetail.value) message += "- 주소\n";
        alert(message);
        return;
      }

      // ID 중복 확인 여부 검사
      if (!isIdChecked.value) {
        alert("아이디 중복 확인을 해주세요.");
        return;
      }

      const domain = isCustomDomain.value ? customDomain.value : emailDomain.value;
      const fullEmail = `${emailLocal.value}@${domain}`;
      const fullAddress = `${address.value} ${addressDetail.value}`;

      isSubmitting.value = true;

      try {
        // 회원가입 요청
        await authApi.signup({
          userId: userId.value,
          userPassword: password.value,
          userNickname: nickname.value,
          userEmail: fullEmail,
          userPhoneNumber: phoneNumber.value,
          country: country.value,
          userAddress: fullAddress,
        });

        alert("회원가입이 완료되었습니다.");
        // 폼 상태 초기화
        resetForm();
        // 로그인 페이지로 이동
        router.push("/auth/login");
      } catch (error) {
        console.error("회원가입 실패:", error);
        if (error.response?.data?.message) {
          alert(error.response.data.message);
        } else {
          alert("회원가입에 실패했습니다. 다시 시도해주세요.");
        }
      } finally {
        isSubmitting.value = false; // 로딩 상태 해제
      }
    };

    return {
      userId,
      password,
      passwordConfirm,
      nickname,
      emailLocal,
      emailDomain,
      customDomain,
      phoneNumber,
      country,
      address,
      addressDetail,
      idError,
      idSuccess,
      idMessage,
      isIdValid,
      isIdChecked,
      idDuplicated,
      passwordError,
      passwordSuccess,
      passwordMessage,
      passwordConfirmError,
      passwordConfirmSuccess,
      passwordConfirmMessage,
      emailError,
      emailMessage,
      isCustomDomain,
      isFormValid,
      validateId,
      checkIdDuplicate,
      validatePassword,
      validatePasswordConfirm,
      handleDomainChange,
      validateEmail,
      searchAddress,
      handleSignup,
    };
  },
};
</script>

<style scoped>
.signup-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 40px 20px;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.logo-image {
  width: 60px;
  height: auto;
}

.logo-text {
  color: #ff5a5a;
  font-size: 32px;
  font-weight: bold;
}

.signup-form {
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

.input-container {
  position: relative;
}

input,
select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
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

.inner-button:disabled {
  background-color: #f5f5f5;
  color: #999;
  cursor: not-allowed;
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

.signup-button {
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

.signup-button:hover {
  background-color: #ff4141;
}

.signup-button:disabled {
  background-color: #ffaeae;
  cursor: not-allowed;
}

@media (max-width: 480px) {
  .signup-container {
    padding: 20px 16px;
  }
}
</style>
