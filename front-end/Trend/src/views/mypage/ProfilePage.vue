<template>
    <div class="profile-page">
      <!-- Header -->
      <header class="header">
        <div class="logo" @click="goToHome">Service</div>
        <button class="menu-btn" @click="toggleSidebar">☰</button>
      </header>
  
      <!-- Profile Card -->
      <section class="profile-section">
        <div class="profile-card">
          <img class="profile-image" :src="user.profilePicture" alt="Profile Picture" />
          <div class="profile-details">
            <h2>{{ user.name }}</h2>
            <p>이메일: {{ user.email }}</p>
            <p>지역: {{ user.region }}</p>
          </div>
        </div>
      </section>
  
      <!-- User Introduction -->
      <section class="introduction-section">
        <h3>소개</h3>
        <p>{{ user.introduction }}</p>
        <button class="edit-btn" @click="goToEditProfile">수정</button>
      </section>
  
      <!-- User Reviews -->
      <section class="reviews-section">
        <h3>나에 대한 거래 후기</h3>
        <div class="review-card">
          <img class="reviewer-image" :src="currentReview.reviewerPicture" alt="Reviewer Image" />
          <div class="review-content">
            <p>{{ currentReview.content }}</p>
            <p class="review-meta">
              {{ currentReview.reviewerName }} | {{ currentReview.date }}
            </p>
          </div>
          <div class="review-navigation">
            <button @click="previousReview">⬅</button>
            <button @click="nextReview">➡</button>
          </div>
        </div>
        <button class="all-reviews-btn" @click="goToAllReviews">모든 후기 보기</button>
      </section>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        user: {
          profilePicture: "",
          name: "",
          email: "",
          region: "",
          introduction: "",
        },
        reviews: [],
        currentReviewIndex: 0,
      };
    },
    computed: {
      currentReview() {
        return this.reviews[this.currentReviewIndex] || {};
      },
    },
    methods: {
      async fetchUserData() {
        // Replace with actual API call
        const response = await fetch("/api/user");
        const data = await response.json();
        this.user = data;
      },
      async fetchReviews() {
        // Replace with actual API call
        const response = await fetch("/api/reviews");
        const data = await response.json();
        this.reviews = data;
      },
      goToHome() {
        this.$router.push("/");
      },
      toggleSidebar() {
        alert("메뉴 버튼 클릭됨"); // 사이드바 여는 로직 추가
      },
      goToEditProfile() {
        this.$router.push("/edit-profile");
      },
      goToAllReviews() {
        this.$router.push("/reviews");
      },
      nextReview() {
        if (this.currentReviewIndex < this.reviews.length - 1) {
          this.currentReviewIndex++;
        }
      },
      previousReview() {
        if (this.currentReviewIndex > 0) {
          this.currentReviewIndex--;
        }
      },
    },
    mounted() {
      this.fetchUserData();
      this.fetchReviews();
    },
  };
  </script>
  
  <style scoped>
  .profile-page {
    font-family: Arial, sans-serif;
    padding: 20px;
    max-width: 600px;
    margin: 0 auto;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #ddd;
  }
  
  .logo {
    font-size: 24px;
    font-weight: bold;
    color: #ff5a5a;
    cursor: pointer;
  }
  
  .menu-btn {
    font-size: 24px;
    background: none;
    border: none;
    cursor: pointer;
  }
  
  .profile-section {
    margin-top: 20px;
  }
  
  .profile-card {
    display: flex;
    align-items: center;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
  }
  
  .profile-image {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    border: 2px solid #ddd;
    margin-right: 20px;
  }
  
  .profile-details h2 {
    margin: 0;
    font-size: 20px;
  }
  
  .introduction-section {
    margin-top: 20px;
  }
  
  .edit-btn {
    margin-top: 10px;
    padding: 5px 10px;
    background-color: #ff5a5a;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .reviews-section {
    margin-top: 20px;
  }
  
  .review-card {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
  }
  
  .reviewer-image {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
  }
  
  .review-content {
    flex: 1;
  }
  
  .review-meta {
    font-size: 12px;
    color: #777;
  }
  
  .review-navigation button {
    background: none;
    border: none;
    font-size: 18px;
    cursor: pointer;
  }
  
  .all-reviews-btn {
    margin-top: 10px;
    padding: 5px 10px;
    background-color: #ff5a5a;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    display: block;
    width: 100%;
    text-align: center;
  }
  </style>