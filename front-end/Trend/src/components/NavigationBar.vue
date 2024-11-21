<!-- src/components/NavigationBar.vue -->
<template>
  <nav class="navigation-bar">
    <div class="nav-content">
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
  position: sticky;
  top: 0;
  width: 100%;
  height: var(--nav-height);
  background-color: var(--color-white);
  border-bottom: 1px solid #ddd;
  z-index: 1000;
}

.nav-content {
  width: 100%;
  height: 100%;
  max-width: var(--max-content-width);
  margin: 0 auto;
  padding: 0 var(--page-padding);
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  gap: 16px;
}

.nav-icons button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
}

.icon {
  width: 24px;
  height: 24px;
}
</style>