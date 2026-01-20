import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import Button from '../../components/Button';
import type { JobTitle } from '../../types';
import { jobTitleService } from '../../services/jobTitle.service';

const JobTitlesList: React.FC = () => {
  const navigate = useNavigate();
  const [jobTitles, setJobTitles] = useState<JobTitle[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadJobTitles = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await jobTitleService.getAll();
      setJobTitles(data);
    } catch (error) {
      console.error('Errore nel caricamento dei job titles:', error);
      setError('Errore nel caricamento dei ruoli professionali. Verifica la connessione al server.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadJobTitles();
  }, []);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo ruolo professionale?')) {
      return;
    }
    try {
      await jobTitleService.delete(id);
      loadJobTitles();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
      setError('Errore nell\'eliminazione del ruolo professionale.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (jobTitle: JobTitle) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/configurazione/job-titles/${jobTitle.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(jobTitle.id!);
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
        <h1>Gestione Ruoli Professionali</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/configurazione/job-titles/new')}
        >
          Nuovo Ruolo Professionale
        </Button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <Table
        data={jobTitles}
        columns={columns}
        loading={loading}
      />
    </div>
  );
};

export default JobTitlesList;
