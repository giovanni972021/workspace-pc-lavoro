import apiClient from './api';
import type { Certificazione, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/certificazioni';

export const certificazioneService = {
  getAll: async (params?: SearchParams): Promise<Certificazione[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Certificazione> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (certificazione: Certificazione): Promise<Certificazione> => {
    const response = await apiClient.post(BASE_PATH, certificazione);
    return response.data;
  },

  update: async (id: number, certificazione: Certificazione): Promise<Certificazione> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, certificazione);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<Certificazione[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },
};
