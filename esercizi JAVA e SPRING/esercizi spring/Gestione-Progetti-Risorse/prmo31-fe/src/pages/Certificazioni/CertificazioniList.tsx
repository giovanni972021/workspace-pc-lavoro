import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import Modal from '../../components/Modal';
import CertificazioneDetailModal from '../../components/DetailModal/CertificazioneDetailModal';
import type { Certificazione } from '../../types';
import { certificazioneService } from '../../services/certificazione.service';

const CertificazioniList: React.FC = () => {
  const [certificazioni, setCertificazioni] = useState<Certificazione[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCert, setEditingCert] = useState<Certificazione | null>(null);
  const [formData, setFormData] = useState<Certificazione>({
    nome: '',
    descrizione: '',
    conseguita: '',
  });
  const [selectedCertificazione, setSelectedCertificazione] = useState<Certificazione | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  useEffect(() => {
    loadCertificazioni();
  }, []);

  const loadCertificazioni = async () => {
    try {
      setLoading(true);
      const data = await certificazioneService.getAll();
      setCertificazioni(data);
    } catch (error) {
      console.error('Errore:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        certificazioneService.search(searchTerm).then(setCertificazioni);
      } else {
        loadCertificazioni();
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questa certificazione?')) return;
    try {
      await certificazioneService.delete(id);
      loadCertificazioni();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCert?.id) {
        await certificazioneService.update(editingCert.id, formData);
      } else {
        await certificazioneService.create(formData);
      }
      setIsModalOpen(false);
      setEditingCert(null);
      setFormData({ nome: '', descrizione: '', conseguita: '' });
      loadCertificazioni();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const openEditModal = (cert: Certificazione) => {
    setEditingCert(cert);
    setFormData(cert);
    setIsModalOpen(true);
  };

  const handleRowClick = async (cert: Certificazione) => {
    try {
      setLoading(true);
      const certCompleta = await certificazioneService.getById(cert.id!);
      setSelectedCertificazione(certCompleta);
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
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'conseguita',
      label: 'Conseguimento',
      render: (cert: Certificazione) =>
        cert.conseguita ? new Date(cert.conseguita).toLocaleDateString('it-IT') : '-',
    },
    {
      key: 'actions',
      label: 'Azioni',
      render: (cert: Certificazione) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              openEditModal(cert);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(cert.id!);
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
        <h1>Gestione Certificazioni</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => {
            setEditingCert(null);
            setFormData({ nome: '', descrizione: '', conseguita: '' });
            setIsModalOpen(true);
          }}
        >
          Nuova Certificazione
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar value={searchTerm} onChange={setSearchTerm} placeholder="Cerca certificazione..." />
      </div>

      <Table data={certificazioni} columns={columns} onRowClick={handleRowClick} loading={loading} />

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingCert ? 'Modifica Certificazione' : 'Nuova Certificazione'}
        footer={
          <>
            <Button variant="secondary" onClick={() => setIsModalOpen(false)}>
              Annulla
            </Button>
            <Button variant="primary" onClick={handleSubmit}>
              Salva
            </Button>
          </>
        }
      >
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nome *</label>
            <input
              type="text"
              value={formData.nome}
              onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Descrizione</label>
            <textarea
              value={formData.descrizione || ''}
              onChange={(e) => setFormData({ ...formData, descrizione: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>Conseguimento</label>
            <input
              type="date"
              value={formData.conseguita || ''}
              onChange={(e) => setFormData({ ...formData, conseguita: e.target.value })}
            />
          </div>
        </form>
      </Modal>

      <CertificazioneDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        certificazione={selectedCertificazione}
      />
    </div>
  );
};

export default CertificazioniList;
