import { apiClient } from './api';
import type { Profilo } from '../types';

export const profiloService = {
  async getAll(): Promise<Profilo[]> {
    const response = await apiClient.get('/api/profili');
    return response.data;
  },

  async getById(id: number): Promise<Profilo> {
    const response = await apiClient.get(`/api/profili/${id}`);
    return response.data;
  },

  async create(profilo: Profilo): Promise<Profilo> {
    const response = await apiClient.post('/api/profili', profilo);
    return response.data;
  },

  async update(id: number, profilo: Profilo): Promise<Profilo> {
    const response = await apiClient.put(`/api/profili/${id}`, profilo);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await apiClient.delete(`/api/profili/${id}`);
  },

  async count(): Promise<number> {
    const response = await apiClient.get('/api/profili/count');
    return response.data.count;
  },
};
