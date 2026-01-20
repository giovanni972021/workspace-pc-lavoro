import React from 'react';
import { useAuth } from '../contexts/AuthContext';

interface RoleGuardProps {
  roles?: string[];
  requireAll?: boolean;
  superUserOnly?: boolean;
  children: React.ReactNode;
  fallback?: React.ReactNode;
}

/**
 * Componente per proteggere contenuti in base ai ruoli dell'utente.
 *
 * @param roles - Array di ruoli richiesti
 * @param requireAll - Se true, richiede tutti i ruoli. Se false, richiede almeno uno (default: false)
 * @param superUserOnly - Se true, mostra il contenuto solo ai SUPER_USER
 * @param children - Contenuto da mostrare se l'utente ha i permessi
 * @param fallback - Contenuto da mostrare se l'utente non ha i permessi (opzionale)
 */
const RoleGuard: React.FC<RoleGuardProps> = ({
  roles = [],
  requireAll = false,
  superUserOnly = false,
  children,
  fallback = null,
}) => {
  const { currentUser, isSuperUser, hasRole, hasAnyRole } = useAuth();

  // Se non c'è utente corrente, non mostrare nulla
  if (!currentUser) {
    return <>{fallback}</>;
  }

  // Se richiesto solo SUPER_USER
  if (superUserOnly) {
    return isSuperUser() ? <>{children}</> : <>{fallback}</>;
  }

  // I SUPER_USER hanno sempre accesso
  if (isSuperUser()) {
    return <>{children}</>;
  }

  // Se non ci sono ruoli specificati, mostra il contenuto
  if (roles.length === 0) {
    return <>{children}</>;
  }

  // Verifica i ruoli
  let hasPermission = false;

  if (requireAll) {
    // Richiede tutti i ruoli
    hasPermission = roles.every(role => hasRole(role));
  } else {
    // Richiede almeno uno dei ruoli
    hasPermission = hasAnyRole(...roles);
  }

  return hasPermission ? <>{children}</> : <>{fallback}</>;
};

export default RoleGuard;
