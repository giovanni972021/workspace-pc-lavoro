import apiClient from './api';
import type { Utente, UtenteListDTO, SearchParams } from '../types/index.js';

const BASE_PATH = '/api/utenti';

export const utenteService = {
  getAll: async (params?: SearchParams): Promise<UtenteListDTO[]> => {
    const response = await apiClient.get(BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Utente> => {
    const response = await apiClient.get(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (utente: Utente): Promise<Utente> => {
    const response = await apiClient.post(BASE_PATH, utente);
    return response.data;
  },

  update: async (id: number, utente: Utente): Promise<Utente> => {
    const response = await apiClient.put(`${BASE_PATH}/${id}`, utente);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  search: async (searchTerm: string): Promise<UtenteListDTO[]> => {
    const response = await apiClient.get(`${BASE_PATH}/search`, {
      params: { q: searchTerm },
    });
    return response.data;
  },
};
