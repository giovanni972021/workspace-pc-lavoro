import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft, Plus, Trash2 } from 'lucide-react';
import Button from '../../components/Button';
import type { Progetto, StatoProgetto, NumberJobTitle, JobTitle } from '../../types';
import { progettoService } from '../../services/progetto.service';
import { statoProgettoService } from '../../services/statoProgetto.service';
import { jobTitleService } from '../../services/jobTitle.service';

const ProgettoForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [statiProgetto, setStatiProgetto] = useState<StatoProgetto[]>([]);
  const [jobTitles, setJobTitles] = useState<JobTitle[]>([]);
  const [formData, setFormData] = useState<Progetto>({
    nome: '',
    descrizione: '',
    dataInizioPresunta: '',
    dataInizioEffettiva: '',
    dataFinePresunta: '',
    dataFineEffettiva: '',
    budgetPreventivato: 0,
    budgetEffettivo: 0,
    stato: undefined,
    priorita: 0,
    fattibilita: 0,
    numberJobTitles: [],
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadStatiProgetto();
    loadJobTitles();
    if (isEdit) {
      loadProgetto();
    }
  }, [id]);

  const loadStatiProgetto = async () => {
    try {
      const data = await statoProgettoService.getAll();
      setStatiProgetto(data);
    } catch (error) {
      console.error('Errore nel caricamento degli stati progetto:', error);
    }
  };

  const loadJobTitles = async () => {
    try {
      const data = await jobTitleService.getAll();
      setJobTitles(data);
    } catch (error) {
      console.error('Errore nel caricamento dei job titles:', error);
    }
  };

  const loadProgetto = async () => {
    try {
      setLoading(true);
      const data = await progettoService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento del progetto:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEdit) {
        await progettoService.update(Number(id), formData);
      } else {
        await progettoService.create(formData);
      }
      navigate('/progetti');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value,
    }));
  };

  const addNumberJobTitle = () => {
    setFormData((prev) => ({
      ...prev,
      numberJobTitles: [
        ...(prev.numberJobTitles || []),
        { number: 1, jobTitle: undefined },
      ],
    }));
  };

  const removeNumberJobTitle = (index: number) => {
    setFormData((prev) => ({
      ...prev,
      numberJobTitles: prev.numberJobTitles?.filter((_, i) => i !== index) || [],
    }));
  };

  const updateNumberJobTitle = (index: number, field: keyof NumberJobTitle, value: any) => {
    setFormData((prev) => ({
      ...prev,
      numberJobTitles: prev.numberJobTitles?.map((njt, i) =>
        i === index ? { ...njt, [field]: value } : njt
      ) || [],
    }));
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>{isEdit ? 'Modifica Progetto' : 'Nuovo Progetto'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/progetti')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Generali</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Nome *</label>
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Stato</label>
              <select
                name="stato"
                value={formData.stato?.id || ''}
                onChange={(e) =>
                  setFormData((prev) => ({
                    ...prev,
                    stato: statiProgetto.find((s) => s.id === Number(e.target.value)),
                  }))
                }
              >
                <option value="">Seleziona stato</option>
                {statiProgetto.map((stato) => (
                  <option key={stato.id} value={stato.id}>
                    {stato.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group full-width">
              <label>Descrizione *</label>
              <textarea
                name="descrizione"
                value={formData.descrizione || ''}
                onChange={handleChange}
                required
              />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Date e Budget</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Data Inizio Presunta</label>
              <input
                type="date"
                name="dataInizioPresunta"
                value={formData.dataInizioPresunta || ''}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Data Fine Prevista</label>
              <input
                type="date"
                name="dataFinePresunta"
                value={formData.dataFinePresunta || ''}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Data Inizio Effettiva</label>
              <input
                type="date"
                name="dataInizioEffettiva"
                value={formData.dataInizioEffettiva || ''}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Data Fine Effettiva</label>
              <input
                type="date"
                name="dataFineEffettiva"
                value={formData.dataFineEffettiva || ''}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Budget Preventivato (€)</label>
              <input
                type="number"
                name="budgetPreventivato"
                value={formData.budgetPreventivato || ''}
                onChange={handleChange}
                step="0.01"
              />
            </div>

            <div className="form-group">
              <label>Budget Effettivo (€)</label>
              <input
                type="number"
                name="budgetEffettivo"
                value={formData.budgetEffettivo || ''}
                onChange={handleChange}
                step="0.01"
              />
            </div>

            <div className="form-group">
              <label>Priorità (0-100)</label>
              <input
                type="number"
                name="priorita"
                value={formData.priorita || 0}
                onChange={handleChange}
                min="0"
                max="100"
              />
            </div>

            <div className="form-group">
              <label>Fattibilità (0-100)</label>
              <input
                type="number"
                name="fattibilita"
                value={formData.fattibilita || 0}
                onChange={handleChange}
                min="0"
                max="100"
              />
            </div>
          </div>
        </div>

        <div className="form-section">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h3>Figure Richieste</h3>
            <Button
              type="button"
              variant="primary"
              icon={<Plus size={16} />}
              onClick={addNumberJobTitle}
            >
              Aggiungi Figura
            </Button>
          </div>

          {formData.numberJobTitles && formData.numberJobTitles.length > 0 ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {formData.numberJobTitles.map((njt, index) => (
                <div key={index} style={{
                  display: 'grid',
                  gridTemplateColumns: '1fr 150px 50px',
                  gap: '1rem',
                  alignItems: 'end',
                  padding: '1rem',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  backgroundColor: 'var(--surface-color)'
                }}>
                  <div className="form-group" style={{ margin: 0 }}>
                    <label>Ruolo Professionale *</label>
                    <select
                      value={njt.jobTitle?.id || ''}
                      onChange={(e) => {
                        const selectedJobTitle = jobTitles.find(
                          (jt) => jt.id === Number(e.target.value)
                        );
                        updateNumberJobTitle(index, 'jobTitle', selectedJobTitle);
                      }}
                      required
                    >
                      <option value="">Seleziona ruolo</option>
                      {jobTitles.map((jobTitle) => (
                        <option key={jobTitle.id} value={jobTitle.id}>
                          {jobTitle.nome}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group" style={{ margin: 0 }}>
                    <label>Numero *</label>
                    <input
                      type="number"
                      min="1"
                      value={njt.number || 1}
                      onChange={(e) =>
                        updateNumberJobTitle(index, 'number', Number(e.target.value))
                      }
                      required
                    />
                  </div>

                  <button
                    type="button"
                    className="action-btn delete"
                    onClick={() => removeNumberJobTitle(index)}
                    style={{ height: '40px' }}
                    title="Rimuovi figura"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)', fontStyle: 'italic' }}>
              Nessuna figura richiesta. Clicca su "Aggiungi Figura" per aggiungerne una.
            </p>
          )}
        </div>

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/progetti')}>
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

export default ProgettoForm;
