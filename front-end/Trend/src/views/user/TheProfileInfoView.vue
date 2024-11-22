<!-- src/views/user/TheProfileInfoView.vue -->
<template>
  <DefaultLayout>
    <div class="profile-container">
      <ProfileCard :profileData="profileData" />
      <div class="divider"></div>
      <ReviewCarousel :reviews="reviews" />
    </div>
  </DefaultLayout>
</template>

<script>
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ProfileCard from "@/components/user/ProfileCard.vue";
import ReviewCarousel from "@/components/user/ReviewCarousel.vue";

export default {
  name: "TheProfileInfoView",
  components: {
    DefaultLayout,
    ProfileCard,
    ReviewCarousel,
  },
  data() {
    return {
      profileData: {
        name: "홍길동",
        email: "email@email.com",
        location: "대한민국 서울시",
        imageUrl: "/profile-image.jpg",
        introduction: "안녕하세요. 소개글입니다.",
      },
      reviews: [
        {
          id: 1,
          content: "안녕하세요. 거래 좋았습니다. 깔끔하고 좋았습니다!",
          reviewerName: "이름",
          reviewerImage: "/reviewer-image.jpg",
          date: "2024.10 31일",
        },
      ],
    };
  },
};
</script>

<style scoped>
.profile-container {
  width: 100%;
  padding: 20px;
  background-color: var(--color-white);
}

.profile-page {
  width: 90%; /* 양쪽에 5%씩 여백 */
  max-width: 600px; /* 최대 너비 설정 */
  margin: 0 auto;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #eee;
  margin: 24px 0;
}

/* 태블릿 이상 디바이스에서의 여백 조정 */
@media (min-width: 768px) {
  .profile-container {
    padding: 24px;
  }

  .profile-page {
    width: 85%; /* 더 큰 화면에서는 여백을 더 늘림 */
  }
}

/* 작은 모바일 화면에서의 여백 조정 */
@media (max-width: 375px) {
  .profile-container {
    padding: 16px;
  }

  .profile-page {
    width: 95%; /* 매우 작은 화면에서는 여백을 줄임 */
  }
}
</style>
