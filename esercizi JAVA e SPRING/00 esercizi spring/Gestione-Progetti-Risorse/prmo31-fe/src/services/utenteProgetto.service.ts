import apiClient from './api';
import type { UtenteProgetto, GanttResponseDTO, GanttProgettiResponseDTO, GanttParams, SearchParams } from '../types/index.js';

export const utenteProgettoService = {
  getAll: async (params?: SearchParams): Promise<UtenteProgetto[]> => {
    const response = await apiClient.get('/api/utente-progetto', { params });
    return response.data;
  },

  getById: async (id: number): Promise<UtenteProgetto> => {
    const response = await apiClient.get(`/api/utente-progetto/${id}`);
    return response.data;
  },

  create: async (utenteProgetto: UtenteProgetto): Promise<UtenteProgetto> => {
    const response = await apiClient.post('/api/utente-progetto', utenteProgetto);
    return response.data;
  },

  update: async (id: number, utenteProgetto: UtenteProgetto): Promise<UtenteProgetto> => {
    const response = await apiClient.put(`/api/utente-progetto/${id}`, utenteProgetto);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/utente-progetto/${id}`);
  },

  count: async (): Promise<number> => {
    const response = await apiClient.get('/api/utente-progetto/count');
    return response.data.count;
  },

  getGantt: async (params?: GanttParams): Promise<GanttResponseDTO> => {
    const response = await apiClient.get('/api/utente-progetto/gantt', { params });
    return response.data;
  },

  getGanttProgetti: async (params?: GanttParams): Promise<GanttProgettiResponseDTO> => {
    const response = await apiClient.get('/api/utente-progetto/gantt-progetti', { params });
    return response.data;
  }
};
