import { defineStore } from 'pinia'
import { authApi } from '@/api/authApi'
import axios from 'axios'
import { storage } from '@/../firebase';
import { ref as firebaseRef, getDownloadURL } from 'firebase/storage';
import DefaultProfileImage from '@/assets/default-profile.svg';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    isAuthenticated: false,
    isInitialized: false,
    userId: null,
    userNickname: null,
    profileImgUrl: null,
    profileImage: null,
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
          const profileImage = await this.loadFirebaseImage(userData.profileImgUrl);

          this.setAuthData(accessToken, userData, profileImage)
          this.saveToLocalStorage()
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
      if (!imagePath) return DefaultProfileImage; // 기본 이미지 경로
      try {
        const storageRef = firebaseRef(storage, imagePath);
        const imageUrl = await getDownloadURL(storageRef);
        return imageUrl;
      } catch (error) {
        console.error('Firebase에서 프로필 이미지를 가져오는 데 실패했습니다:', error);
        return DefaultProfileImage;
      }
    },

    async initializeAuth() {
      try {
        const savedAuth = localStorage.getItem('auth')
        if (savedAuth) {
          const parsedAuth = JSON.parse(savedAuth)
          
          // 먼저 저장된 상태 복원
          this.accessToken = parsedAuth.accessToken
          this.userId = parsedAuth.userId
          this.userNickname = parsedAuth.userNickname
          this.profileImgUrl = parsedAuth.profileImgUrl
          this.isAuthenticated = true

          try {
            // 토큰 유효성 검증을 위한 간단한 API 호출
            await axios.get('http://localhost:8080/api/user/userinfo', {
              headers: {
                Authorization: this.accessToken
              },
              withCredentials: true
            });
            
            // API 호출이 성공하면 토큰이 유효한 것
            const profileImage = await this.loadFirebaseImage(parsedAuth.profileImgUrl);
            this.profileImage = profileImage;
          } catch (error) {
            if (error.response?.status === 401) {
              // 토큰이 만료된 경우 갱신 시도
              console.log('토큰 만료. 갱신 시도...');
              await this.refreshToken();
            } else {
              throw error;
            }
          }
        }
      } catch (error) {
        console.error('인증 초기화 실패:', error);
        this.clearAuth();
      } finally {
        this.isInitialized = true;
      }
    },


    async refreshToken() {
      try {
        const response = await authApi.refreshToken()
        const newAccessToken = response.headers['authorization'] || 
                             response.headers['Authorization']

        if (newAccessToken) {
          this.setAccessToken(newAccessToken)
          this.isAuthenticated = true
          return newAccessToken
        }
        throw new Error('No access token received')
      } catch (error) {
        console.error('토큰 갱신 실패:', error)
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

    // 프로필 업데이트를 위한 새로운 메서드
    async updateProfile({ userNickname, userProfileImgUrl }) {
      try {
        if (userProfileImgUrl) {
          this.profileImgUrl = userProfileImgUrl;
          this.profileImage = await this.loadFirebaseImage(userProfileImgUrl);
        }
        
        if (userNickname) {
          this.userNickname = userNickname;
        }
    
        // 로컬 스토리지 업데이트
        this.saveToLocalStorage();
      } catch (error) {
        console.error('프로필 상태 업데이트 실패:', error);
        throw error;
      }
    },

    // 전역 상태 설정
    setAuthData(accessToken, userData, profileImage) {
      this.accessToken = accessToken
      this.userId = userData.userId
      this.userNickname = userData.nickName
      this.profileImgUrl = userData.profileImgUrl
      this.profileImage = profileImage
      this.isAuthenticated = true
    },

    setAccessToken(token) {
      this.accessToken = token;
      this.saveToLocalStorage();
    },

    // 상태 초기화
    clearAuth() {
      this.accessToken = null
      this.userId = null
      this.userNickname = null
      this.profileImgUrl = null
      this.profileImage = null
      this.isAuthenticated = false
      this.isInitialized = false
    },

    // 로컬 스토리지 저장
    saveToLocalStorage() {
      localStorage.setItem(
        'auth',
        JSON.stringify({
          accessToken: this.accessToken,
          userId: this.userId,
          userNickname: this.userNickname,
          profileImgUrl: this.profileImgUrl,
          isAuthenticated: this.isAuthenticated,
        }),
      )
    },

    removeFromLocalStorage() {
      localStorage.removeItem('auth')
    },
  },
})