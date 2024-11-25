import { storage } from '@/firebase';
import { ref, getDownloadURL } from 'firebase/storage';
import DefaultProfileImage from '@/assets/default-profile.svg';

export const firebaseUtils = {
  async getImageUrl(imagePath) {
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
  }
};