import apiClient from './api';
import type { Curriculum, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/curricula';

export const curriculumService = {
  getAll: async (params?: SearchParams): Promise<Curriculum[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Curriculum> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  getByUtenteId: async (utenteId: number): Promise<Curriculum> => {
    const response = await apiClient.get(`${BASE_PATH}/utente/${utenteId}`);
    return response.data;
  },

  create: async (curriculum: Curriculum): Promise<Curriculum> => {
    const response = await apiClient.post(BASE_PATH, curriculum);
    return response.data;
  },

  update: async (id: number, curriculum: Curriculum): Promise<Curriculum> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, curriculum);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },
};
