import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { Profilo } from '../../types';
import { profiloService } from '../../services/profilo.service';

const ProfiloForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<Profilo>({
    nome: '',
    descrizione: '',
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      loadProfilo();
    }
  }, [id]);

  const loadProfilo = async () => {
    try {
      setLoading(true);
      const data = await profiloService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento del profilo:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await profiloService.update(Number(id), formData);
      } else {
        await profiloService.create(formData);
      }
      navigate('/configurazione/profili');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
      alert('Errore nel salvataggio del profilo. Verifica i dati inseriti.');
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
        <h1>{isEdit ? 'Modifica Profilo' : 'Nuovo Profilo'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/configurazione/profili')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Profilo</h3>
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
                placeholder="Es: Amministratore, Manager, Utente"
              />
            </div>

            <div className="form-group">
              <label>Descrizione *</label>
              <input
                type="text"
                name="descrizione"
                value={formData.descrizione || ''}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={100}
                placeholder="Descrizione del profilo"
              />
            </div>
          </div>

          <p className="form-note" style={{ marginTop: '1rem' }}>
            <strong>Nota:</strong> I ruoli associati al profilo devono essere gestiti separatamente nella sezione di modifica avanzata.
          </p>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/configurazione/profili')}
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

export default ProfiloForm;
