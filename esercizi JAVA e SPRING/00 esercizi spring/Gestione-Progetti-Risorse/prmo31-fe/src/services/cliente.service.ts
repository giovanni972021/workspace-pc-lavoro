import apiClient from './api';
import type { Cliente, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/clienti';

export const clienteService = {
  getAll: async (params?: SearchParams): Promise<Cliente[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Cliente> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (cliente: Cliente): Promise<Cliente> => {
    const response = await apiClient.post(BASE_PATH, cliente);
    return response.data;
  },

  update: async (id: number, cliente: Cliente): Promise<Cliente> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, cliente);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<Cliente[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },
};
