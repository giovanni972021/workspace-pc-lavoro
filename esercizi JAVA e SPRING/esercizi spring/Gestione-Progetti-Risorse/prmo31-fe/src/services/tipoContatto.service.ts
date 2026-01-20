import api from './api';
import type { TipoContatto } from '../types/index.js';

const TIPO_CONTATTO_BASE_PATH = '/api/tipi-contatto';

export const tipoContattoService = {
  getAll: async (params?: { page?: number; size?: number }) => {
    const response = await api.get<TipoContatto[]>(TIPO_CONTATTO_BASE_PATH, { params });
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<TipoContatto>(`${TIPO_CONTATTO_BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (tipoContatto: Omit<TipoContatto, 'id'>) => {
    const response = await api.post<TipoContatto>(TIPO_CONTATTO_BASE_PATH, tipoContatto);
    return response.data;
  },

  update: async (id: number, tipoContatto: Partial<TipoContatto>) => {
    const response = await api.put<TipoContatto>(`${TIPO_CONTATTO_BASE_PATH}/${id}`, tipoContatto);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`${TIPO_CONTATTO_BASE_PATH}/${id}`);
  },

  count: async () => {
    const response = await api.get<number>(`${TIPO_CONTATTO_BASE_PATH}/count`);
    return response.data;
  }
};
