<!-- src/components/user/ReviewCarousel.vue -->
<template>
    <div class="review-section">
      <div class="section-header">
        <h3>나에 대한 거래 후기</h3>
        <div class="navigation-buttons">
          <button 
            @click="showPreviousReview" 
            :disabled="currentIndex === 0"
            class="nav-button"
          >
            ←
          </button>
          <button 
            @click="showNextReview" 
            :disabled="currentIndex === reviews.length - 1"
            class="nav-button"
          >
            →
          </button>
        </div>
      </div>
  
      <div v-if="currentReview" class="review-card">
        <div class="review-content">
          <p>{{ currentReview.content }}</p>
          <div class="reviewer-info">
            <img :src="currentReview.reviewerImage" alt="Reviewer" class="reviewer-image" />
            <span class="reviewer-name">{{ currentReview.reviewerName }}</span>
            <span class="review-date">{{ currentReview.date }}</span>
          </div>
        </div>
      </div>
  
      <button @click="viewAllReviews" class="view-all-button">
        모든 후기 보기
      </button>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ReviewCarousel',
    props: {
      reviews: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        currentIndex: 0
      }
    },
    computed: {
      currentReview() {
        return this.reviews[this.currentIndex];
      }
    },
    methods: {
      showPreviousReview() {
        if (this.currentIndex > 0) {
          this.currentIndex--;
        }
      },
      showNextReview() {
        if (this.currentIndex < this.reviews.length - 1) {
          this.currentIndex++;
        }
      },
      viewAllReviews() {
        this.$router.push('/user/reviews');
      }
    }
  }
  </script>
  
  <style scoped>
  .review-section {
    margin-top: 20px;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  
  .section-header h3 {
    font-size: 16px;
    font-weight: bold;
  }
  
  .navigation-buttons {
    display: flex;
    gap: 8px;
  }
  
  .nav-button {
    width: 32px;
    height: 32px;
    border: 1px solid #ddd;
    border-radius: 50%;
    background: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .nav-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .review-card {
    background: white;
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;
  }
  
  .reviewer-info {
    display: flex;
    align-items: center;
    margin-top: 12px;
  }
  
  .reviewer-image {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    margin-right: 8px;
  }
  
  .reviewer-name {
    font-weight: 500;
    margin-right: 8px;
  }
  
  .review-date {
    color: #666;
    font-size: 12px;
  }
  
  .view-all-button {
    width: 100%;
    padding: 12px;
    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 500;
  }
  
  .view-all-button:hover {
    background: #f9f9f9;
  }
  </style>