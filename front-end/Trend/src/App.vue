<!-- src/App.vue -->
<template>
  <div id="app">
    <NavigationBar />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
import NavigationBar from "@/components/NavigationBar.vue";

export default {
  name: "App",
  components: {
    NavigationBar,
  },
};
</script>

<style>
:root {
  /* Colors */
  --color-background: #f9f9f9;
  --color-white: #ffffff;
  
  /* Layout */
  --content-width: 85%;
  --max-content-width: 768px;
  
  /* Spacing */
  --nav-height: 60px;
  --page-padding: 20px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: var(--color-background);
}

#app {
  min-height: 100vh;
}

.main-content {
  width: var(--content-width);
  max-width: var(--max-content-width);
  margin: 0 auto;
  padding: var(--page-padding) 0;
  background-color: var(--color-white);
  min-height: calc(100vh - var(--nav-height));
}

@media (max-width: 768px) {
  :root {
    --content-width: 100%;
    --page-padding: 16px;
  }
  
  .main-content {
    padding: var(--page-padding);
  }
}
</style>