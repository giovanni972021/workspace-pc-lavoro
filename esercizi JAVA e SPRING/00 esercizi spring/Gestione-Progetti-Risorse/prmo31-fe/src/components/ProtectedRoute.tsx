import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { isAuthenticated, isLoading, currentUser, userLoadError, logout } = useAuth();

  // Show loading while authentication is being checked
  if (isLoading) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh'
      }}>
        <div>
          <h2>Caricamento...</h2>
        </div>
      </div>
    );
  }

  // If not authenticated, redirect to login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // If there was an error loading the user profile, show error message
  if (isAuthenticated && !currentUser && userLoadError) {
    return (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        gap: '1rem',
        padding: '2rem'
      }}>
        <div style={{ textAlign: 'center' }}>
          <h2 style={{ color: '#e53e3e', marginBottom: '1rem' }}>Errore</h2>
          <p>{userLoadError}</p>
          <button
            onClick={logout}
            style={{
              marginTop: '1rem',
              padding: '0.5rem 1rem',
              backgroundColor: '#3182ce',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            Torna al Login
          </button>
        </div>
      </div>
    );
  }

  // If authenticated but currentUser is not yet loaded and no error, show loading
  // This prevents protected components from making API calls before user data is ready
  if (isAuthenticated && !currentUser) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh'
      }}>
        <div>
          <h2>Caricamento profilo utente...</h2>
        </div>
      </div>
    );
  }

  return <>{children}</>;
};

export default ProtectedRoute;
