import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import ProgettoDetailModal from '../../components/DetailModal/ProgettoDetailModal';
import type { Progetto } from '../../types';
import { progettoService } from '../../services/progetto.service';

const ProgettiList: React.FC = () => {
  const navigate = useNavigate();
  const [progetti, setProgetti] = useState<Progetto[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedProgetto, setSelectedProgetto] = useState<Progetto | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  useEffect(() => {
    loadProgetti();
  }, []);

  const loadProgetti = async () => {
    try {
      setLoading(true);
      const data = await progettoService.getAll();
      setProgetti(data);
    } catch (error) {
      console.error('Errore nel caricamento dei progetti:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        progettoService.search(searchTerm).then(setProgetti);
      } else {
        loadProgetti();
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo progetto?')) {
      return;
    }
    try {
      await progettoService.delete(id);
      loadProgetti();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
    }
  };

  const handleRowClick = async (progetto: Progetto) => {
    try {
      setLoading(true);
      const progettoCompleto = await progettoService.getById(progetto.id!);
      setSelectedProgetto(progettoCompleto);
      setIsDetailModalOpen(true);
    } catch (error) {
      console.error('Errore nel caricamento del dettaglio:', error);
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    {
      key: 'stato',
      label: 'Stato',
      render: (progetto: Progetto) => progetto.stato?.nome || '-',
    },
    {
      key: 'dataInizioPresunta',
      label: 'Data Inizio Presunta',
      render: (progetto: Progetto) =>
        progetto.dataInizioPresunta
          ? new Date(progetto.dataInizioPresunta).toLocaleDateString('it-IT')
          : '-',
    },
    {
      key: 'dataFinePresunta',
      label: 'Data Fine Prevista',
      render: (progetto: Progetto) =>
        progetto.dataFinePresunta
          ? new Date(progetto.dataFinePresunta).toLocaleDateString('it-IT')
          : '-',
    },
    {
      key: 'budgetPreventivato',
      label: 'Budget Preventivato',
      render: (progetto: Progetto) =>
        progetto.budgetPreventivato ? `€ ${progetto.budgetPreventivato.toLocaleString('it-IT')}` : '-',
    },
    {
      key: 'priorita',
      label: 'Priorità',
      render: (progetto: Progetto) => progetto.priorita || '-',
    },
    {
      key: 'actions',
      label: 'Azioni',
      render: (progetto: Progetto) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/progetti/${progetto.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(progetto.id!);
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
        <h1>Gestione Progetti</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/progetti/new')}
        >
          Nuovo Progetto
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Cerca progetto per nome..."
        />
      </div>

      <Table
        data={progetti}
        columns={columns}
        onRowClick={handleRowClick}
        loading={loading}
      />

      <ProgettoDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        progetto={selectedProgetto}
      />
    </div>
  );
};

export default ProgettiList;
