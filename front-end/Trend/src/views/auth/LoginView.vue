<template>
  <div class="login-container">
    <div class="login-content">
      <!-- Logo -->
      <div class="logo-container" @click="GoToHome">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <h1 class="logo-text">Trend</h1>
      </div>

      <!-- Login Form -->
      <form @submit.prevent="handleLogin" class="login-form">
        <input
          v-model="userId"
          type="text"
          placeholder="아이디"
          required
          class="login-input"
        />
        <input
          v-model="password"
          type="password"
          placeholder="비밀번호"
          required
          class="login-input"
        />
        <button type="submit" class="login-button" :disabled="isLoading">
          {{ isLoading ? "로그인 중..." : "로그인" }}
        </button>
      </form>

      <!-- Divider -->
      <div class="divider">
        <span class="divider-text">또는</span>
      </div>

      <!-- Additional Links -->
      <div class="links-container">
        <router-link to="/auth/find-id" class="link">아이디 찾기</router-link>
        <span class="separator">/</span>
        <router-link to="/auth/find-password" class="link">비밀번호 찾기</router-link>
        <span class="separator">/</span>
        <router-link to="/auth/signup" class="link">회원가입</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";

export default {
  name: "LoginView",
  setup() {
    const router = useRouter();
    const route = useRoute();
    const authStore = useAuthStore();

    const userId = ref("");
    const password = ref("");
    const isLoading = ref(false);

    const GoToHome = () => {
      router.push("/");
    };

    const handleLogin = async () => {
      if (isLoading.value) return;

      isLoading.value = true;
      try {
        await authStore.login(userId.value, password.value);

        // 리다이렉트 처리
        const redirectPath = route.query.redirect || "/";
        router.push(redirectPath);
      } catch (error) {
        alert("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
      } finally {
        isLoading.value = false;
      }
    };

    return {
      userId,
      password,
      isLoading,
      handleLogin,
      GoToHome
    };
  },
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: white;
}

.login-content {
  width: 100%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo {
  height: 48px;
  width: auto;
}

.logo-text {
  font-size: 32px;
  font-weight: bold;
  color: #ff5a5a;
}

.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.login-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.login-button {
  width: 100%;
  padding: 12px;
  background-color: #ff5a5a;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}

.login-button:disabled {
  background-color: #ff8a8a;
  cursor: not-allowed;
}

.divider {
  width: 100%;
  text-align: center;
  position: relative;
  margin: 12px 0;
}

.divider::before,
.divider::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 45%;
  height: 1px;
  background-color: #ddd;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider-text {
  background-color: white;
  padding: 0 12px;
  color: #888;
  font-size: 14px;
}

.links-container {
  display: flex;
  gap: 8px;
  align-items: center;
}

.link {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.separator {
  color: #ddd;
}
</style>
