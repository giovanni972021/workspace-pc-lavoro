import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { TipoUtente } from '../../types';
import { tipoUtenteService } from '../../services/tipoUtente.service';

const TipoUtenteForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<TipoUtente>({
    tipo: '',
    descrizione: '',
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      loadTipoUtente();
    }
  }, [id]);

  const loadTipoUtente = async () => {
    try {
      setLoading(true);
      const data = await tipoUtenteService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento del tipo utente:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await tipoUtenteService.update(Number(id), formData);
      } else {
        await tipoUtenteService.create(formData);
      }
      navigate('/configurazione/tipi-utente');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
      alert('Errore nel salvataggio del tipo utente. Verifica i dati inseriti.');
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
        <h1>{isEdit ? 'Modifica Tipo Utente' : 'Nuovo Tipo Utente'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/configurazione/tipi-utente')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Tipo Utente</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Tipo *</label>
              <input
                type="text"
                name="tipo"
                value={formData.tipo}
                onChange={handleChange}
                required
                maxLength={100}
                placeholder="Es: Consultant, Employee, T&M"
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
                placeholder="Inserisci una descrizione del tipo utente..."
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/configurazione/tipi-utente')}
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

export default TipoUtenteForm;
