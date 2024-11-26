<template>
  <DefaultLayout>
    <div class="course-detail-container">
      <!-- 로딩 상태 표시 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <p>코스 정보를 불러오는 중입니다...</p>
      </div>
      <!-- 에러 상태 표시 -->
      <div v-else-if="error" class="error-state">
        <p>{{ error }}</p>
        <button @click="fetchCourseData" class="retry-button">다시 시도</button>
      </div>
      <!-- 데이터 로드 완료 시 컴포넌트 표시 -->
      <template v-else-if="courseData">
        <CourseInfo 
          :course-data="processedCourseData"
          @like-clicked="handleLike"
        />
      </template>
    </div>
  </DefaultLayout>
</template>

<script>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import CourseInfo from "@/components/course/CourseInfo.vue";
import { courseApi } from "@/api/courseApi";
import { firebaseUtils } from "@/utils/firebaseUtils";

export default {
  name: "CourseDetailView",

  components: {
    DefaultLayout,
    CourseInfo,
  },

  setup() {
    const route = useRoute();
    const router = useRouter();
    const courseId = ref(route.params.id);
    const courseData = ref(null);
    const isLoading = ref(true);
    const error = ref(null);

    // 코스 데이터 처리
    const processedCourseData = computed(() => {
      if (!courseData.value) return null;

      return {
        ...courseData.value,
        // spots 배열을 visitOrder 기준으로 정렬
        spots: courseData.value.spots?.sort((a, b) => a.visitOrder - b.visitOrder) || []
      };
    });

    // 코스 데이터 가져오기
    const fetchCourseData = async () => {
      try {
        isLoading.value = true;
        error.value = null;

        // API 호출
        const response = await courseApi.getCourseDetail(courseId.value);
        
        if (!response.data) {
          throw new Error("코스 정보를 찾을 수 없습니다.");
        }

        // 프로필 이미지 URL 가져오기
        const profileImgUrl = await firebaseUtils.getProfileImageUrl(response.data.writerProfileImg);
        
        // 데이터 설정
        courseData.value = {
          ...response.data,
          writerProfileImg: profileImgUrl,
        };

      } catch (err) {
        console.error("Failed to fetch course data:", err);
        error.value = "코스 정보를 불러오는데 실패했습니다.";
        setTimeout(() => router.push("/course/list"), 3000);
      } finally {
        isLoading.value = false;
      }
    };

    // 좋아요 처리
    const handleLike = async () => {
      try {
        // 좋아요 API 호출 (추후 구현)
        console.log("Like clicked for course:", courseId.value);
        // 좋아요 상태 업데이트
        courseData.value = {
          ...courseData.value,
          likeCount: courseData.value.likeCount + 1
        };
      } catch (error) {
        console.error("Failed to handle like:", error);
      }
    };

    onMounted(() => {
      fetchCourseData();
    });

    return {
      courseId,
      courseData,
      processedCourseData,
      isLoading,
      error,
      handleLike,
    };
  },
};
</script>

<style scoped>
.course-detail-container {
  max-width: 768px;
  margin: 0 auto;
  padding: 16px;
}

.loading-state {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  margin: 20px auto;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #ff5a5a;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.error-state {
  text-align: center;
  padding: 2rem;
  color: #ff5a5a;
}

.retry-button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #ff5a5a;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>