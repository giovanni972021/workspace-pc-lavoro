import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import UtenteDetailModal from '../../components/DetailModal/UtenteDetailModal';
import type { UtenteListDTO, Utente } from '../../types';
import { utenteService } from '../../services/utente.service';
import './Utenti.css';

const UtentiList: React.FC = () => {
  const navigate = useNavigate();
  const [utenti, setUtenti] = useState<UtenteListDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [selectedUtente, setSelectedUtente] = useState<Utente | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = searchTerm.trim()
          ? await utenteService.search(searchTerm)
          : await utenteService.getAll();
        setUtenti(data);
      } catch (error) {
        console.error('Errore nel caricamento degli utenti:', error);
        setError(
          searchTerm.trim()
            ? 'Errore nella ricerca degli utenti. Riprova.'
            : 'Errore nel caricamento degli utenti. Verifica la connessione al server.'
        );
      } finally {
        setLoading(false);
      }
    };

    const timer = setTimeout(() => {
      loadData();
    }, searchTerm ? 300 : 0);

    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo utente?')) {
      return;
    }
    try {
      await utenteService.delete(id);
      setSearchTerm('');
    } catch (error) {
      console.error('Errore nell\'eliminazione:', error);
      setError('Errore nell\'eliminazione dell\'utente.');
    }
  };

  const handleRowClick = async (utente: UtenteListDTO) => {
    try {
      setLoading(true);
      const utenteCompleto = await utenteService.getById(utente.id!);
      setSelectedUtente(utenteCompleto);
      setIsDetailModalOpen(true);
    } catch (error) {
      console.error('Errore nel caricamento del dettaglio:', error);
      setError('Errore nel caricamento del dettaglio utente.');
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'cognome', label: 'Cognome' },
    { key: 'username', label: 'Username' },
    { key: 'email', label: 'Email' },
    { key: 'tipoUtente', label: 'Tipo Utente' },
    { key: 'jobTitle', label: 'Job Title' },
    {
      key: 'attivo',
      label: 'Stato',
      render: (utente: UtenteListDTO) => (
        <span className={`badge ${utente.attivo ? 'badge-success' : 'badge-danger'}`}>
          {utente.attivo ? 'Attivo' : 'Non attivo'}
        </span>
      ),
    },
    {
      key: 'actions',
      label: 'Azioni',
      render: (utente: UtenteListDTO) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/utenti/${utente.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(utente.id!);
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
        <h1>Gestione Utenti</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/utenti/new')}
        >
          Nuovo Utente
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Cerca utente per nome, cognome o email..."
        />
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <Table
        data={utenti}
        columns={columns}
        onRowClick={handleRowClick}
        loading={loading}
      />

      <UtenteDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        utente={selectedUtente}
      />
    </div>
  );
};

export default UtentiList;
