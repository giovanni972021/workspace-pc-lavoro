import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { JobTitle } from '../../types';
import { jobTitleService } from '../../services/jobTitle.service';

const JobTitleForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<JobTitle>({
    nome: '',
    descrizione: '',
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      loadJobTitle();
    }
  }, [id]);

  const loadJobTitle = async () => {
    try {
      setLoading(true);
      const data = await jobTitleService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento del job title:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await jobTitleService.update(Number(id), formData);
      } else {
        await jobTitleService.create(formData);
      }
      navigate('/configurazione/job-titles');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
      alert('Errore nel salvataggio del ruolo professionale. Verifica i dati inseriti.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>{isEdit ? 'Modifica Ruolo Professionale' : 'Nuovo Ruolo Professionale'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/configurazione/job-titles')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Ruolo Professionale</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Nome *</label>
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={100}
                placeholder="Es: Senior Developer, Project Manager"
              />
            </div>

            <div className="form-group">
              <label>Descrizione</label>
              <textarea
                name="descrizione"
                value={formData.descrizione || ''}
                onChange={handleChange}
                rows={4}
                maxLength={500}
                placeholder="Inserisci una descrizione del ruolo professionale..."
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/configurazione/job-titles')}
          >
            Annulla
          </Button>
          <Button
            type="submit"
            variant="primary"
            icon={<Save size={20} />}
            disabled={loading}
          >
            {loading ? 'Salvataggio...' : 'Salva'}
          </Button>
        </div>
      </form>
    </div>
  );
};

export default JobTitleForm;
