<template>
  <DefaultLayout>
    <div class="course-list-container">
      <!-- 페이지 제목 -->
      <h1 class="page-title">코스 전체 조회</h1>
      <h2 class="sub-title">여행 코스</h2>

      <!-- 검색바 -->
      <div class="search-bar">
        <input v-model="searchQuery" type="text" class="search-input" placeholder="여행 코스" />
        <button class="search-button">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
        </button>
      </div>

      <!-- 필터/글쓰기 버튼 -->
      <div class="button-container">
        <button class="filter-button" @click="toggleFilter">필터</button>
        <button class="write-button" @click="goToWrite">글쓰기</button>
      </div>

      <!-- 게시물 목록 부분만 수정 -->
      <div class="post-list" v-infinite-scroll="loadMore">
        <div v-for="post in posts" :key="post.id" class="post-item">
          <div class="post-content-wrapper">
            <!-- 유저 정보 -->
            <div class="user-info">
              <img :src="post.userProfile" alt="Profile" class="profile-image" />
              <span class="nickname">{{ post.nickname }}</span>
            </div>

            <!-- 게시글 컨텐츠 영역 -->
            <div class="content-layout">
              <div class="text-content">
                <h3 class="post-title">{{ post.title }}</h3>
                <p class="post-content">{{ post.content }}</p>
              </div>

              <!-- 썸네일 이미지 -->
              <img :src="post.thumbnail" alt="Thumbnail" class="thumbnail" />
            </div>

            <!-- 좋아요/댓글 카운트 -->
            <div class="interaction-counts">
              <div class="count-item">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path
                    d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"
                  ></path>
                </svg>
                <span>{{ post.likes }}</span>
              </div>
              <div class="count-item">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path
                    d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"
                  ></path>
                </svg>
                <span>{{ post.comments }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 필터 팝업 -->
      <FilterPopup 
        :is-visible="isFilterVisible"
        @close="closeFilter"
        @apply="applyFilter"
      />
    </div>
  </DefaultLayout>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import DefaultImage from '@/assets/default-image.svg'
import DefaultProfile from '@/assets/default-profile.svg'
import FilterPopup from '@/components/course/FilterPopup.vue'

export default {
  name: 'CourseListView',
  components: {
    DefaultLayout,
    FilterPopup
  },

  setup() {
    const router = useRouter()
    const searchQuery = ref('')
    const posts = ref([])
    const isFilterVisible = ref(false)

    // 더미 데이터 생성
    const generateDummyPosts = () => {
      return Array.from({ length: 5 }, (_, index) => ({
        id: index + 1,
        userProfile: DefaultProfile,
        nickname: `여행러${index + 1}`,
        title: `여행 코스 타이틀 ${index + 1}`,
        content:
          '본문 내용이 여기에 들어갑니다. 본문 내용이 여기에 들어갑니다. 본문 내용이 여기에 들어갑니다. 본문 내용이 여기에 들어갑니다.',
        thumbnail: DefaultImage,
        likes: 256,
        comments: 256,
      }))
    }

    // 초기 데이터 로드
    onMounted(() => {
      posts.value = generateDummyPosts()
    })

    // 무한 스크롤
    const loadMore = () => {
      const newPosts = generateDummyPosts()
      posts.value.push(...newPosts)
    }

    // 필터 관련 메서드
    const toggleFilter = () => {
      isFilterVisible.value = !isFilterVisible.value
    }

    const closeFilter = () => {
      isFilterVisible.value = false
    }

    const applyFilter = (filterData) => {
      console.log('Applied filters:', filterData)
      // TODO: 필터 적용 로직 구현
      // 예: API 호출하여 필터링된 데이터 가져오기
      closeFilter()
    }

    // 글쓰기 페이지 이동
    const goToWrite = () => {
      router.push('/course/write')
    }

    return {
      searchQuery,
      posts,
      loadMore,
      isFilterVisible,
      toggleFilter,
      closeFilter,
      applyFilter,
      goToWrite
    }
  },
}
</script>

<style scoped>
.course-list-container {
  padding: 16px;
  background-color: #ffffff;
  min-height: 100vh;
}

.page-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
}

.sub-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 16px;
}

.search-bar {
  position: relative;
  margin-bottom: 16px;
}

.search-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
}

.search-button {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #ff5a5a;
  background: none;
  border: none;
  cursor: pointer;
}

.button-container {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 20px;
}

.filter-button {
  padding: 6px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background: white;
  font-size: 14px;
}

.write-button {
  padding: 6px 16px;
  background: #ff5a5a;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.post-item {
  background: white;
  padding: 16px;
  border-radius: 8px;
}

.post-content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.content-layout {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.text-content {
  flex: 1;
  min-width: 0; /* 텍스트 오버플로우 방지 */
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.profile-image {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  font-size: 14px;
  color: #333;
}

.post-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.post-content {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.thumbnail {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0; /* 이미지 크기 고정 */
}

.interaction-counts {
  display: flex;
  gap: 16px;
}

.count-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.count-item svg {
  color: #666;
}

/* 반응형 스타일 */
@media (max-width: 640px) {
  .content-layout {
    gap: 12px;
  }

  .thumbnail {
    width: 100px;
    height: 100px;
  }
}
</style>
