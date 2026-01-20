import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import ClienteDetailModal from '../../components/DetailModal/ClienteDetailModal';
import type { Cliente } from '../../types';
import { clienteService } from '../../services/cliente.service';

const ClientiList: React.FC = () => {
  const navigate = useNavigate();
  const [clienti, setClienti] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCliente, setSelectedCliente] = useState<Cliente | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  useEffect(() => {
    loadClienti();
  }, []);

  const loadClienti = async () => {
    try {
      setLoading(true);
      const data = await clienteService.getAll();
      setClienti(data);
    } catch (error) {
      console.error('Errore:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        clienteService.search(searchTerm).then(setClienti);
      } else {
        loadClienti();
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questo cliente?')) return;
    try {
      await clienteService.delete(id);
      loadClienti();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const handleRowClick = async (cliente: Cliente) => {
    try {
      setLoading(true);
      const clienteCompleto = await clienteService.getById(cliente.id!);
      setSelectedCliente(clienteCompleto);
      setIsDetailModalOpen(true);
    } catch (error) {
      console.error('Errore nel caricamento del dettaglio:', error);
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome Cliente' },
    { key: 'partitaIva', label: 'P.IVA' },
    { key: 'email', label: 'Email' },
    { key: 'telefono', label: 'Telefono' },
    { key: 'citta', label: 'Città' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (cliente: Cliente) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/clienti/${cliente.id}/edit`);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(cliente.id!);
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
        <h1>Gestione Clienti</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => navigate('/clienti/new')}
        >
          Nuovo Cliente
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Cerca cliente..."
        />
      </div>

      <Table
        data={clienti}
        columns={columns}
        onRowClick={handleRowClick}
        loading={loading}
      />

      <ClienteDetailModal
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
        cliente={selectedCliente}
      />
    </div>
  );
};

export default ClientiList;
