<template>
  <DefaultLayout>
    <div class="course-detail-container">
      <!-- 로딩 상태 표시 -->
      <div v-if="isLoading" class="loading-state">Loading...</div>
      <!-- 데이터 로드 완료 시 컴포넌트 표시 -->
      <template v-else-if="courseData">
        <CourseInfo 
          :course-data="courseData"
          @like-clicked="handleLike"
        />
        
        <Comments 
          :course-id="courseId"
          :comment-count="courseData.commentCount"
        />
      </template>
    </div>
  </DefaultLayout>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import CourseInfo from "@/components/course/CourseInfo.vue";
import Comments from "@/components/course/Comments.vue";

export default {
  name: "CourseDetailView",

  components: {
    DefaultLayout,
    CourseInfo,
    Comments,
  },

  setup() {
    const route = useRoute();
    const router = useRouter();
    const courseId = ref(route.params.id); // route.params.courseId를 route.params.id로 수정
    const courseData = ref(null);
    const isLoading = ref(true);

    const fetchCourseData = async () => {
      try {
        isLoading.value = true;
        const response = await axios.get(`/api/course/detail/${courseId.value}`);
        courseData.value = response.data;

        // 데이터 유효성 검사
        if (!courseData.value) {
          throw new Error("Course not found");
        }
      } catch (error) {
        console.error("Failed to fetch course data:", error);
        // 에러 발생 시 목록 페이지로 리다이렉트
        router.push("/course/list");
      } finally {
        isLoading.value = false;
      }
    };
    const handleLike = async () => {
      // 좋아요 처리 로직은 추후 구현
      console.log("Like clicked");
    };

    onMounted(() => {
      fetchCourseData();
    });

    return {
      courseId,
      courseData,
      isLoading,
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
</style>
