<!-- src/App.vue -->
<template>
  <router-view v-slot="{ Component }">
    <component :is="Component" />
  </router-view>
</template>

<style>
:root {
  --color-background: #f5f5f5;
  --color-white: #ffffff;
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
</style>