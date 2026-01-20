import apiClient from './api';
import type { Progetto, SearchParams, ProgettoAllocazioneDTO } from '../types/index.js';

const BASE_PATH = '/api/progetti';

export const progettoService = {
  getAll: async (params?: SearchParams): Promise<Progetto[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Progetto> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (progetto: Progetto): Promise<Progetto> => {
    const response = await apiClient.post(BASE_PATH, progetto);
    return response.data;
  },

  update: async (id: number, progetto: Progetto): Promise<Progetto> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, progetto);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<Progetto[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },

  getAllocazioni: async (): Promise<ProgettoAllocazioneDTO[]> => {
    const response = await apiClient.get(`${BASE_PATH}/allocazioni`);
    return response.data;
  },
};
