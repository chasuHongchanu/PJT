<template>
  <div class="items-view">
    <header class="header">
      <!-- Filtering button -->
      <button class="filter-btn" @click="filterItems">필터</button>
      <!-- Search area -->
      <div class="search-container">
        <input 
          type="text" 
          class="search-bar" 
          v-model="searchQuery" 
          placeholder="Search items..."
          @input="filterItems"
        />
        <button class="search-btn">
          <i class="fa fa-search"></i>
        </button>
      </div>
    </header>

    <!-- Registering button -->
    <div class="register-container">
      <button class="register-btn" @click="registerItem">글쓰기</button>
    </div>
    
    <!-- Items list -->
    <div class="items-list">
      <ItemDetail
        v-for="item in filteredItems"
        :key="item.id"
        :photo-url="item.photoUrl"
        :name="item.name"
        :price="item.price"
        :address="item.address"
      />
    </div>

    <!-- Map button -->
    <footer class="footer">
      <button class="map-btn" @click="showMap">지도보기</button>
    </footer>
  </div>
</template>

<script>
import ItemDetail from "@/components/items/ItemDetail.vue";

export default {
  name: "TheItemsView",
  components: {
    ItemDetail,
  },
  data() {
    return {
      items: [
        {
          id: 1,
          photoUrl: "https://via.placeholder.com/150",
          name: "스마트폰",
          price: 35000,
          address: "대한민국 서울시 강남구",
        },
        {
          id: 2,
          photoUrl: "https://via.placeholder.com/150",
          name: "Camera",
          price: 50,
          address: "California, USA",
        },
      ],
      searchQuery: "",
      filteredItems: [],
    };
  },
  methods: {
    filterItems() {
      if (!this.searchQuery) {
        this.filteredItems = this.items;
      } else {
        this.filteredItems = this.items.filter(item =>
          item.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
    },
    registerItem() {
      alert("Redirect to item registration page.");
    },
    showMap() {
      alert("Redirect to map page.");
    },
  },
  mounted() {
    this.filterItems();
  },
};
</script>

<style scoped>
/* Header layout */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin-bottom: 20px;
}

/* Filter button on the left */
.filter-btn {
  padding: 8px 16px;
  border-radius: 20px;
  background: #fff;
  color: #333;
  border: 1px solid #333;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

/* Search bar in the center */
.search-container {
  display: flex;
  flex: 1;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 30px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin: 0 10px;
}

.search-bar {
  flex: 1;
  border: none;
  outline: none;
  padding: 8px 12px;
  font-size: 16px;
}

.search-btn {
  background: #ff3b30;
  color: white;
  border: none;
  padding: 8px 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

/* Register button beneath search */
.register-container {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}

.register-btn {
  padding: 8px 16px;
  border-radius: 20px;
  background: #ff3b30;
  color: white;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  border: none;
}

/* Items list centered */
.items-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

/* Footer */
.footer {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.map-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  background-color: #f04e30;
  color: white;
  cursor: pointer;
}

/* Mobile responsiveness */
@media screen and (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-btn {
    margin-bottom: 10px;
    align-self: flex-start;
  }

  .search-container {
    width: 100%;
  }

  .register-container {
    justify-content: center;
  }

  .items-list {
    flex-direction: column;
    align-items: center;
  }
}
</style>
