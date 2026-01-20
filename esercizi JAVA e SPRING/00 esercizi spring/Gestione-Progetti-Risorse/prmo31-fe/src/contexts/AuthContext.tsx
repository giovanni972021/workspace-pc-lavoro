import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { User } from 'oidc-client-ts';
import type { CurrentUser } from '../types';
import { currentUserService } from '../services/currentUser.service';
import { userManager } from '../auth/userManager';

interface AuthContextType {
  user: User | null;
  currentUser: CurrentUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  userLoadError: string | null;
  login: () => Promise<void>;
  logout: () => Promise<void>;
  getAccessToken: () => string | null;
  hasRole: (role: string) => boolean;
  hasAnyRole: (...roles: string[]) => boolean;
  isSuperUser: () => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [userLoadError, setUserLoadError] = useState<string | null>(null);

  // Load current user profile and roles from backend
  const loadCurrentUser = async (oidcUser: User) => {
    // Only load current user if we have a valid OIDC user
    if (!oidcUser || oidcUser.expired) {
      setCurrentUser(null);
      setUserLoadError(null);
      return;
    }

    try {
      const userData = await currentUserService.getCurrentUser();
      setCurrentUser(userData);
      setUserLoadError(null);
    } catch (error: any) {
      console.error('Error loading current user:', error);

      const status = error?.response?.status;

      // If we get a 401, the token is invalid - remove it and redirect to login
      if (status === 401) {
        await userManager.removeUser();
        setUser(null);
        setCurrentUser(null);
        setUserLoadError('Sessione non valida. Effettua nuovamente il login.');
      }
      // If we get a 404, the user doesn't exist in the database
      else if (status === 404) {
        setCurrentUser(null);
        setUserLoadError('Utente non trovato nel database. Contatta l\'amministratore.');
      }
      // For any other error (500, network errors, etc.), show a generic message
      else {
        setCurrentUser(null);
        setUserLoadError('Errore nel caricamento del profilo utente. Riprova più tardi.');
      }
    }
  };

  useEffect(() => {
    // Check if user is already authenticated
    userManager.getUser().then(async (user) => {
      if (user && !user.expired) {
        setUser(user);
        await loadCurrentUser(user);
      } else if (user && user.expired) {
        // Clean up expired user
        await userManager.removeUser();
      }
      setIsLoading(false);
    }).catch(error => {
      console.error('Error checking user authentication:', error);
      setIsLoading(false);
    });

    // Listen for user loaded events
    const handleUserLoaded = async (user: User) => {
      setUser(user);
      await loadCurrentUser(user);
      setIsLoading(false);
    };

    const handleUserUnloaded = () => {
      setUser(null);
      setCurrentUser(null);
    };

    const handleAccessTokenExpiring = () => {
      console.log('Access token expiring...');
    };

    const handleAccessTokenExpired = () => {
      console.log('Access token expired');
      setUser(null);
      setCurrentUser(null);
    };

    userManager.events.addUserLoaded(handleUserLoaded);
    userManager.events.addUserUnloaded(handleUserUnloaded);
    userManager.events.addAccessTokenExpiring(handleAccessTokenExpiring);
    userManager.events.addAccessTokenExpired(handleAccessTokenExpired);

    return () => {
      userManager.events.removeUserLoaded(handleUserLoaded);
      userManager.events.removeUserUnloaded(handleUserUnloaded);
      userManager.events.removeAccessTokenExpiring(handleAccessTokenExpiring);
      userManager.events.removeAccessTokenExpired(handleAccessTokenExpired);
    };
  }, []);

  const login = async () => {
    try {
      await userManager.signinRedirect();
    } catch (error) {
      console.error('Login error:', error);
    }
  };

  const logout = async () => {
    try {
      await userManager.signoutRedirect();
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const getAccessToken = (): string | null => {
    return user?.access_token || null;
  };

  const hasRole = (role: string): boolean => {
    return currentUser?.ruoli.includes(role) || false;
  };

  const hasAnyRole = (...roles: string[]): boolean => {
    if (!currentUser) return false;
    return roles.some(role => currentUser.ruoli.includes(role));
  };

  const isSuperUser = (): boolean => {
    return currentUser?.isSuperUser || false;
  };

  const value: AuthContextType = {
    user,
    currentUser,
    isAuthenticated: !!user && !user.expired,
    isLoading,
    userLoadError,
    login,
    logout,
    getAccessToken,
    hasRole,
    hasAnyRole,
    isSuperUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
