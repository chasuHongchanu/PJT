<template>
  <FullWidthLayout>
    <div class="main-container">
      <!-- 여행을 더 가볍고 풍성하게 섹션 -->
      <section class="section bg-white">
        <div class="content-wrapper">
          <div class="text-content text-left">
            <h2 class="section-title">여행을 더 가볍고 풍성하게!</h2>
            <p class="section-desc desktop-text">
              Trend는 여행을 계획하는 누구나 편리하지만<br/>
              필요한 물품을 쉽고 합리적인 가격으로 빌리고,<br/>
              추천 코스를 통해 특별한 여행 경험을 제공하는<br/>
              플랫폼입니다.
            </p>
            <p class="section-desc mobile-text">
              Trend는 여행을 계획하는 누구나 편리하지만 필요한 물품을 쉽고 합리적인 가격으로 빌리고, 추천 코스를 통해 특별한 여행 경험을 제공하는 플랫폼입니다.
            </p>
            <router-link to="/intro" class="more-link">More →</router-link>
          </div>
          <div class="image-wrapper">
            <img src="@/assets/main/intro.svg" alt="Travel stamps" class="section-img animate-float"/>
          </div>
        </div>
      </section>

      <!-- 간편한 물품 대여 서비스 섹션 -->
      <section class="section bg-gray">
        <div class="content-wrapper">
          <div class="text-content text-right">
            <h2 class="section-title">간편한 물품 대여 서비스</h2>
            <p class="section-desc desktop-text">
              간단한 클릭으로 여행에 필요한 물품을 지역<br/>
              사용자에게 대여받아 부담 없이 사용해보세요.<br/>
              임차인은 일회성으로 필요한 물건을 저렴하게<br/>
              빌릴 수 있고, 대여자는 쓰지 않는 물건을 대여해<br/>
              수익을 창출할 수 있습니다.
            </p>
            <p class="section-desc mobile-text">
              간단한 클릭으로 여행에 필요한 물품을 지역 사용자에게 대여받아 부담 없이 사용해보세요. 임차인은 일회성으로 필요한 물건을 저렴하게 빌릴 수 있고, 대여자는 쓰지 않는 물건을 대여해 수익을 창출할 수 있습니다.
            </p>
            <router-link to="/items" class="more-link">More →</router-link>
          </div>
          <div class="image-wrapper">
            <img src="@/assets/main/item.svg" alt="Travel items" class="section-img animate-float-reverse"/>
          </div>
        </div>
      </section>

      <!-- 추천 여행 코스 공유 섹션 -->
      <section class="section bg-white">
        <div class="content-wrapper">
          <div class="text-content text-left">
            <h2 class="section-title">추천 여행 코스 공유</h2>
            <p class="section-desc desktop-text">
              현지인이 직접 추천하는 여행지와 다니고<br/>
              싶었던 코스를 참고해 나만의 여행을 계획하세요.<br/>
              사용자들이 공유하는 맞춤 코스로 새로운<br/>
              여행을 떠나보세요.
            </p>
            <p class="section-desc mobile-text">
              현지인이 직접 추천하는 여행지와 다니고 싶었던 코스를 참고해 나만의 여행을 계획하세요. 사용자들이 공유하는 맞춤 코스로 새로운 여행을 떠나보세요.
            </p>
            <router-link to="/courses" class="more-link">More →</router-link>
          </div>
          <div class="image-wrapper">
            <img src="@/assets/main/course.svg" alt="World landmarks" class="section-img animate-scale"/>
          </div>
        </div>
      </section>

      <!-- 여행 커뮤니티 섹션 -->
      <section class="section bg-gray">
        <div class="content-wrapper">
          <div class="text-content text-right">
            <h2 class="section-title">여행 커뮤니티</h2>
            <p class="section-desc desktop-text">
              여행 후기와 정보를 공유하는 공간으로,<br/>
              알기 어려웠던 현지마다 숨겨진 맛집까지,<br/>
              진짜 여행 정보를 통해 즐거운 여행을<br/>
              경험할 수 있습니다.
            </p>
            <p class="section-desc mobile-text">
              여행 후기와 정보를 공유하는 공간으로, 알기 어려웠던 현지마다 숨겨진 맛집까지, 진짜 여행 정보를 통해 즐거운 여행을 경험할 수 있습니다.
            </p>
            <router-link to="/community" class="more-link">More →</router-link>
          </div>
          <div class="image-wrapper">
            <img src="@/assets/main/community.svg" alt="Community network" class="section-img animate-scale-reverse"/>
          </div>
        </div>
      </section>
    </div>
  </FullWidthLayout>
</template>

<script>
import FullWidthLayout from '@/layouts/FullWidthLayout.vue'

export default {
  name: 'MainView',
  components: {
    FullWidthLayout
  }
}
</script>

<style scoped>
.main-container {
  width: 100%;
}

.section {
  padding: 4rem 0;
  width: 100%;
}

.bg-white {
  background-color: #ffffff;
}

.bg-gray {
  background-color: #f8f9fa;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 50px; /* 32px에서 50px로 증가 */
  position: relative;
}

.text-content {
  width: 66%;
  margin-bottom: 3rem;
}

.text-left {
  margin-left: 0;
  margin-right: auto;
  text-align: left;
}

.text-right {
  margin-left: auto;
  margin-right: 0;
  text-align: right;
}

.image-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.section-title {
  color: #ff5a5f;
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
}

.section-desc {
  color: #4a4a4a;
  font-size: 1.25rem;
  line-height: 1.8;
  margin-bottom: 1rem;
}

.more-link {
  display: inline-block;
  color: #ff5a5f;
  text-decoration: none;
  margin-top: 1.5rem;
  font-size: 1.2rem;
  font-weight: 500;
  transition: transform 0.3s ease;
}

.text-right .more-link:hover {
  transform: translateX(-5px);
}

.text-left .more-link:hover {
  transform: translateX(5px);
}

.section-img {
  width: 80%;
  max-width: 800px;
  height: auto;
}

/* 애니메이션 */
@keyframes float {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
  100% { transform: translateY(0px); }
}

@keyframes float-reverse {
  0% { transform: translateY(0px); }
  50% { transform: translateY(20px); }
  100% { transform: translateY(0px); }
}

@keyframes scale {
  0% { transform: scale(0.9); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes scale-reverse {
  0% { transform: scale(1.1); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}

.animate-float {
  animation: float 6s ease-in-out infinite;
}

.animate-float-reverse {
  animation: float-reverse 6s ease-in-out infinite;
}

.animate-scale {
  animation: scale 1s ease-out forwards;
}

.animate-scale-reverse {
  animation: scale-reverse 1s ease-out forwards;
}

.mobile-text {
  display: none;
  word-break: keep-all; /* 단어 단위로 줄바꿈 */
  word-wrap: break-word; /* 긴 단어는 줄바꿈 */
}

.desktop-text {
  display: block;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .section {
    padding: 3rem 0;
  }

  .content-wrapper {
    padding: 0 1rem;
  }

  .text-content {
    width: 100%;
    margin-bottom: 2rem;
  }

  /* 모바일에서 텍스트 영역을 3/4로 확장 */
  .text-left {
    padding-left: 8%;
    padding-right: 8%; /* 34%에서 25%로 수정하여 텍스트 영역을 확장 */
  }

  .text-right {
    padding-right: 8%;
    padding-left: 8%; /* 34%에서 25%로 수정하여 텍스트 영역을 확장 */
  }

  .section-title {
    font-size: 2rem;
  }

  .section-desc {
    font-size: 1.1rem;
  }

  .section-img {
    width: 90%;
  }
}
</style>