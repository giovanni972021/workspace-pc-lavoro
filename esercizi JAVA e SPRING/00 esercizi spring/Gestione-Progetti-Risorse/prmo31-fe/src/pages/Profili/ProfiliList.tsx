import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import Button from '../../components/Button';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import RoleGuard from '../../components/RoleGuard';
import type { Profilo } from '../../types';
import { profiloService } from '../../services/profilo.service';

const ProfiliList: React.FC = () => {
  const navigate = useNavigate();
  const [profili, setProfili] = useState<Profilo[]>([]);
  const [filteredProfili, setFilteredProfili] = useState<Profilo[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadProfili();
  }, []);

  useEffect(() => {
    if (searchTerm) {
      const filtered = profili.filter(
        (p) =>
          p.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.descrizione?.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredProfili(filtered);
    } else {
      setFilteredProfili(profili);
    }
  }, [searchTerm, profili]);

  const loadProfili = async () => {
    try {
      setLoading(true);
      const data = await profiloService.getAll();
      setProfili(data);
      setFilteredProfili(data);
    } catch (error) {
      console.error('Errore nel caricamento dei profili:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Sei sicuro di voler eliminare questo profilo?')) {
      try {
        await profiloService.delete(id);
        loadProfili();
      } catch (error) {
        console.error('Errore durante l\'eliminazione:', error);
        alert('Errore durante l\'eliminazione del profilo');
      }
    }
  };

  const columns = [
    {
      key: 'nome',
      label: 'Nome',
      sortable: true,
    },
    {
      key: 'descrizione',
      label: 'Descrizione',
      sortable: true,
    },
    {
      key: 'ruoli',
      label: 'Ruoli',
      render: (profilo: Profilo) => (
        <span>
          {profilo.ruoli?.length || 0} ruol{profilo.ruoli?.length === 1 ? 'o' : 'i'}
        </span>
      ),
    },
    {
      key: 'actions',
      label: 'Azioni',
      render: (profilo: Profilo) => (
        <RoleGuard superUserOnly>
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <Button
              variant="secondary"
              size="small"
              icon={<Pencil size={16} />}
              onClick={() => navigate(`/configurazione/profili/${profilo.id}/edit`)}
            >
              Modifica
            </Button>
            <Button
              variant="danger"
              size="small"
              icon={<Trash2 size={16} />}
              onClick={() => handleDelete(profilo.id!)}
            >
              Elimina
            </Button>
          </div>
        </RoleGuard>
      ),
    },
  ];

  if (loading) {
    return <div>Caricamento...</div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Profili</h1>
        <RoleGuard superUserOnly>
          <Button
            variant="primary"
            icon={<Plus size={20} />}
            onClick={() => navigate('/configurazione/profili/new')}
          >
            Nuovo Profilo
          </Button>
        </RoleGuard>
      </div>

      <SearchBar
        value={searchTerm}
        onChange={setSearchTerm}
        placeholder="Cerca profili..."
      />

      <Table
        columns={columns}
        data={filteredProfili}
        emptyMessage="Nessun profilo trovato"
      />
    </div>
  );
};

export default ProfiliList;
