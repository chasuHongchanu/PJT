import axiosInstance from './axiosInstance'

export const tradeApi = {
  getReservation(formData) {
    return axiosInstance.get('/trade/reservation', {
      params: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  createReservation(requestBody) {
    return axiosInstance.post('/trade/reservation', requestBody, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  getTradeDetail(tradeId) {
    return axiosInstance.get(`/trade/detail/${tradeId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  updateReservation(requestBody) {
    return axiosInstance.put('/trade/reservation', requestBody, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  completePay(tradeId) {
    return axiosInstance.put(`/trade/pay/${tradeId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  insertItemConditionImages(formData) {
    return axiosInstance.put('/trade/detail', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  getItemInfoForReview(tradeId) {
    return axiosInstance.get(`/trade/detail/${tradeId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  updateTradeStatusToReturn(tradeId) {
    return axiosInstance.put(`/trade/return/${tradeId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  insertReview(requestBody) {
    return axiosInstance.post('/trade/review', requestBody, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  getRegistedItems() {
    return axiosInstance.get(`/trade/registItems`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  getLendItems() {
    return axiosInstance.get(`/trade/lendItems`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },

  getLeaseItems() {
    return axiosInstance.get(`/trade/leaseItems`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('auth') ? JSON.parse(localStorage.getItem('auth')).accessToken : ''}`,
      },
    })
  },
}
