<template>
  <div class="comments-section">
    <!-- 댓글 작성 영역 -->
    <div class="comment-write">
      <button v-if="!isWriting" class="write-button" @click="isWriting = true">댓글 달기</button>
      <div v-else class="write-form">
        <textarea
          v-model="newComment"
          placeholder="댓글을 입력하세요"
          class="comment-input"
        ></textarea>
        <div class="button-group">
          <button class="cancel-button" @click="cancelWrite">취소</button>
          <button class="submit-button" @click="submitComment">등록</button>
        </div>
      </div>
    </div>

    <!-- 댓글 수 -->
    <h3 class="comments-count">댓글 {{ totalComments }}개</h3>

    <!-- 댓글 목록 -->
    <div class="comments-list">
      <div v-for="comment in comments" :key="comment.commentId" class="comment-item">
        <!-- 댓글 내용 -->
        <div class="comment-content">
          <div class="writer-info">
            <img :src="comment.writerProfileImg" alt="Profile" class="profile-image" />
            <span class="nickname">{{ comment.writerNickname }}</span>
            <span class="date">{{ formatDate(comment.createdAt) }}</span>
          </div>
          <p class="text">{{ comment.content }}</p>
          <div class="actions">
            <button class="reply-button" @click="toggleReplyForm(comment.commentId)">
              답글 달기
            </button>
            <div class="like-action">
              <button class="like-button" @click="toggleLike(comment.commentId)">
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
              </button>
              <span>{{ comment.likesCount }}</span>
            </div>
          </div>
        </div>

        <!-- 답글 작성 폼 -->
        <div v-if="activeReplyId === comment.commentId" class="reply-form">
          <textarea
            v-model="newReply"
            placeholder="답글을 입력하세요"
            class="reply-input"
          ></textarea>
          <div class="button-group">
            <button class="cancel-button" @click="cancelReply">취소</button>
            <button class="submit-button" @click="submitReply(comment.commentId)">등록</button>
          </div>
        </div>

        <!-- 답글 목록 -->
        <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
          <div v-for="reply in comment.replies" :key="reply.commentId" class="reply-item">
            <div class="writer-info">
              <img :src="reply.writerProfileImg" alt="Profile" class="profile-image" />
              <span class="nickname">{{ reply.writerNickname }}</span>
              <span class="date">{{ formatDate(reply.createdAt) }}</span>
            </div>
            <p class="text">{{ reply.content }}</p>
          </div>
          <button
            v-if="hasMoreReplies(comment.commentId)"
            class="more-button"
            @click="loadMoreReplies(comment.commentId)"
          >
            답글 더보기
          </button>
        </div>
      </div>

      <!-- 더보기 버튼 -->
      <button v-if="hasMoreComments" class="more-button" @click="loadMoreComments">
        댓글 더보기
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  name: 'Comments',

  props: {
    courseId: {
      type: [String, Number],
      required: true,
    },
    commentCount: {
      type: Number,
      default: 0,
    },
  },

  setup(props) {
    const comments = ref([])
    const totalComments = ref(props.commentCount)
    const currentPage = ref(1)
    const isWriting = ref(false)
    const newComment = ref('')
    const newReply = ref('')
    const activeReplyId = ref(null)

    const fetchComments = async (page = 1) => {
      try {
        const response = await fetch(`/api/course/${props.courseId}/comment?page=${page}&size=5`)
        const data = await response.json()
        if (page === 1) {
          comments.value = data.data
        } else {
          comments.value.push(...data.data)
        }
        currentPage.value = data.currentPage
        totalComments.value = data.totalItems
      } catch (error) {
        console.error('Failed to fetch comments:', error)
      }
    }

    const fetchReplies = async (commentId, page = 1) => {
      try {
        const response = await fetch(
          `/api/course/${props.courseId}/comment/${commentId}?page=${page}&size=5`,
        )
        const data = await response.json()

        // 해당 댓글 찾기
        const comment = comments.value.find((c) => c.commentId === commentId)
        if (comment) {
          if (!comment.replies) comment.replies = []
          if (page === 1) {
            comment.replies = data.data
          } else {
            comment.replies.push(...data.data)
          }
          comment.totalReplies = data.totalItems
          comment.currentReplyPage = data.currentPage
        }
      } catch (error) {
        console.error('Failed to fetch replies:', error)
      }
    }

    const submitComment = async () => {
      if (!newComment.value.trim()) return
      try {
        // API 호출로 댓글 등록 (실제 구현 필요)
        await fetch(`/api/course/${props.courseId}/comment`, {
          method: 'POST',
          body: JSON.stringify({ content: newComment.value }),
        })

        // 댓글 목록 새로고침
        newComment.value = ''
        isWriting.value = false
        currentPage.value = 1
        await fetchComments(1)
      } catch (error) {
        console.error('Failed to submit comment:', error)
      }
    }

    const submitReply = async (commentId) => {
      if (!newReply.value.trim()) return
      try {
        // API 호출로 답글 등록 (실제 구현 필요)
        await fetch(`/api/course/${props.courseId}/comment`, {
          method: 'POST',
          body: JSON.stringify({
            content: newReply.value,
            parentsCommentId: commentId,
          }),
        })

        // 답글 목록 새로고침
        newReply.value = ''
        activeReplyId.value = null
        await fetchReplies(commentId, 1)
      } catch (error) {
        console.error('Failed to submit reply:', error)
      }
    }

    const toggleLike = async (commentId) => {
      try {
        // API 호출로 좋아요 토글 (실제 구현 필요)
        await fetch(`/api/course/comment/${commentId}/like`, {
          method: 'POST',
        })

        // 좋아요 상태 업데이트
        const comment = comments.value.find((c) => c.commentId === commentId)
        if (comment) {
          comment.likesCount = (comment.likesCount || 0) + 1
        }
      } catch (error) {
        console.error('Failed to toggle like:', error)
      }
    }

    const loadMoreComments = () => {
      fetchComments(currentPage.value + 1)
    }

    const loadMoreReplies = (commentId) => {
      const comment = comments.value.find((c) => c.commentId === commentId)
      if (comment) {
        fetchReplies(commentId, (comment.currentReplyPage || 1) + 1)
      }
    }

    const hasMoreComments = computed(() => {
      return comments.value.length < totalComments.value
    })

    const hasMoreReplies = (commentId) => {
      const comment = comments.value.find((c) => c.commentId === commentId)
      return comment?.replies?.length < comment?.totalReplies
    }

    const toggleReplyForm = (commentId) => {
      activeReplyId.value = activeReplyId.value === commentId ? null : commentId
      newReply.value = ''
    }

    const cancelWrite = () => {
      isWriting.value = false
      newComment.value = ''
    }

    const cancelReply = () => {
      activeReplyId.value = null
      newReply.value = ''
    }

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString()
    }

    onMounted(() => {
      fetchComments()
    })

    return {
      comments,
      totalComments,
      isWriting,
      newComment,
      newReply,
      activeReplyId,
      hasMoreComments,
      hasMoreReplies,
      submitComment,
      submitReply,
      toggleLike,
      loadMoreComments,
      loadMoreReplies,
      toggleReplyForm,
      cancelWrite,
      cancelReply,
      formatDate,
    }
  },
}
</script>

