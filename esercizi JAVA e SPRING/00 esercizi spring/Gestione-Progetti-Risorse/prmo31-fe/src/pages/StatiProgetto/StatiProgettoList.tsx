import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import Button from '../../components/Button';
import type { StatoProgetto } from '../../types';
import { statoProgettoService } from '../../services/statoProgetto.service';

const StatiProgettoList: React.FC = () => {
  const navigate = useNavigate();
  const [statiProgetto, setStatiProgetto] = useState<StatoProgetto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadStatiProgetto = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await statoProgettoService.getAll();
      setStatiProgetto(data);
    } catch (error) {
      console.error('Errore nel caricamento degli stati progetto:', error);
      setError('Errore nel caricamento degli stati progetto. Verifica la connessione al server.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStatiProgetto();
  }, []);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo stato progetto?')) {
      return;
    }
    try {
      await statoProgettoService.delete(id);
      loadStatiProgetto();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
      setError('Errore nell\'eliminazione dello stato progetto.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (statoProgetto: StatoProgetto) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/configurazione/stati-progetto/${statoProgetto.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(statoProgetto.id!);
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
        <h1>Gestione Stati Progetto</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/configurazione/stati-progetto/new')}
        >
          Nuovo Stato Progetto
        </Button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <Table
        data={statiProgetto}
        columns={columns}
        loading={loading}
      />
    </div>
  );
};

export default StatiProgettoList;
