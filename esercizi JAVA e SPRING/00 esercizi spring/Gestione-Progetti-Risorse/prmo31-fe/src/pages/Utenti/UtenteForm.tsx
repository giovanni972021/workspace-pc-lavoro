import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft } from 'lucide-react';
import Button from '../../components/Button';
import type { Utente, Contatto, JobTitle, TipoUtente, Profilo } from '../../types';
import { utenteService } from '../../services/utente.service';
import { jobTitleService } from '../../services/jobTitle.service';
import { tipoUtenteService } from '../../services/tipoUtente.service';
import { profiloService } from '../../services/profilo.service';
import './Utenti.css';

const UtenteForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<Utente>({
    nome: '',
    cognome: '',
    username: '',
    codiceFiscale: '',
    contatti: [],
  });

  // Campi di input temporanei per email e telefono
  const [emailInput, setEmailInput] = useState('');
  const [telefonoInput, setTelefonoInput] = useState('');

  const [loading, setLoading] = useState(false);
  const [jobTitles, setJobTitles] = useState<JobTitle[]>([]);
  const [tipiUtente, setTipiUtente] = useState<TipoUtente[]>([]);
  const [profili, setProfili] = useState<Profilo[]>([]);

  useEffect(() => {
    loadJobTitles();
    loadTipiUtente();
    loadProfili();
    if (isEdit) {
      loadUtente();
    }
  }, [id]);

  const loadJobTitles = async () => {
    try {
      const data = await jobTitleService.getAll();
      setJobTitles(data);
    } catch (error) {
      console.error('Errore nel caricamento dei job title:', error);
    }
  };

  const loadTipiUtente = async () => {
    try {
      const data = await tipoUtenteService.getAll();
      setTipiUtente(data);
    } catch (error) {
      console.error('Errore nel caricamento dei tipi utente:', error);
    }
  };

  const loadProfili = async () => {
    try {
      const data = await profiloService.getAll();
      setProfili(data);
    } catch (error) {
      console.error('Errore nel caricamento dei profili:', error);
    }
  };

  const loadUtente = async () => {
    try {
      setLoading(true);
      const data = await utenteService.getById(Number(id));
      setFormData(data);

      // Estrai email e telefono dai contatti se presenti
      if (data.contatti) {
        const emailContatto = data.contatti.find(c =>
          c.tipoContatto?.nome?.toLowerCase() === 'email'
        );
        const telefonoContatto = data.contatti.find(c =>
          c.tipoContatto?.nome?.toLowerCase() === 'telefono'
        );

        if (emailContatto) setEmailInput(emailContatto.valore);
        if (telefonoContatto) setTelefonoInput(telefonoContatto.valore);
      }
    } catch (error) {
      console.error('Errore nel caricamento dell\'utente:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);

      // Prepara i contatti da salvare
      const contatti: Contatto[] = [];

      if (emailInput.trim()) {
        // Trova il contatto email esistente se in modalità modifica
        const existingEmailContatto = formData.contatti?.find(c =>
          c.tipoContatto?.nome?.toLowerCase() === 'email'
        );

        contatti.push({
          ...(existingEmailContatto?.id && { id: existingEmailContatto.id }),
          riferimento: `email-${formData.username}`,
          valore: emailInput,
          tipoContatto: { nome: 'email', descrizione: 'Email' }
        });
      }

      if (telefonoInput.trim()) {
        // Trova il contatto telefono esistente se in modalità modifica
        const existingTelefonoContatto = formData.contatti?.find(c =>
          c.tipoContatto?.nome?.toLowerCase() === 'telefono'
        );

        contatti.push({
          ...(existingTelefonoContatto?.id && { id: existingTelefonoContatto.id }),
          riferimento: `telefono-${formData.username}`,
          valore: telefonoInput,
          tipoContatto: { nome: 'telefono', descrizione: 'Telefono' }
        });
      }

      const dataToSave = {
        ...formData,
        contatti: contatti.length > 0 ? contatti : undefined
      };

      if (isEdit) {
        await utenteService.update(Number(id), dataToSave);
      } else {
        await utenteService.create(dataToSave);
      }
      navigate('/utenti');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
      alert('Errore nel salvataggio dell\'utente. Verifica i dati inseriti.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleJobTitleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const jobTitleId = e.target.value;
    if (jobTitleId) {
      const selectedJobTitle = jobTitles.find(jt => jt.id === Number(jobTitleId));
      setFormData((prev) => ({
        ...prev,
        jobTitle: selectedJobTitle,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        jobTitle: undefined,
      }));
    }
  };

  const handleTipoUtenteChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const tipoUtenteId = e.target.value;
    if (tipoUtenteId) {
      const selectedTipoUtente = tipiUtente.find(tu => tu.id === Number(tipoUtenteId));
      setFormData((prev) => ({
        ...prev,
        tipoUtente: selectedTipoUtente,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        tipoUtente: undefined,
      }));
    }
  };

  const handleProfiloChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const profiloId = e.target.value;
    if (profiloId) {
      const selectedProfilo = profili.find(p => p.id === Number(profiloId));
      setFormData((prev) => ({
        ...prev,
        profilo: selectedProfilo,
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        profilo: undefined,
      }));
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>{isEdit ? 'Modifica Utente' : 'Nuovo Utente'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/utenti')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Informazioni Personali</h3>
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
              />
            </div>

            <div className="form-group">
              <label>Cognome *</label>
              <input
                type="text"
                name="cognome"
                value={formData.cognome}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>Username *</label>
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={100}
              />
            </div>

            <div className="form-group">
              <label>Codice Fiscale *</label>
              <input
                type="text"
                name="codiceFiscale"
                value={formData.codiceFiscale}
                onChange={handleChange}
                required
                maxLength={16}
                minLength={16}
                pattern="[A-Z0-9]{16}"
                title="Il codice fiscale deve essere di 16 caratteri alfanumerici maiuscoli"
                style={{ textTransform: 'uppercase' }}
              />
            </div>

            <div className="form-group">
              <label>Email *</label>
              <input
                type="email"
                name="email"
                value={emailInput}
                onChange={(e) => setEmailInput(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label>Telefono</label>
              <input
                type="tel"
                name="telefono"
                value={telefonoInput}
                onChange={(e) => setTelefonoInput(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Dati Lavorativi</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Tipo Utente</label>
              <select
                name="tipoUtente"
                value={formData.tipoUtente?.id || ''}
                onChange={handleTipoUtenteChange}
              >
                <option value="">Seleziona un tipo utente</option>
                {tipiUtente.map((tu) => (
                  <option key={tu.id} value={tu.id}>
                    {tu.tipo}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Job Title</label>
              <select
                name="jobTitle"
                value={formData.jobTitle?.id || ''}
                onChange={handleJobTitleChange}
              >
                <option value="">Seleziona un job title</option>
                {jobTitles.map((jt) => (
                  <option key={jt.id} value={jt.id}>
                    {jt.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Profilo</label>
              <select
                name="profilo"
                value={formData.profilo?.id || ''}
                onChange={handleProfiloChange}
              >
                <option value="">Seleziona un profilo</option>
                {profili.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Valutazione (1-10)</label>
              <input
                type="number"
                name="valutazione"
                value={formData.valutazione || ''}
                onChange={handleChange}
                min="1"
                max="10"
                step="1"
              />
            </div>

            <div className="form-group">
              <label>Data Inizio Collaborazione</label>
              <input
                type="date"
                name="dataInizioCollaborazione"
                value={formData.dataInizioCollaborazione || ''}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Data Fine Collaborazione</label>
              <input
                type="date"
                name="dataFineCollaborazione"
                value={formData.dataFineCollaborazione || ''}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-group" style={{ marginTop: '1rem' }}>
            <label>Note</label>
            <textarea
              name="note"
              value={formData.note || ''}
              onChange={handleChange}
              rows={4}
              maxLength={4000}
              placeholder="Inserisci eventuali note sull'utente..."
            />
          </div>

          <p className="form-note">
            <strong>Nota:</strong> Il tipo utente e il profilo devono essere gestiti separatamente.
            La data di nascita è associata al curriculum dell'utente.
          </p>
        </div>

        <div className="form-actions">
          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate('/utenti')}
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

export default UtenteForm;
