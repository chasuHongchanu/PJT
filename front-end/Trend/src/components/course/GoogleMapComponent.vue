<template>
  <div class="map-wrapper">
    <div ref="mapDiv" style="width: 100%; height: 100%"></div>
    <div v-if="isLoading" class="loading-overlay">
      <span>지도를 불러오는 중입니다...</span>
    </div>
  </div>
</template>

<script>
import { ref, toRef, onMounted, watchEffect } from 'vue'
import { debounce } from 'lodash'

export default {
  name: 'GoogleMap',

  props: {
    apiKey: {
      type: String,
      required: true,
    },
    spots: {
      type: Array,
      default: () => [],
    },
    connectMarkers: {
      type: Boolean,
      default: false,
    },
    zoom: {
      type: Number,
      default: 14,
    },
  },

  setup(props) {
    const mapDiv = ref(null)
    const map = ref(null)
    const geocoder = ref(null)
    const mapMarkers = ref([])
    const pathLine = ref(null)
    const isLoading = ref(true)
    const isMapInitialized = ref(false)
    const spots = toRef(props, 'spots')

    // spots 변경 감지
    const updateMarkersDebounced = debounce(() => {
      clearMapObjects()
      updateMarkers()
    }, 300)

    watchEffect(() => {
      if (isMapInitialized.value && map.value) {
        clearMapObjects()
        updateMarkers()
      }
    })

    // 마커와 경로선 초기화 함수
    const clearMapObjects = () => {
      console.log('Clearing map objects...')
      if (!map.value) return

      // 기존 마커 제거
      mapMarkers.value.forEach((marker) => {
        if (marker) {
          marker.setMap(null) // 지도에서 제거
          google.maps.event.clearInstanceListeners(marker) // 리스너 제거
        }
      })
      mapMarkers.value = [] // 마커 배열 초기화

      // 기존 경로선 제거
      if (pathLine.value) {
        pathLine.value.setMap(null)
        google.maps.event.clearInstanceListeners(pathLine.value)
        pathLine.value = null
      }
    }

    const initializeMap = () => {
      if (!window.google) return

      map.value = new window.google.maps.Map(mapDiv.value, {
        center: { lat: 37.5665, lng: 126.978 },
        zoom: props.zoom,
        zoomControl: true,
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
      })

      geocoder.value = new window.google.maps.Geocoder()
      isMapInitialized.value = true

      // 초기화 완료 후 첫 번째 마커 업데이트 호출
      updateMarkers()
    }

    // 커스텀 마커 스타일 생성
    const createCustomMarkerIcon = (text) => ({
      path: window.google.maps.SymbolPath.CIRCLE,
      fillColor: '#FF5A5A',
      fillOpacity: 1,
      strokeWeight: 2,
      strokeColor: '#FFFFFF',
      scale: 15,
      labelOrigin: new window.google.maps.Point(0, 0),
    })

    // 마커 업데이트
    // 마커 업데이트
    const updateMarkers = () => {
      console.log('Updating markers with spots:', props.spots)
      if (!map.value || !isMapInitialized.value) return

      isLoading.value = true

      // spots가 비어있으면 여기서 종료
      if (!props.spots || props.spots.length === 0) {
        isLoading.value = false
        return
      }

      const locations = props.spots
        .map((spot) => ({
          lat: parseFloat(spot.latitude),
          lng: parseFloat(spot.longitude),
        }))
        .filter((loc) => !isNaN(loc.lat) && !isNaN(loc.lng))

      if (locations.length === 0) {
        isLoading.value = false
        return
      }

      // 새로운 마커 생성
      mapMarkers.value = locations.map((location, index) => {
        return new window.google.maps.Marker({
          position: location,
          map: map.value,
          icon: createCustomMarkerIcon(),
          label: {
            text: String(index + 1),
            color: 'white',
            fontSize: '12px',
            fontWeight: 'bold',
          },
        })
      })

      // 새로운 경로선 생성
      if (props.connectMarkers && locations.length > 1) {
        pathLine.value = new window.google.maps.Polyline({
          path: locations,
          geodesic: true,
          strokeColor: '#FF5A5A',
          strokeOpacity: 0.8,
          strokeWeight: 2,
          map: map.value,
        })
      }

      // 지도 범위 조정
      const bounds = new window.google.maps.LatLngBounds()
      locations.forEach((location) => bounds.extend(location))
      map.value.fitBounds(bounds)

      isLoading.value = false
    }

    // 구글맵 스크립트 로드
    const loadGoogleMapsScript = () => {
      if (window.google) {
        initializeMap()
        return
      }

      const script = document.createElement('script')
      script.src = `https://maps.googleapis.com/maps/api/js?key=${props.apiKey}`
      script.async = true
      script.defer = true
      script.onload = () => {
        initializeMap()
      }
      document.head.appendChild(script)
    }

    onMounted(() => {
      loadGoogleMapsScript()
    })

    return {
      mapDiv,
      isLoading,
    }
  },
}
</script>

<style scoped>
.map-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}
</style>
