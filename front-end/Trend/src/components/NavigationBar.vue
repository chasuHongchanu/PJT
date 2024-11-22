<template>
  <nav class="navigation-bar">
    <!-- Left Logo -->
    <div class="logo" @click="goToMainPage">
      <img src="@/assets/nav/logo.png" alt="Logo" class="logo-image" />
      <span class="logo-text">Trend</span>
    </div>

    <!-- Right Navigation Icons -->
    <div class="nav-icons">
      <button @click="goToChatPage">
        <img src="@/assets/nav/chat-icon.svg" alt="Chat" class="icon" />
      </button>
      <button @click="goToMenuPage">
        <img src="@/assets/nav/menu-icon.svg" alt="Menu" class="icon" />
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: "NavigationBar",
  methods: {
    goToMainPage() {
      this.$router.push("/");
    },
    goToChatPage() {
      this.$router.push("/chat");
    },
    goToMenuPage() {
      this.$router.push("/menu");
    },
  },
};
</script>

<style scoped>
.navigation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #ffffff;
  border-bottom: 1px solid #ddd;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-image {
  width: 24px;
  height: 24px;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #ff5a5a;
}

.nav-icons {
  display: flex;
  align-items: center;
}

.nav-icons button {
  background: none;
  border: none;
  margin-left: 16px;
  cursor: pointer;
}

.icon {
  width: 24px;
  height: 24px;
}
</style>