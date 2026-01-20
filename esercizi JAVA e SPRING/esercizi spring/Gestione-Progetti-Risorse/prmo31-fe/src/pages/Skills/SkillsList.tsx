import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/Table';
import SearchBar from '../../components/SearchBar';
import Button from '../../components/Button';
import Modal from '../../components/Modal';
import type { Skill } from '../../types';
import { skillService } from '../../services/skill.service';

const SkillsList: React.FC = () => {
  const [skills, setSkills] = useState<Skill[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSkill, setEditingSkill] = useState<Skill | null>(null);
  const [formData, setFormData] = useState<Skill>({
    nome: '',
    descrizione: '',
  });

  useEffect(() => {
    loadSkills();
  }, []);

  const loadSkills = async () => {
    try {
      setLoading(true);
      const data = await skillService.getAll();
      setSkills(data);
    } catch (error) {
      console.error('Errore:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        skillService.search(searchTerm).then(setSkills);
      } else {
        loadSkills();
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const handleDelete = async (id: number) => {
    if (!window.confirm('Sei sicuro di voler eliminare questa skill?')) return;
    try {
      await skillService.delete(id);
      loadSkills();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingSkill?.id) {
        await skillService.update(editingSkill.id, formData);
      } else {
        await skillService.create(formData);
      }
      setIsModalOpen(false);
      setEditingSkill(null);
      setFormData({ nome: '', descrizione: '' });
      loadSkills();
    } catch (error) {
      console.error('Errore:', error);
    }
  };

  const openEditModal = (skill: Skill) => {
    setEditingSkill(skill);
    setFormData(skill);
    setIsModalOpen(true);
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'descrizione', label: 'Descrizione' },
    {
      key: 'actions',
      label: 'Azioni',
      render: (skill: Skill) => (
        <div className="action-buttons">
          <button
            className="action-btn edit"
            onClick={(e) => {
              e.stopPropagation();
              openEditModal(skill);
            }}
          >
            <Edit size={16} />
          </button>
          <button
            className="action-btn delete"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(skill.id!);
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
        <h1>Gestione Skills</h1>
        <Button
          variant="primary"
          icon={<Plus size={20} />}
          onClick={() => {
            setEditingSkill(null);
            setFormData({ nome: '', descrizione: '' });
            setIsModalOpen(true);
          }}
        >
          Nuova Skill
        </Button>
      </div>

      <div className="page-toolbar">
        <SearchBar value={searchTerm} onChange={setSearchTerm} placeholder="Cerca skill..." />
      </div>

      <Table data={skills} columns={columns} loading={loading} />

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingSkill ? 'Modifica Skill' : 'Nuova Skill'}
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

export default SkillsList;
