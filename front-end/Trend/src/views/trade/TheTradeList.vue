<template>
  <DefaultLayout>
    <div class="app-container">
      <div class="tabs">
        <button
          v-for="tab in tabs"
          :key="tab.id"
          :class="['tab-button', { active: currentTab === tab.id }]"
          @click="currentTab = tab.id"
        >
          {{ tab.name }}
        </button>
      </div>

      <div class="content-area">
        <keep-alive>
          <ItemComponent
            v-if="currentTab === 'registered'"
            title="등록한 임대 물품"
            :fetchItems="fetchRegisteredItems"
            :tabId="currentTab"
          />
          <ItemComponent
            v-else-if="currentTab === 'renting'"
            title="임대 중인 물품"
            :fetchItems="fetchRentingItems"
            :tabId="currentTab"
          />
          <ItemComponent
            v-else
            title="임차 중인 물품"
            :fetchItems="fetchRentedItems"
            :tabId="currentTab"
          />
        </keep-alive>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref } from 'vue'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ItemComponent from '@/components/trade/ItemComponent.vue'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import { tradeApi } from '@/api/tradeApi'

const currentTab = ref('registered')
const authStore = useAuthStore()
const { userId } = storeToRefs(authStore)

const tabs = [
  { id: 'registered', name: '등록한 임대 물품' },
  { id: 'renting', name: '임대 중인 물품' },
  { id: 'rented', name: '임차 중인 물품' },
]

const fetchRegisteredItems = async () => {
  return await tradeApi.getRegistedItems()
}

const fetchRentingItems = async () => {
  return await tradeApi.getLendItems()
}

const fetchRentedItems = async () => {
  return await tradeApi.getLeaseItems()
}
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  border-bottom: 1px solid #e5e7eb;
  padding-bottom: 1rem;
  justify-content: center; /* 추가: 버튼들을 가운데 정렬 */
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.tab-button {
  padding: 0.75rem 1.5rem;
  border: none;
  background: none;
  font-size: 1rem;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 8px;
}

.tab-button:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.tab-button.active {
  background-color: #ff385c;
  color: white;
}
</style>
