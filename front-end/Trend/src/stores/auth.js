import { defineStore } from 'pinia'
import { authApi } from '@/api/authApi'
import axios from 'axios'
import { storage } from '@/../firebase';
import { ref as firebaseRef, getDownloadURL } from 'firebase/storage';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    isAuthenticated: false,
    isInitialized: false,
    userId: null,
    userNickname: null,
    profileImageUrl: null,
  }),

  actions: {
    async login(userId, userPassword) {
      try {
        const response = await authApi.login(userId, userPassword)
        console.log('로그인 응답 데이터:', response.data)
        console.log('로그인 응답 헤더:', response.headers)

        const accessToken =
          response.headers['authorization'] ||
          response.headers.authrization ||
          response.headers.get('authorization')
        console.log('전체 헤더:', accessToken)

        const userData = response.data

        if (accessToken) {
          // Firebase에서 프로필 이미지 미리 가져오기
          const profileImageUrl = await this.loadFirebaseImage(userData.profileImgUrl);

          this.setAuthData(accessToken, userData, profileImageUrl)
          this.saveToLocalStorage(); // 로컬 스토리지 저장
          console.log('토큰 및 사용자 데이터 저장 완료:', this.accessToken, this.userId)
          return response
        } else {
          console.error('Authorization 헤더 없음')
          throw new Error('No access token received')
        }
      } catch (error) {
        console.error('authStore.login 에러:', error)
        this.clearAuth()
        throw error
      }
    },

    async loadFirebaseImage(imagePath) {
      if (!imagePath) return '/default-profile.svg'; // 기본 이미지 경로
      try {
        const storageRef = firebaseRef(storage, imagePath);
        return await getDownloadURL(storageRef);
      } catch (error) {
        console.error('Firebase에서 프로필 이미지를 가져오는 데 실패했습니다:', error);
        return '/default-profile.svg';
      }
    },

    async refreshToken() {
      try {
        const response = await authApi.refreshToken()
        const newAccessToken = response.headers['authorization']

        if (newAccessToken) {
          this.setAccessToken(newAccessToken)
          return newAccessToken
        }
        throw new Error('No access token received')
      } catch (error) {
        this.clearAuth()
        throw error
      }
    },

    // 로그아웃 처리
    async logout() {
      try {
        await authApi.logout()
      } finally {
        this.clearAuth()
        this.removeFromLocalStorage()
      }
    },

    // 전역 상태 설정
    setAuthData(accessToken, userData, profileImageUrl) {
      this.accessToken = accessToken
      this.userId = userData.userId
      this.userNickname = userData.nickName
      this.profileImageUrl = profileImageUrl
      this.isAuthenticated = true
    },

    // 상태 초기화
    clearAuth() {
      this.accessToken = null
      this.userId = null
      this.userNickname = null
      this.profileImageUrl = null
      this.isAuthenticated = false
    },

    // 로컬 스토리지 저장
    saveToLocalStorage() {
      localStorage.setItem(
        'auth',
        JSON.stringify({
          accessToken: this.accessToken,
          userId: this.userId,
          userNickname: this.userNickname,
          profileImageUrl: this.profileImageUrl,
          isAuthenticated: this.isAuthenticated,
        }),
      )
    },

    // 로컬 스토리지 복원
    restoreFromLocalStorage() {
      const savedAuth = localStorage.getItem('auth')
      if (savedAuth) {
        const parsedAuth = JSON.parse(savedAuth)
        this.setAuthData(parsedAuth.accessToken, parsedAuth)
      }
    },

    // 로컬 스토리지 삭제
    removeFromLocalStorage() {
      localStorage.removeItem('auth')
    },
  },
})
