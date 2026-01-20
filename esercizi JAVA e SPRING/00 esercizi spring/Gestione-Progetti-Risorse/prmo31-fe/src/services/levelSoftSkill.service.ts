import apiClient from './api';
import type { LevelSoftSkill, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/level-soft-skills';

export const levelSoftSkillService = {
  getAll: async (params?: SearchParams): Promise<LevelSoftSkill[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<LevelSoftSkill> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (levelSoftSkill: LevelSoftSkill): Promise<LevelSoftSkill> => {
    const response = await apiClient.post(BASE_PATH, levelSoftSkill);
    return response.data;
  },

  update: async (id: number, levelSoftSkill: LevelSoftSkill): Promise<LevelSoftSkill> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, levelSoftSkill);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },
};
