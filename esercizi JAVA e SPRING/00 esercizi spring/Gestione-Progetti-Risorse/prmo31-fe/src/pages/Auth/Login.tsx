import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import Button from '../../components/Button';

const Login: React.FC = () => {
  const { login } = useAuth();

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      gap: '2rem'
    }}>
      <img
        src="/logo-schema31.png"
        alt="Schema31"
        style={{ maxWidth: '200px' }}
      />
      <h1>CTO Manager</h1>
      <p>Gestione delle risorse e progetti</p>
      <Button onClick={login} variant="primary">
        Accedi con Schema31 Identity
      </Button>
    </div>
  );
};

export default Login;
