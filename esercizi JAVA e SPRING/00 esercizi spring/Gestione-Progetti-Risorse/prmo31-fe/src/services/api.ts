import axios from 'axios';
import { userManager } from '../auth/userManager';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for authentication
apiClient.interceptors.request.use(
  async (config) => {
    try {
      const user = await userManager.getUser();
      if (user && !user.expired) {
        config.headers.Authorization = `Bearer ${user.access_token}`;
      }
    } catch (error) {
      console.error('Error getting user token:', error);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized - redirect to login only if not already there
      const currentPath = window.location.pathname;
      if (currentPath !== '/login' && currentPath !== '/callback') {
        try {
          await userManager.removeUser();
        } catch (e) {
          console.error('Error removing user:', e);
        }
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
