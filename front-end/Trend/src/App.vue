<template>
  <div id="app">
    <!-- 공통 네비게이션 바 -->
    <NavigationBar />
    <!-- 페이지 렌더링 -->
    <router-view />
  </div>
</template>

<script>
import NavigationBar from "@/components/NavigationBar.vue";
export default {
  components: {
    NavigationBar,
  },
  name: "App",
};
</script>

<style>
/* 전역 스타일 설정 */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f9f9f9;
}
#app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
</style>