<!-- src/views/RentalView.vue -->
<template>
    <DefaultLayout>
      <div class="rental">
        <h1>여행 물품 대여</h1>
        <div class="item-grid">
          <!-- 대여 물품 목록 -->
          <div v-for="i in 6" :key="i" class="item-card">
            물품 ${i}
          </div>
        </div>
      </div>
    </DefaultLayout>
  </template>
  
  <script>
  import DefaultLayout from '@/layouts/DefaultLayout.vue'
  
  export default {
    name: 'RentalView',
    components: {
      DefaultLayout
    }
  }
  </script>
  
  <style scoped>
  .item-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
    margin-top: 20px;
  }
  
  .item-card {
    background: #f0f0f0;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
  }
  </style>