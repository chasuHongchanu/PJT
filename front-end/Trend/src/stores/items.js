// store/items.js
import { defineStore } from 'pinia'

export const useItemsStore = defineStore('items', {
  state: () => ({
    items: [], // 전체 아이템 리스트
    filteredItems: [], // 필터링된 아이템 리스트
    searchResults: [], // 검색 결과 리스트 (MapView에서 사용)
    mapCenter: { lat: 37.5665, lng: 126.978 }, // 지도 중심 좌표
  }),
  actions: {
    // 전체 아이템 가져오기
    async fetchItems() {
      try {
        const response = await fetch('http://localhost:8080/api/item/rent/list')
        const data = await response.json()
        this.items = data
        this.filteredItems = [...data.data] // 초기 필터링
      } catch (error) {
        console.error('Error fetching items:', error)
      }
    },
    // 검색 처리
    async searchItems(query) {
      try {
        const { latitude = 0, longitude = 0, keyword = '', maxPrice = 0, minPrice = 0 } = query

        const response = await fetch(
          `https://localhost:8080/api/goods/rent/search?latitude=${latitude}&longitude=${longitude}&keyword=${keyword}&maxPrice=${maxPrice}&minprice=${minPrice}`,
        )
        const data = await response.json()
        this.searchResults = data.results || []
        if (this.searchResults.length > 0) {
          this.mapCenter = {
            lat: this.searchResults[0].latitude,
            lng: this.searchResults[0].longitude,
          }
        }
      } catch (error) {
        console.error('Error searching items:', error)
      }
    },
    // 검색 결과를 목록에 반영
    applySearchResults() {
      this.filteredItems = [...this.searchResults]
    },
  },
})
