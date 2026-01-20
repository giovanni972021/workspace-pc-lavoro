import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import Button from '../../components/Button';
import type { TipoUtente } from '../../types';
import { tipoUtenteService } from '../../services/tipoUtente.service';

const TipiUtenteList: React.FC = () => {
  const navigate = useNavigate();
  const [tipiUtente, setTipiUtente] = useState<TipoUtente[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadTipiUtente = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await tipoUtenteService.getAll();
      setTipiUtente(data);
    } catch (error) {
      console.error('Errore nel caricamento dei tipi utente:', error);
      setError('Errore nel caricamento dei tipi utente. Verifica la connessione al server.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTipiUtente();
  }, []);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo tipo utente?')) {
      return;
    }
    try {
      await tipoUtenteService.delete(id);
      loadTipiUtente();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
      setError('Errore nell\'eliminazione del tipo utente.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'tipo', label: 'Tipo' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (tipoUtente: TipoUtente) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/configurazione/tipi-utente/${tipoUtente.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(tipoUtente.id!);
            }}
          >
            <Trash2 size={16} />
          </button>
        </div>
      ),
    },
  ];

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Gestione Tipi Utente</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/configurazione/tipi-utente/new')}
        >
          Nuovo Tipo Utente
        </Button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <Table
        data={tipiUtente}
        columns={columns}
        loading={loading}
      />
    </div>
  );
};

export default TipiUtenteList;
