import apiClient from './api';
import type { LevelSkill, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/level-skills';

export const levelSkillService = {
  getAll: async (params?: SearchParams): Promise<LevelSkill[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<LevelSkill> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (levelSkill: LevelSkill): Promise<LevelSkill> => {
    const response = await apiClient.post(BASE_PATH, levelSkill);
    return response.data;
  },

  update: async (id: number, levelSkill: LevelSkill): Promise<LevelSkill> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, levelSkill);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },
};
