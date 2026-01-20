import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2, Eye, FileText } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import CurriculumDetailModal from '../../components/DetailModal/CurriculumDetailModal';
import type { Curriculum } from '../../types';
import { curriculumService } from '../../services/curriculum.service';
import { exportCurriculumToPDF } from '../../utils/pdfExport';

const CurriculaList: React.FC = () => {
  const navigate = useNavigate();
  const [curricula, setCurricula] = useState<Curriculum[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCurriculum, setSelectedCurriculum] = useState<Curriculum | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  useEffect(() => {
    loadCurricula();
  }, []);

  const loadCurricula = async () => {
    try {
      setLoading(true);
      const data = await curriculumService.getAll();
      setCurricula(data);
    } catch (error) {
      console.error('Errore nel caricamento dei curricula:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo curriculum?')) {
      return;
    }
    try {
      await curriculumService.delete(id);
      loadCurricula();
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
    }
  };

  const handleExportPDF = async (curriculum: Curriculum) => {
    try {
      await exportCurriculumToPDF(curriculum);
    } catch (error) {
      console.error('Errore nell\'export PDF:', error);
    }
  };

  const handleRowClick = async (cv: Curriculum) => {
    try {
      setLoading(true);
      const cvCompleto = await curriculumService.getById(cv.id!);
      setSelectedCurriculum(cvCompleto);
      setIsDetailModalOpen(true);
    } catch (error) {
      console.error('Errore nel caricamento del dettaglio:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredCurricula = curricula.filter((cv) => {
    if (!searchTerm) return true;
    const search = searchTerm.toLowerCase();
    return (
      cv.utente?.nome?.toLowerCase().includes(search) ||
      cv.utente?.cognome?.toLowerCase().includes(search) ||
      cv.utente?.email?.toLowerCase().includes(search)
    );
  });

  const columns = [
    { key: 'id', label: 'ID' },
    {
      key: 'utente',
      label: 'Utente',
      render: (cv: Curriculum) =>
        cv.utente ? `${cv.utente.nome} ${cv.utente.cognome}` : '-',
    },
    {
      key: 'email',
      label: 'Email',
      render: (cv: Curriculum) => cv.utente?.email || '-',
    },
    {
      key: 'skills',
      label: 'Skills',
      render: (cv: Curriculum) => cv.skills?.length || 0,
    },
    {
      key: 'certificazioni',
      label: 'Certificazioni',
      render: (cv: Curriculum) => cv.certificazioni?.length || 0,
    },
    {
      key: 'esperienze',
      label: 'Esperienze',
      render: (cv: Curriculum) => cv.esperienze?.length || 0,
    },
    {
      key: 'actions',
      label: 'Azioni',
      render: (cv: Curriculum) => (
        <div className="action-buttons">
          <button
            className="action-btn view"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/curricula/${cv.id}`);
            }}
            title="Visualizza"
          >
            <Eye size={16} />
          </button>
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/curricula/${cv.id}/edit`);
            }}
            title="Modifica"
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn pdf"
            onClick={(e) => {
              e.stopPropagation();
              handleExportPDF(cv);
            }}
            title="Esporta PDF"
          >
            <FileText size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(cv.id!);
            }}
            title="Elimina"
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
        <h1>Gestione Curricula</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/curricula/new')}
        >
          Nuovo Curriculum
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Cerca curriculum per utente..."
        />
      </div>

      <Table
        data={filteredCurricula}
        columns={columns}
        onRowClick={handleRowClick}
        loading={loading}
      />

      <CurriculumDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        curriculum={selectedCurriculum}
      />
    </div>
  );
};

export default CurriculaList;
