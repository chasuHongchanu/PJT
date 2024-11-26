import { storage } from '@/firebase';
import { ref, getDownloadURL } from 'firebase/storage';
import DefaultProfileImage from '@/assets/default-profile.svg';
import DefaultImage from '@/assets/default-image.svg';

export const firebaseUtils = {
  async getProfileImageUrl(imagePath) {
    if (!imagePath) {
      return DefaultProfileImage;
    }

    try {
      const storageRef = ref(storage, imagePath);
      const url = await getDownloadURL(storageRef);
      return url;
    } catch (error) {
      console.error('Firebase 이미지 로드 실패:', error);
      return DefaultProfileImage;
    }
  },
  async getImageUrl(imagePath) {
    if (!imagePath) {
      return DefaultImage;
    }

    try {
      const storageRef = ref(storage, imagePath);
      const url = await getDownloadURL(storageRef);
      return url;
    } catch (error) {
      console.error('Firebase 이미지 로드 실패:', error);
      return DefaultImage;
    }
  }
};