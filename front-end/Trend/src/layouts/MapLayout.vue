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
    NavigationBar,
  },
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
  /* 빨간색 화살표 기준: 모바일에서는 좌우 5% 정도의 여백만 */
  width: clamp(85%, calc(85% + 100 * (100vw - 320px) / (1200 - 320)), 95%);
  max-width: 100%;
  margin: 0 auto;
  padding: 12px 0; /* 좌우 패딩 제거 */
  transition: all 0.3s ease;
}

.content-wrapper {
  background-color: white;
  border-radius: 2px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  /* 파란색 화살표 기준: 모바일에서는 좌우 12px 정도의 여백 */
  padding: 0;
}
</style>
