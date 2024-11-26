<template>
    <div class="full-width-layout">
      <NavigationBar />
      <main class="main-content">
        <slot></slot>
      </main>
    </div>
  </template>
  
  <script>
  import NavigationBar from '@/components/NavigationBar.vue'
  
  export default {
    name: 'FullWidthLayout',
    components: {
      NavigationBar
    }
  }
  </script>
  
  <style scoped>
  .full-width-layout {
    min-height: 100vh;
    background-color: var(--color-background);
  }
  
  .main-content {
    padding-top: 60px;
    min-height: calc(100vh - 60px);
    width: 100%;
  }
  </style>