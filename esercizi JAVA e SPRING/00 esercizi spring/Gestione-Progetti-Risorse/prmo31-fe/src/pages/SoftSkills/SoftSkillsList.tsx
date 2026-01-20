import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import Modal from '../../components/Modal';
import type { SoftSkill } from '../../types';
import { softSkillService } from '../../services/softSkill.service';

const SoftSkillsList: React.FC = () => {
  const [softSkills, setSoftSkills] = useState<SoftSkill[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSoftSkill, setEditingSoftSkill] = useState<SoftSkill | null>(null);
  const [formData, setFormData] = useState<SoftSkill>({
    nome: '',
    descrizione: '',
  });

  useEffect(() => {
    loadSoftSkills();
  }, []);

  const loadSoftSkills = async () => {
    try {
      setLoading(true);
      const data = await softSkillService.getAll();
      setSoftSkills(data);
    } catch (error) {
      console.error('Errore:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        softSkillService.search(searchTerm).then(setSoftSkills);
      } else {
        loadSoftSkills();
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questa soft skill?')) return;
    try {
      await softSkillService.delete(id);
      loadSoftSkills();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingSoftSkill?.id) {
        await softSkillService.update(editingSoftSkill.id, formData);
      } else {
        await softSkillService.create(formData);
      }
      setIsModalOpen(false);
      setEditingSoftSkill(null);
      setFormData({ nome: '', descrizione: '' });
      loadSoftSkills();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const openEditModal = (softSkill: SoftSkill) => {
    setEditingSoftSkill(softSkill);
    setFormData(softSkill);
    setIsModalOpen(true);
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (softSkill: SoftSkill) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              openEditModal(softSkill);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(softSkill.id!);
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
        <h1>Gestione Soft Skills</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => {
            setEditingSoftSkill(null);
            setFormData({ nome: '', descrizione: '' });
            setIsModalOpen(true);
          }}
        >
          Nuova Soft Skill
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar value={searchTerm} onChange={setSearchTerm} placeholder="Cerca soft skill..." />
      </div>

      <Table data={softSkills} columns={columns} loading={loading} />

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingSoftSkill ? 'Modifica Soft Skill' : 'Nuova Soft Skill'}
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
            <label>Descrizione *</label>
            <textarea
              value={formData.descrizione || ''}
              onChange={(e) => setFormData({ ...formData, descrizione: e.target.value })}
              required
            />
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default SoftSkillsList;
