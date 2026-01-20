import { apiClient } from './api';
import type { CurrentUser } from '../types';

export const currentUserService = {
  async getCurrentUser(): Promise<CurrentUser> {
    const response = await apiClient.get('/api/current-user');
    return response.data;
  },
};
