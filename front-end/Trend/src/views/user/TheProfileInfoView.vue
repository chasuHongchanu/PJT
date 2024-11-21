<!-- src/views/user/TheProfileInfoView.vue -->
<template>
  <div class="profile-page">
    <ProfileCard :profileData="profileData" />
    <div class="divider"></div>
    <ReviewCarousel :reviews="reviews" />
  </div>
</template>

<script>
import ProfileCard from '@/components/user/ProfileCard.vue';
import ReviewCarousel from '@/components/user/ReviewCarousel.vue';

export default {
  name: 'TheProfileInfoView',
  components: {
    ProfileCard,
    ReviewCarousel
  },
  data() {
    return {
      profileData: {
        name: '홍길동',
        email: 'email@email.com',
        location: '대한민국 서울시',
        imageUrl: '/profile-image.jpg',
        introduction: '안녕하세요. 소개글입니다.'
      },
      reviews: [
        {
          id: 1,
          content: '안녕하세요. 거래 좋았습니다. 깔끔하고 좋았습니다!',
          reviewerName: '이름',
          reviewerImage: '/reviewer-image.jpg',
          date: '2024.10 31일'
        },
        // 더미 데이터 추가...
      ]
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.divider {
  height: 1px;
  background-color: #eee;
  margin: 24px 0;
}
</style>