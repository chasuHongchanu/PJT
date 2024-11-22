<!-- src/layouts/DefaultLayout.vue -->
<template>
  <div class="default-layout">
    <NavigationBar />
    <main class="main-content">
      <slot></slot>
    </main>
  </div>
</template>

<script>
import NavigationBar from '@/components/NavigationBar.vue'

export default {
  name: 'DefaultLayout',
  components: {
    NavigationBar
  }
}
</script>

<style scoped>
.default-layout {
  min-height: 100vh;
  background-color: var(--color-background);
}

.main-content {
  padding-top: 60px; /* NavigationBar 높이만큼 여백 */
  min-height: calc(100vh - 60px);
}

@media (max-width: 767px) {
  .main-content {
    width: 100%;
  }
}
</style>