import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import Button from '../../components/Button';
import type { TipoContatto } from '../../types';
import { tipoContattoService } from '../../services/tipoContatto.service';

const TipiContattoList: React.FC = () => {
  const navigate = useNavigate();
  const [tipiContatto, setTipiContatto] = useState<TipoContatto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadTipiContatto = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await tipoContattoService.getAll();
      setTipiContatto(data);
    } catch (error) {
      console.error('Errore nel caricamento dei tipi contatto:', error);
      setError('Errore nel caricamento dei tipi contatto. Verifica la connessione al server.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTipiContatto();
  }, []);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo tipo contatto?')) {
      return;
    }
    try {
      await tipoContattoService.delete(id);
      loadTipiContatto();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
      setError('Errore nell\'eliminazione del tipo contatto.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (tipoContatto: TipoContatto) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/configurazione/tipi-contatto/${tipoContatto.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(tipoContatto.id!);
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
        <h1>Gestione Tipi Contatto</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/configurazione/tipi-contatto/new')}
        >
          Nuovo Tipo Contatto
        </Button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <Table
        data={tipiContatto}
        columns={columns}
        loading={loading}
      />
    </div>
  );
};

export default TipiContattoList;
