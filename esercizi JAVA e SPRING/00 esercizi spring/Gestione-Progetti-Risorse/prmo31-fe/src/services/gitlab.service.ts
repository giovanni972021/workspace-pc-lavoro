import apiClient from './api';
import type {
  GitLabProject,
  GitLabStatsResponse,
  MergeRequestStats,
  IssueStats,
  SimpleUtente,
} from '../types/index.js';

const BASE_PATH = '/api/gitlab';

export interface GitLabStatsParams {
  startDate: string;
  endDate: string;
  userEmail?: string;
  projectId?: number;
}

export const gitlabService = {
  getProjects: async (): Promise<GitLabProject[]> => {
    const response = await apiClient.get(`${BASE_PATH}/projects`);
    return response.data;
  },

  getCommitStats: async (
    params: GitLabStatsParams
  ): Promise<GitLabStatsResponse> => {
    const response = await apiClient.get(`${BASE_PATH}/stats/commits`, {
      params,
    });
    return response.data;
  },

  getMergeRequestStats: async (
    params: GitLabStatsParams
  ): Promise<MergeRequestStats[]> => {
    const response = await apiClient.get(`${BASE_PATH}/stats/merge-requests`, {
      params,
    });
    return response.data;
  },

  getIssueStats: async (params: GitLabStatsParams): Promise<IssueStats[]> => {
    const response = await apiClient.get(`${BASE_PATH}/stats/issues`, {
      params,
    });
    return response.data;
  },

  getUtenti: async (query?: string): Promise<SimpleUtente[]> => {
    const response = await apiClient.get(`${BASE_PATH}/utenti`, {
      params: { q: query },
    });
    return response.data;
  },

  getProgettiGitLab: async (): Promise<GitLabProject[]> => {
    const response = await apiClient.get(`${BASE_PATH}/projects`);
    return response.data;
  },

  // --- NUOVA FUNZIONE AGGIUNTA QUI SOTTO ---
  getPushEventsStats: async (params: GitLabStatsParams): Promise<any[]> => {
    const response = await apiClient.get(`${BASE_PATH}/stats/push-events`, {
      params,
    });
    return response.data;
  },
};