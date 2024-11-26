<template>
  <DefaultLayout>
    <div class="course-list-container">
      <!-- 기존 템플릿 코드는 유지 -->
      <h1 class="page-title">코스 전체 조회</h1>
      <h2 class="sub-title">여행 코스</h2>

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

      <div class="button-container">
        <button class="filter-button" @click="toggleFilter">필터</button>
        <button class="write-button" @click="goToWrite">글쓰기</button>
      </div>

      <!-- 게시물 목록 -->
      <div 
        class="course-list" 
        v-infinite-scroll="loadMore"
        :infinite-scroll-disabled="loading || !hasNextPage"
        :infinite-scroll-distance="10"
      >
        <div 
          v-for="course in courses" 
          :key="course.courseId" 
          class="course-item"
          @click="goToDetail(course.courseId)"
        >
          <div class="course-content-wrapper">
            <div class="user-info">
              <img :src="course.userProfileImg || DefaultProfile" alt="Profile" class="profile-image" />
              <span class="nickname">{{ course.userNickname }}</span>
            </div>

            <div class="content-layout">
              <div class="text-content">
                <h3 class="course-title">{{ course.courseTitle }}</h3>
                <p class="course-content">{{ course.courseContent }}</p>
              </div>

              <img :src="DefaultMap" alt="Course Map" class="thumbnail" />
            </div>

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
                  <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                </svg>
                <span>{{ course.likeCount }}</span>
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
                  <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path>
                </svg>
                <span>{{ course.commentCount }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 로딩 표시 -->
        <div v-if="loading" class="loading">
          Loading...
        </div>
      </div>

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
import DefaultMap from '@/assets/default-map.svg'
import DefaultProfile from '@/assets/default-profile.svg'
import FilterPopup from '@/components/course/FilterPopup.vue'
import { courseApi } from '@/api/courseApi'
import { firebaseUtils } from '@/utils/firebaseUtils'

export default {
  name: 'CourseListView',
  components: {
    DefaultLayout,
    FilterPopup
  },

  setup() {
    const router = useRouter()
    const searchQuery = ref('')
    const courses = ref([])
    const isFilterVisible = ref(false)
    const currentPage = ref(1)
    const loading = ref(false)
    const hasNextPage = ref(true)
    const pageSize = 10

    // 이미지 URL 로드 (프로필 이미지만 처리)
    const loadImageUrls = async (courseData) => {
      const updatedCourses = await Promise.all(
        courseData.map(async (course) => {
          // 프로필 이미지만 Firebase에서 로드
          const profileUrl = await firebaseUtils.getProfileImageUrl(course.userProfileImg);
          
          return {
            ...course,
            userProfileImg: profileUrl
          };
        })
      );
      return updatedCourses;
    };

    // 코스 목록 조회
    const fetchCourses = async () => {
      if (loading.value || !hasNextPage.value) return

      try {
        loading.value = true
        const response = await courseApi.getCourseList(currentPage.value)
        
        if (response.data && response.data.data) {
          // Firebase에서 이미지 URL 로드
          const updatedCourseData = await loadImageUrls(response.data.data);
          
          // 첫 페이지가 아닌 경우 기존 데이터에 추가
          if (currentPage.value === 1) {
            courses.value = updatedCourseData;
          } else {
            courses.value = [...courses.value, ...updatedCourseData];
          }

          // 다음 페이지 존재 여부 확인
          hasNextPage.value = response.data.currentPage < response.data.totalPages
          currentPage.value++
        }
      } catch (error) {
        console.error('Failed to fetch courses:', error)
      } finally {
        loading.value = false
      }
    }

    // 초기 데이터 로드
    onMounted(() => {
      fetchCourses()
    })

    // 무한 스크롤
    const loadMore = () => {
      if (!loading.value && hasNextPage.value) {
        fetchCourses()
      }
    }

    // 필터 관련 메서드
    const toggleFilter = () => {
      isFilterVisible.value = !isFilterVisible.value
    }

    const closeFilter = () => {
      isFilterVisible.value = false
    }

    // 필터 적용
    const applyFilter = async (filterData) => {
      try {
        loading.value = true
        currentPage.value = 1
        
        const response = await courseApi.searchCourses({
          ...filterData,
          page: currentPage.value,
          size: pageSize
        })
        
        if (response.data && response.data.data) {
          // Firebase에서 이미지 URL 로드
          const updatedCourseData = await loadImageUrls(response.data.data);
          courses.value = updatedCourseData;
          hasNextPage.value = response.data.currentPage < response.data.totalPages
          currentPage.value++
        }
      } catch (error) {
        console.error('Failed to apply filters:', error)
      } finally {
        loading.value = false
        closeFilter()
      }
    }

    // 글쓰기 페이지 이동
    const goToWrite = () => {
      router.push({ name: 'CourseRegist' })
    }

    // 상세 페이지 이동
    const goToDetail = (courseId) => {
      router.push(`/course/detail/${courseId}`)
    }

    return {
      searchQuery,
      courses,
      loadMore,
      loading,
      hasNextPage,
      isFilterVisible,
      toggleFilter,
      closeFilter,
      applyFilter,
      goToWrite,
      goToDetail,
      DefaultMap,
      DefaultProfile
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
  font-size: 16px;  /* 14px -> 16px */
  color: #333;
  margin-bottom: 8px;
}

.sub-title {
  font-size: 24px;  /* 20px -> 24px */
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

.course-list {
  display: flex;
  flex-direction: column;
  gap: 16px;  /* 12px -> 16px */
}

.course-item {
  background: white;
  padding: 20px;  /* 16px -> 20px */
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);  /* 그림자 효과 추가 */
}

.course-content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;  /* 12px -> 16px */
}

.course-item:hover {
  transform: translateY(-2px);
}

.content-layout {
  display: flex;
  gap: 20px;  /* 16px -> 20px */
  align-items: flex-start;
}

.text-content {
  flex: 1;
  min-width: 0;
}


.user-info {
  display: flex;
  align-items: center;
  gap: 12px;  /* 8px -> 12px */
  margin-bottom: 16px;  /* 12px -> 16px */
}

.profile-image {
  width: 40px;  /* 24px -> 40px */
  height: 40px;  /* 24px -> 40px */
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  font-size: 16px;  /* 14px -> 16px */
  color: #333;
  font-weight: 500;  /* 글씨 약간 더 진하게 */
}

.course-title {
  font-size: 20px;  /* 16px -> 20px */
  font-weight: bold;
  margin-bottom: 12px;  /* 8px -> 12px */
  line-height: 1.4;
}

.course-content {
  font-size: 16px;  /* 14px -> 16px */
  color: #666;
  line-height: 1.5;  /* 1.4 -> 1.5 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 16px;  /* 하단 여백 추가 */
}

.thumbnail {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 7px;
  flex-shrink: 0; /* 이미지 크기 고정 */
}

.interaction-counts {
  display: flex;
  gap: 24px;  /* 16px -> 24px */
  margin-top: 8px;  /* 상단 여백 추가 */
}

.count-item {
  display: flex;
  align-items: center;
  gap: 6px;  /* 4px -> 6px */
  color: #666;
  font-size: 16px;  /* 14px -> 16px */
}

.count-item svg {
  width: 20px;  /* 16px -> 20px */
  height: 20px;  /* 16px -> 20px */
  color: #666;
}

/* 반응형 스타일 */
@media (max-width: 640px) {
  .content-layout {
    gap: 16px;
  }

  .thumbnail {
    width: 100px;
    height: 100px;
  }
  
  .course-title {
    font-size: 18px;  /* 모바일에서는 약간 작게 */
  }
  
  .course-content {
    font-size: 15px;  /* 모바일에서는 약간 작게 */
  }
}

/* 로딩 표시 스타일 */
.loading {
  text-align: center;
  padding: 20px;
  color: #666;
  font-size: 16px;  /* 로딩 텍스트도 크게 */
}
</style>
