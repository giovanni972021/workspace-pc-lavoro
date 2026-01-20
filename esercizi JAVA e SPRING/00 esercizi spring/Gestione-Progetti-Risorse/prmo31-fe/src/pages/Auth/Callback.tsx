import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userManager } from '../../auth/userManager';

const Callback: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    userManager
      .signinRedirectCallback()
      .then(() => {
        navigate('/dashboard');
      })
      .catch((error) => {
        console.error('Callback error:', error);
        navigate('/');
      });
  }, [navigate]);

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh'
    }}>
      <div>
        <h2>Autenticazione in corso...</h2>
        <p>Attendere prego...</p>
      </div>
    </div>
  );
};

export default Callback;
