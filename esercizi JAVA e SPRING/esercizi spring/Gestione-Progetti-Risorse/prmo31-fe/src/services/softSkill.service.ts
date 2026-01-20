import apiClient from './api';
import type { SoftSkill, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/soft-skills';

export const softSkillService = {
  getAll: async (params?: SearchParams): Promise<SoftSkill[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<SoftSkill> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (softSkill: SoftSkill): Promise<SoftSkill> => {
    const response = await apiClient.post(BASE_PATH, softSkill);
    return response.data;
  },

  update: async (id: number, softSkill: SoftSkill): Promise<SoftSkill> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, softSkill);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<SoftSkill[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },
};
