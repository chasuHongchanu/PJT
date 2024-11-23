<template>
  <div class="default-layout">
    <NavigationBar />
    <main class="main-content">
      <div class="content-container">
        <div class="content-wrapper">
          <slot></slot>
        </div>
      </div>
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
  /* NavigationBar 높이만큼 여백 */
  padding-top: 60px; 
  min-height: calc(100vh - 60px);
}

.content-container {
  width: 80%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 20px;
}

.content-wrapper {
  background-color: white;
  border-radius: 2px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

@media (max-width: 767px) {
  .content-container {
    flex-direction: column;

  }
  
  .content-wrapper {
    padding: 22px;
  }
}
</style>