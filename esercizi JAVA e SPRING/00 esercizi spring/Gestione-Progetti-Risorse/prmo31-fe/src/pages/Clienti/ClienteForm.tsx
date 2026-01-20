import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { Cliente } from '../../types';
import { clienteService } from '../../services/cliente.service';

const ClienteForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<Cliente>({
    nome: '',
    descrizione: '',
    feedbackScore: 0,
    partitaIva: '',
    codiceFiscale: '',
    indirizzo: '',
    citta: '',
    provincia: '',
    cap: '',
    telefono: '',
    email: '',
    pec: '',
    referente: '',
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      loadCliente();
    }
  }, [id]);

  const loadCliente = async () => {
    try {
      setLoading(true);
      const data = await clienteService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento del cliente:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await clienteService.update(Number(id), formData);
      } else {
        await clienteService.create(formData);
      }
      navigate('/clienti');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
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
        <h1>{isEdit ? 'Modifica Cliente' : 'Nuovo Cliente'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/clienti')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Azienda</h3>
          <div className="form-grid">
            <div className="form-group full-width">
              <label>Nome Cliente *</label>
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                required
                maxLength={100}
              />
            </div>

            <div className="form-group full-width">
              <label>Descrizione</label>
              <input
                type="text"
                name="descrizione"
                value={formData.descrizione || ''}
                onChange={handleChange}
                maxLength={255}
              />
            </div>

            <div className="form-group">
              <label>Partita IVA</label>
              <input
                type="text"
                name="partitaIva"
                value={formData.partitaIva || ''}
                onChange={handleChange}
                maxLength={16}
              />
            </div>

            <div className="form-group">
              <label>Codice Fiscale</label>
              <input
                type="text"
                name="codiceFiscale"
                value={formData.codiceFiscale || ''}
                onChange={handleChange}
                maxLength={16}
              />
            </div>

            <div className="form-group">
              <label>Referente</label>
              <input
                type="text"
                name="referente"
                value={formData.referente || ''}
                onChange={handleChange}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>Feedback Score (1-10)</label>
              <input
                type="number"
                name="feedbackScore"
                value={formData.feedbackScore || 0}
                onChange={handleChange}
                min={1}
                max={10}
              />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Contatti</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={formData.email || ''}
                onChange={handleChange}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>PEC</label>
              <input
                type="email"
                name="pec"
                value={formData.pec || ''}
                onChange={handleChange}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>Telefono</label>
              <input
                type="tel"
                name="telefono"
                value={formData.telefono || ''}
                onChange={handleChange}
                maxLength={50}
              />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Indirizzo</h3>
          <div className="form-grid">
            <div className="form-group full-width">
              <label>Indirizzo</label>
              <input
                type="text"
                name="indirizzo"
                value={formData.indirizzo || ''}
                onChange={handleChange}
                maxLength={255}
              />
            </div>

            <div className="form-group">
              <label>Città</label>
              <input
                type="text"
                name="citta"
                value={formData.citta || ''}
                onChange={handleChange}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>Provincia</label>
              <input
                type="text"
                name="provincia"
                value={formData.provincia || ''}
                onChange={handleChange}
                maxLength={2}
              />
            </div>

            <div className="form-group">
              <label>CAP</label>
              <input
                type="text"
                name="cap"
                value={formData.cap || ''}
                onChange={handleChange}
                maxLength={10}
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/clienti')}
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

export default ClienteForm;
