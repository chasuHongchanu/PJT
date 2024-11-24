import { defineStore } from "pinia";

export const useItemsStore = defineStore("items", {
  state: () => ({
    items: [], // 전체 데이터
    filteredItems: [], // 필터링된 데이터
    searchResults: [], // 검색 결과
    mapCenter: { lat: 37.5665, lng: 126.978 }, // 지도 중심
    isInitialLoad: true, // 초기 로딩 여부를 추적
    lastSearchQuery: "", // 마지막 검색어 저장
    lastFilters: null, // 마지막 필터 설정 저장
  }),

  actions: {
    // 초기 데이터 로드 (최초 1회만 실행)
    async initializeItems() {
      if (this.isInitialLoad && this.items.length === 0) {
        try {
          const response = await fetch("http://localhost:8080/api/item/rent/list");
          const data = await response.json();
          this.items = data.data || [];
          this.filteredItems = [...this.items];
          this.isInitialLoad = false;
        } catch (error) {
          console.error("Error fetching items:", error);
        }
      }
    },

    // 전체 데이터 가져오기 (필요한 경우 강제로 새로고침)
    async fetchItems() {
      try {
        const response = await fetch("http://localhost:8080/api/item/rent/list");
        const data = await response.json();
        this.items = data.data || [];
        // 검색이나 필터가 적용된 상태가 아닐 때만 filteredItems 업데이트
        if (!this.lastSearchQuery && !this.lastFilters) {
          this.filteredItems = [...this.items];
        }
      } catch (error) {
        console.error("Error fetching items:", error);
      }
    },

    // 검색 처리
    async searchItems(keyword) {
      try {
        const response = await fetch(
          `http://localhost:8080/api/item/rent/search?keyword=${keyword}`
        );
        const data = await response.json();
        this.filteredItems = data.data || [];
        this.lastSearchQuery = keyword;
        this.lastFilters = null; // 검색 시 필터 초기화

        if (this.filteredItems.length > 0) {
          this.updateMapCenter(this.filteredItems[0]);
        }
      } catch (error) {
        console.error("Error searching items:", error);
      }
    },

    // 필터 처리
    async filterItems(filters) {
      try {
        const queryParams = new URLSearchParams(filters);

        // 필터 키를 DTO 필드와 일치하도록 매핑
        // category 처리
        if (filters.category) {
          if (filters.category.main) queryParams.append("mainCategory", filters.category.main);
          if (filters.category.sub) queryParams.append("subCategory", filters.category.sub);
          if (filters.category.item) queryParams.append("subSubCategory", filters.category.item);
        }

        // region 처리
        if (filters.region) {
          if (filters.region.main) queryParams.append("country", filters.region.main);
          if (filters.region.city) queryParams.append("province", filters.region.city);
          if (filters.region.area) queryParams.append("district", filters.region.area);
        }

        // price 처리
        if (filters.price) {
          if (filters.price.min !== undefined) queryParams.append("minPrice", filters.price.min);
          if (filters.price.max !== undefined) queryParams.append("maxPrice", filters.price.max);
        }

        const response = await fetch(
          `http://localhost:8080/api/item/rent/search?${queryParams.toString()}`
        );
        const data = await response.json();
        this.filteredItems = data.data || [];
        this.lastFilters = filters;
        this.lastSearchQuery = ""; // 필터 적용 시 검색어 초기화

        if (this.filteredItems.length > 0) {
          this.updateMapCenter(this.filteredItems[0]);
        }
      } catch (error) {
        console.error("Error filtering items:", error);
      }
    },

    // 지도 중심점 업데이트
    updateMapCenter(item) {
      this.mapCenter = {
        lat: parseFloat(item.latitude),
        lng: parseFloat(item.longitude),
      };
    },

    // 마지막 검색/필터 상태 복원
    async restoreLastState() {
      if (this.lastSearchQuery) {
        await this.searchItems(this.lastSearchQuery);
      } else if (this.lastFilters) {
        await this.filterItems(this.lastFilters);
      }
    },

    // 데이터 초기화 (필요한 경우)
    resetState() {
      this.lastSearchQuery = "";
      this.lastFilters = null;
      this.filteredItems = [...this.items];
      this.mapCenter = { lat: 37.5665, lng: 126.978 };
    }
  },
});