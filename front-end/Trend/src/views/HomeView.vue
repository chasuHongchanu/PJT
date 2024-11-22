<!-- src/views/HomeView.vue -->
<template>
    <DefaultLayout>
      <div class="home">
        <h1>홈페이지</h1>
        <p>레이아웃 테스트를 위한 홈 콘텐츠입니다.</p>
      </div>
    </DefaultLayout>
  </template>
  
  <script>
  import DefaultLayout from '@/layouts/DefaultLayout.vue'
  
  export default {
    name: 'HomeView',
    components: {
      DefaultLayout
    }
  }
  </script>