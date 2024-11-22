import { defineStore } from 'pinia'
import { authApi } from '@/api/authApi'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    isAuthenticated: false,
    isInitialized: false
  }),

  actions: {
    async login(userId, userPassword) {
      try {
        console.log("도달하나요??")
        const response = await authApi.login(userId, userPassword)
        const accessToken = response.headers['authorization'] || response.headers.authrization || response.headers.get('authorization');
        console.log("전체 헤더:", accessToken);
        
        
        if (accessToken) {
          this.setAccessToken(accessToken)
          return response
        }
        throw new Error('No access token received')
      } catch (error) {
        this.clearAuth()
        throw error
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

    async logout() {
      try {
        await authApi.logout()
      } finally {
        this.clearAuth()
      }
    },

    setAccessToken(token) {
      this.accessToken = token
      this.isAuthenticated = true
      this.isInitialized = true
    },

    clearAuth() {
      this.accessToken = null
      this.isAuthenticated = false
    }
  }
})