<style scoped>
.comments-section {
  margin-top: 24px;
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.write-button {
  width: 100%;
  padding: 12px;
  background: #f5f5f5;
  border: none;
  border-radius: 4px;
  color: #666;
  cursor: pointer;
  margin-bottom: 20px;
}

.write-form,
.reply-form {
  margin-bottom: 20px;
}

.comment-input,
.reply-input {
  width: 100%;
  min-height: 100px;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 8px;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.cancel-button,
.submit-button {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button {
  border: 1px solid #e0e0e0;
  background: white;
}

.submit-button {
  border: none;
  background: #ff5a5a;
  color: white;
}

.comments-count {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 20px;
}

.comment-item {
  border-bottom: 1px solid #eee;
  padding: 16px 0;
}

.writer-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.profile-image {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  font-weight: 500;
}

.date {
  color: #666;
  font-size: 12px;
}

.text {
  line-height: 1.5;
  margin-bottom: 12px;
}

.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.reply-button {
  color: #666;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
}

.like-action {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.like-button {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
}

.replies-list {
  margin-left: 32px;
  margin-top: 12px;
}

.reply-item {
  padding: 12px 0;
  border-top: 1px solid #f5f5f5;
}

.more-button {
  width: 100%;
  padding: 12px;
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  margin-top: 12px;
}

.more-button:hover {
  background: #f5f5f5;
}
</style>
