<!-- src/components/layout/PageLayout.vue -->
<template>
    <div class="page-container">
      <div class="page-content">
        <aside class="side-menu-container">
          <SideMenu />
        </aside>
        <main class="main-content">
          <slot></slot>
        </main>
      </div>
    </div>
  </template>
  
  <script>
  import SideMenu from '@/components/menu/SideMenu.vue'
  
  export default {
    name: 'PageLayout',
    components: {
      SideMenu
    }
  }
  </script>
  
  <style scoped>
  .page-container {
    width: 100%;
    padding: 20px;
    background-color: var(--color-white);
  }
  
  .page-content {
    width: 80%;
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    gap: 24px;
  }
  
  .side-menu-container {
    width: 240px;
    flex-shrink: 0;
  }
  
  .main-content {
    flex: 1;
  }
  
  @media (max-width: 768px) {
    .page-content {
      flex-direction: column;
    }
  
    .side-menu-container {
      width: 100%;
    }
  }
  </style>