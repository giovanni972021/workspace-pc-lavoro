import api from './api';
import type { StatoProgetto } from '../types/index.js';

const STATO_PROGETTO_BASE_PATH = '/api/stati-progetto';

export const statoProgettoService = {
  getAll: async (params?: { page?: number; size?: number }) => {
    const response = await api.get<StatoProgetto[]>(STATO_PROGETTO_BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<StatoProgetto>(`${STATO_PROGETTO_BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (statoProgetto: Omit<StatoProgetto, 'id'>) => {
    const response = await api.post<StatoProgetto>(STATO_PROGETTO_BASE_PATH, statoProgetto);
    return response.data;
  },

  update: async (id: number, statoProgetto: Partial<StatoProgetto>) => {
    const response = await api.put<StatoProgetto>(`${STATO_PROGETTO_BASE_PATH}/${id}`, statoProgetto);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`${STATO_PROGETTO_BASE_PATH}/${id}`);
  },

  count: async () => {
    const response = await api.get<number>(`${STATO_PROGETTO_BASE_PATH}/count`);
    return response.data;
  }
};
