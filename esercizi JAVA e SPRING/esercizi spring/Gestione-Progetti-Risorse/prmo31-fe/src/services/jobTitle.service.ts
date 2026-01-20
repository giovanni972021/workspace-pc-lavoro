import apiClient from './api';
import type { JobTitle, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/job-titles';

export const jobTitleService = {
  getAll: async (params?: SearchParams): Promise<JobTitle[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<JobTitle> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (jobTitle: JobTitle): Promise<JobTitle> => {
    const response = await apiClient.post(BASE_PATH, jobTitle);
    return response.data;
  },

  update: async (id: number, jobTitle: JobTitle): Promise<JobTitle> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, jobTitle);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },
};
