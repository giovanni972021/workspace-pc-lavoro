import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { StatoProgetto } from '../../types';
import { statoProgettoService } from '../../services/statoProgetto.service';

const StatoProgettoForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<StatoProgetto>({
    nome: '',
    descrizione: '',
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      loadStatoProgetto();
    }
  }, [id]);

  const loadStatoProgetto = async () => {
    try {
      setLoading(true);
      const data = await statoProgettoService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento dello stato progetto:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await statoProgettoService.update(Number(id), formData);
      } else {
        await statoProgettoService.create(formData);
      }
      navigate('/configurazione/stati-progetto');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
      alert('Errore nel salvataggio dello stato progetto. Verifica i dati inseriti.');
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
        <h1>{isEdit ? 'Modifica Stato Progetto' : 'Nuovo Stato Progetto'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/configurazione/stati-progetto')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Stato Progetto</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Nome *</label>
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                required
                maxLength={100}
                placeholder="Es: In Corso, Completato, Sospeso"
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
                placeholder="Inserisci una descrizione dello stato progetto..."
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/configurazione/stati-progetto')}
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

export default StatoProgettoForm;
