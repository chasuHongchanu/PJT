<!-- src/components/layout/PageLayout.vue -->
<template>
    <div class="page-container">
      <div class="page-content">
        <slot></slot>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'PageLayout'
  }
  </script>
  
  <style scoped>
  .page-container {
    width: 100%;
    padding: 20px;
    background-color: var(--color-white);
  }
  
  .page-content {
    width: 90%;
    max-width: 600px;
    margin: 0 auto;
  }
  
  @media (min-width: 768px) {
    .page-container {
      padding: 24px;
    }
    
    .page-content {
      width: 85%;
    }
  }
  
  @media (max-width: 375px) {
    .page-container {
      padding: 16px;
    }
    
    .page-content {
      width: 95%;
    }
  }
  </style>