<!-- src/components/user/ProfileCard.vue -->
<template>
    <div class="profile-card">
      <div class="profile-info">
        <div class="profile-image-container">
          <img :src="profileData.imageUrl" alt="Profile" class="profile-image" />
        </div>
        <div class="user-info">
          <h2 class="username">{{ profileData.name }}</h2>
          <p class="user-email">{{ profileData.email }}</p>
          <p class="user-location">{{ profileData.location }}</p>
        </div>
      </div>
      <div class="introduction-section">
        <h3>소개</h3>
        <p class="introduction-text">{{ profileData.introduction }}</p>
        <button @click="goToEdit" class="edit-button">수정</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ProfileCard',
    props: {
      profileData: {
        type: Object,
        required: true
      }
    },
    methods: {
      goToEdit() {
        this.$router.push('/user/profile/edit');
      }
    }
  }
  </script>
  
  <style scoped>
  .profile-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .profile-info {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .profile-image-container {
    margin-right: 20px;
  }
  
  .profile-image {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .user-info {
    flex: 1;
  }
  
  .username {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 4px;
  }
  
  .user-email, .user-location {
    color: #666;
    font-size: 14px;
    margin: 2px 0;
  }
  
  .introduction-section {
    border-top: 1px solid #eee;
    padding-top: 16px;
  }
  
  .introduction-section h3 {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 8px;
  }
  
  .introduction-text {
    color: #333;
    margin-bottom: 12px;
  }
  
  .edit-button {
    padding: 6px 16px;
    background: #f0f0f0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
  }
  
  .edit-button:hover {
    background: #e0e0e0;
  }
  </style>