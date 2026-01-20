import apiClient from './api';
import type { Skill, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/skills';

export const skillService = {
  getAll: async (params?: SearchParams): Promise<Skill[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Skill> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (skill: Skill): Promise<Skill> => {
    const response = await apiClient.post(BASE_PATH, skill);
    return response.data;
  },

  update: async (id: number, skill: Skill): Promise<Skill> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, skill);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<Skill[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },
};
