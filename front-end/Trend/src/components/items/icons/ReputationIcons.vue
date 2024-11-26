<!-- components/icons/ReputationIcons.vue -->
<template>
  <div class="reputation-icon">
    <svg v-if="score <= 2" viewBox="0 0 24 24" class="sprout-icon">
      <!-- 새싹 아이콘 -->
      <path
        d="M12 3L4 9v12h16V9l-8-6z"
        fill="#90EE90"
        stroke="#228B22"
        stroke-width="1.5"
      />
      <path
        d="M12 16c-2-3-7-2-7-8 3 4 7 3 7 8z"
        fill="#228B22"
        stroke="#228B22"
        stroke-width="1"
      />
    </svg>

    <svg v-else-if="score <= 5" viewBox="0 0 24 24" class="growing-sprout-icon">
      <!-- 자란 새싹 아이콘 -->
      <path
        d="M12 2L4 8v14h16V8l-8-6z"
        fill="#90EE90"
        stroke="#228B22"
        stroke-width="1.5"
      />
      <path
        d="M12 15c-2-4-8-3-8-10 4 5 8 4 8 10z"
        fill="#228B22"
        stroke="#228B22"
        stroke-width="1"
      />
      <path
        d="M12 15c2-4 8-3 8-10-4 5-8 4-8 10z"
        fill="#228B22"
        stroke="#228B22"
        stroke-width="1"
      />
    </svg>

    <svg v-else viewBox="0 0 24 24" class="tree-icon">
      <!-- 나무 아이콘 -->
      <path d="M12 2L4 14h16L12 2z" fill="#228B22" stroke="#228B22" stroke-width="1.5" />
      <path d="M12 6L6 16h12L12 6z" fill="#32CD32" stroke="#228B22" stroke-width="1.5" />
      <rect x="11" y="14" width="2" height="8" fill="#8B4513" />
    </svg>
  </div>
</template>

<script setup>
defineProps({
  score: {
    type: Number,
    required: true,
  },
});
</script>

<style scoped>
.reputation-icon svg {
  width: 24px;
  height: 24px;
}
</style>
