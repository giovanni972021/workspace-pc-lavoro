import api from './api';
import type { TipoUtente } from '../types/index.js';

const TIPO_UTENTE_BASE_PATH = '/api/tipi-utente';

export const tipoUtenteService = {
  getAll: async (params?: { page?: number; size?: number }) => {
    const response = await api.get<TipoUtente[]>(TIPO_UTENTE_BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<TipoUtente>(`${TIPO_UTENTE_BASE_PATH}/${id}`);
    return response.data;
  },

  getByTipo: async (tipo: string) => {
    const response = await api.get<TipoUtente>(`${TIPO_UTENTE_BASE_PATH}/tipo/${tipo}`);
    return response.data;
  },

  create: async (tipoUtente: Omit<TipoUtente, 'id'>) => {
    const response = await api.post<TipoUtente>(TIPO_UTENTE_BASE_PATH, tipoUtente);
    return response.data;
  },

  update: async (id: number, tipoUtente: Partial<TipoUtente>) => {
    const response = await api.put<TipoUtente>(`${TIPO_UTENTE_BASE_PATH}/${id}`, tipoUtente);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`${TIPO_UTENTE_BASE_PATH}/${id}`);
  },

  count: async () => {
    const response = await api.get<number>(`${TIPO_UTENTE_BASE_PATH}/count`);
    return response.data;
  }
};
