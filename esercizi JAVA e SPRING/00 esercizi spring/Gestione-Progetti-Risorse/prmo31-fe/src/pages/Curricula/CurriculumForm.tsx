import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Save, ArrowLeft, Plus, X } from 'lucide-react';
import Button from '../../components/Button';
import type { Curriculum, Utente, Skill, SoftSkill, Certificazione, Esperienza, LevelSkill, LevelSoftSkill } from '../../types';
import { curriculumService } from '../../services/curriculum.service';
import { utenteService } from '../../services/utente.service';
import { skillService } from '../../services/skill.service';
import { softSkillService } from '../../services/softSkill.service';
import { certificazioneService } from '../../services/certificazione.service';
import '../Utenti/Utenti.css';

const CurriculumForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [loading, setLoading] = useState(false);
  const [utenti, setUtenti] = useState<Utente[]>([]);
  const [availableSkills, setAvailableSkills] = useState<Skill[]>([]);
  const [availableSoftSkills, setAvailableSoftSkills] = useState<SoftSkill[]>([]);
  const [availableCerts, setAvailableCerts] = useState<Certificazione[]>([]);

  const [formData, setFormData] = useState<Curriculum>({
    skills: [],
    softSkills: [],
    certificazioni: [],
    esperienze: [],
    incarichi: [],
  });

  const [newEsperienza, setNewEsperienza] = useState<Esperienza>({
    azienda: '',
    ruolo: '',
    dataInizio: '',
    descrizione: '',
    attivitaSvolte: '',
    tecnologie: '',
  });

  const [showNewSkillForm, setShowNewSkillForm] = useState(false);
  const [newSkill, setNewSkill] = useState<Skill>({ nome: '', descrizione: '' });

  const [showNewSoftSkillForm, setShowNewSoftSkillForm] = useState(false);
  const [newSoftSkill, setNewSoftSkill] = useState<SoftSkill>({ nome: '', descrizione: '' });

  const [showNewCertForm, setShowNewCertForm] = useState(false);
  const [newCert, setNewCert] = useState<Certificazione>({ nome: '', descrizione: '', conseguita: '' });

  useEffect(() => {
    loadData();
    if (isEdit) {
      loadCurriculum();
    }
  }, [id]);

  const loadData = async () => {
    try {
      const [utentiData, skillsData, softSkillsData, certsData] = await Promise.all([
        utenteService.getAll(),
        skillService.getAll(),
        softSkillService.getAll(),
        certificazioneService.getAll(),
      ]);
      setUtenti(utentiData);
      setAvailableSkills(skillsData);
      setAvailableSoftSkills(softSkillsData);
      setAvailableCerts(certsData);
    } catch (error) {
      console.error('Errore nel caricamento dati:', error);
    }
  };

  const loadCurriculum = async () => {
    try {
      setLoading(true);
      const data = await curriculumService.getById(Number(id));
      setFormData(data);
    } catch (error) {
      console.error('Errore nel caricamento curriculum:', error);
    } finally {
      setLoading(false);
    }
  };

  // Sanitizza i dati rimuovendo campi che non devono essere inviati al backend
  const sanitizeForSubmit = (data: Curriculum): Curriculum => {
    const { commonEntity, ...rest } = data;

    return {
      ...rest,
      // Sanitizza le skills rimuovendo common e mantenendo esplicitamente id
      skills: data.skills?.map(({ common: _, ...ls }) => ({
        id: ls.id,
        livello: ls.livello,
        skill: ls.skill ? { id: ls.skill.id, nome: ls.skill.nome, descrizione: ls.skill.descrizione } : ls.skill,
      })),
      // Sanitizza le softSkills rimuovendo common e mantenendo esplicitamente id
      softSkills: data.softSkills?.map(({ common: _, ...lss }) => ({
        id: lss.id,
        livello: lss.livello,
        softSkill: lss.softSkill ? { id: lss.softSkill.id, nome: lss.softSkill.nome, descrizione: lss.softSkill.descrizione } : lss.softSkill,
      })),
      // Sanitizza le certificazioni rimuovendo commonEntity e mantenendo esplicitamente id
      certificazioni: data.certificazioni?.map(({ commonEntity: _, ...cert }) => ({
        id: cert.id,
        nome: cert.nome,
        descrizione: cert.descrizione,
        conseguita: cert.conseguita,
      })),
      // Sanitizza le esperienze rimuovendo common e mantenendo esplicitamente tutti i campi incluso id
      esperienze: data.esperienze?.map(({ common: _, ...exp }) => ({
        id: exp.id,
        azienda: exp.azienda,
        ruolo: exp.ruolo,
        dataInizio: exp.dataInizio,
        dataFine: exp.dataFine,
        descrizione: exp.descrizione,
        attivitaSvolte: exp.attivitaSvolte,
        tecnologie: exp.tecnologie,
      })),
      // Sanitizza gli incarichi rimuovendo common e mantenendo esplicitamente id
      incarichi: data.incarichi?.map(({ common: _, ...inc }) => ({
        id: inc.id,
        ruolo: inc.ruolo,
        dataInizio: inc.dataInizio,
        dataFine: inc.dataFine,
        percentualeImpegno: inc.percentualeImpegno,
        descrizione: inc.descrizione,
        progetto: inc.progetto ? { id: inc.progetto.id, nome: inc.progetto.nome, descrizione: inc.progetto.descrizione } : undefined,
      })),
      // Rimuovi commonEntity dall'utente se presente
      utente: data.utente ? { id: data.utente.id, nome: data.utente.nome, cognome: data.utente.cognome, username: data.utente.username, codiceFiscale: data.utente.codiceFiscale } : undefined,
    };
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      const sanitizedData = sanitizeForSubmit(formData);
      console.log('Dati da inviare:', JSON.stringify(sanitizedData, null, 2));
      if (isEdit) {
        await curriculumService.update(Number(id), sanitizedData);
      } else {
        await curriculumService.create(sanitizedData);
      }
      navigate('/curricula');
    } catch (error) {
      console.error('Errore nel salvataggio:', error);
    } finally {
      setLoading(false);
    }
  };

  const addSkill = (skillId: number, livello: number = 5) => {
    const skill = availableSkills.find((s) => s.id === skillId);
    if (skill && !formData.skills?.some((ls) => ls.skill.id === skillId)) {
      const levelSkill: LevelSkill = {
        skill: skill,
        livello: livello,
      };
      setFormData({
        ...formData,
        skills: [...(formData.skills || []), levelSkill],
      });
    }
  };

  const removeSkill = (skillId: number) => {
    setFormData({
      ...formData,
      skills: formData.skills?.filter((ls) => ls.skill.id !== skillId) || [],
    });
  };

  const updateSkillLevel = (skillId: number, livello: number) => {
    setFormData({
      ...formData,
      skills: formData.skills?.map((ls) =>
        ls.skill.id === skillId ? { ...ls, livello } : ls
      ) || [],
    });
  };

  const addSoftSkill = (softSkillId: number, livello: number = 5) => {
    const softSkill = availableSoftSkills.find((s) => s.id === softSkillId);
    if (softSkill && !formData.softSkills?.some((lss) => lss.softSkill.id === softSkillId)) {
      const levelSoftSkill: LevelSoftSkill = {
        softSkill: softSkill,
        livello: livello,
      };
      setFormData({
        ...formData,
        softSkills: [...(formData.softSkills || []), levelSoftSkill],
      });
    }
  };

  const removeSoftSkill = (softSkillId: number) => {
    setFormData({
      ...formData,
      softSkills: formData.softSkills?.filter((lss) => lss.softSkill.id !== softSkillId) || [],
    });
  };

  const updateSoftSkillLevel = (softSkillId: number, livello: number) => {
    setFormData({
      ...formData,
      softSkills: formData.softSkills?.map((lss) =>
        lss.softSkill.id === softSkillId ? { ...lss, livello } : lss
      ) || [],
    });
  };

  const addCertificazione = (certId: number) => {
    const cert = availableCerts.find((c) => c.id === certId);
    if (cert && !formData.certificazioni?.some((c) => c.id === certId)) {
      setFormData({
        ...formData,
        certificazioni: [...(formData.certificazioni || []), cert],
      });
    }
  };

  const removeCertificazione = (certId: number) => {
    setFormData({
      ...formData,
      certificazioni: formData.certificazioni?.filter((c) => c.id !== certId) || [],
    });
  };

  const addEsperienza = () => {
    if (newEsperienza.azienda && newEsperienza.ruolo && newEsperienza.dataInizio && newEsperienza.attivitaSvolte) {
      setFormData({
        ...formData,
        esperienze: [...(formData.esperienze || []), { ...newEsperienza }],
      });
      setNewEsperienza({
        azienda: '',
        ruolo: '',
        dataInizio: '',
        descrizione: '',
        attivitaSvolte: '',
        tecnologie: '',
      });
    }
  };

  const removeEsperienza = (index: number) => {
    setFormData({
      ...formData,
      esperienze: formData.esperienze?.filter((_, i) => i !== index) || [],
    });
  };

  const createAndAddNewSkill = () => {
    if (newSkill.nome && newSkill.descrizione) {
      const levelSkill: LevelSkill = {
        skill: { ...newSkill },
        livello: 5,
      };
      setFormData({
        ...formData,
        skills: [...(formData.skills || []), levelSkill],
      });
      setNewSkill({ nome: '', descrizione: '' });
      setShowNewSkillForm(false);
    }
  };

  const createAndAddNewSoftSkill = () => {
    if (newSoftSkill.nome && newSoftSkill.descrizione) {
      const levelSoftSkill: LevelSoftSkill = {
        softSkill: { ...newSoftSkill },
        livello: 5,
      };
      setFormData({
        ...formData,
        softSkills: [...(formData.softSkills || []), levelSoftSkill],
      });
      setNewSoftSkill({ nome: '', descrizione: '' });
      setShowNewSoftSkillForm(false);
    }
  };

  const createAndAddNewCert = () => {
    if (newCert.nome && newCert.descrizione) {
      setFormData({
        ...formData,
        certificazioni: [...(formData.certificazioni || []), { ...newCert }],
      });
      setNewCert({ nome: '', descrizione: '', conseguita: '' });
      setShowNewCertForm(false);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>{isEdit ? 'Modifica Curriculum' : 'Nuovo Curriculum'}</h1>
        <Button
          variant="secondary"
          icon={<ArrowLeft size={20} />}
          onClick={() => navigate('/curricula')}
        >
          Indietro
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="form-container">
        <div className="form-section">
          <h3>Utente</h3>
          <div className="form-grid">
            <div className="form-group full-width">
              <label>Seleziona Utente *</label>
              {isEdit && formData.utente ? (
                <div style={{ padding: '10px', backgroundColor: '#f5f5f5', borderRadius: '4px' }}>
                  <strong>{formData.utente.nome} {formData.utente.cognome}</strong>
                  {formData.utente.username && <span> ({formData.utente.username})</span>}
                </div>
              ) : (
                <select
                  value={formData.utente?.id || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      utente: utenti.find((u) => u.id === Number(e.target.value)),
                    })
                  }
                  required
                >
                  <option value="">Seleziona un utente</option>
                  {utenti.map((utente) => (
                    <option key={utente.id} value={utente.id}>
                      {utente.nome} {utente.cognome} - {utente.username}
                    </option>
                  ))}
                </select>
              )}
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Skills ({formData.skills?.length || 0})</h3>
          <div className="form-group">
            <label>Aggiungi Skill</label>
            <select
              onChange={(e) => {
                if (e.target.value) {
                  addSkill(Number(e.target.value));
                  e.target.value = '';
                }
              }}
              value=""
            >
              <option value="">Seleziona una skill da aggiungere...</option>
              {availableSkills
                .filter((s) => !formData.skills?.some((ls) => ls.skill.id === s.id))
                .map((skill) => (
                  <option key={skill.id} value={skill.id}>
                    {skill.nome} {skill.descrizione ? `- ${skill.descrizione}` : ''}
                  </option>
                ))}
            </select>
            {availableSkills.filter((s) => !formData.skills?.some((ls) => ls.skill.id === s.id)).length === 0 && (
              <small style={{ color: '#666', marginTop: '4px', display: 'block' }}>
                Tutte le skill disponibili sono state aggiunte
              </small>
            )}
            {!showNewSkillForm && (
              <div style={{ marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="secondary"
                  icon={<Plus size={20} />}
                  onClick={() => setShowNewSkillForm(true)}
                >
                  Crea Nuova Skill
                </Button>
              </div>
            )}
          </div>
          {showNewSkillForm && (
            <div style={{ marginTop: '15px', padding: '15px', border: '1px solid #ddd', borderRadius: '4px' }}>
              <h4>Crea Nuova Skill</h4>
              <div className="form-grid">
                <div className="form-group">
                  <label>Nome *</label>
                  <input
                    type="text"
                    value={newSkill.nome}
                    onChange={(e) => setNewSkill({ ...newSkill, nome: e.target.value })}
                    placeholder="Es: Java"
                  />
                </div>
                <div className="form-group">
                  <label>Descrizione *</label>
                  <input
                    type="text"
                    value={newSkill.descrizione}
                    onChange={(e) => setNewSkill({ ...newSkill, descrizione: e.target.value })}
                    placeholder="Es: Linguaggio di programmazione"
                  />
                </div>
              </div>
              <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="primary"
                  icon={<Plus size={20} />}
                  onClick={createAndAddNewSkill}
                  disabled={!newSkill.nome || !newSkill.descrizione}
                >
                  Aggiungi
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => {
                    setShowNewSkillForm(false);
                    setNewSkill({ nome: '', descrizione: '' });
                  }}
                >
                  Annulla
                </Button>
              </div>
            </div>
          )}
          <div className="tags-container">
            {formData.skills && formData.skills.length > 0 ? (
              formData.skills.map((levelSkill, index) => (
                <div key={levelSkill.skill.id || index} className="tag">
                  <span>
                    {levelSkill.skill.nome} - Livello:
                    <input
                      type="number"
                      min="1"
                      max="10"
                      value={levelSkill.livello}
                      onChange={(e) => updateSkillLevel(levelSkill.skill.id!, Number(e.target.value))}
                      style={{ width: '50px', margin: '0 5px' }}
                    />
                    /10
                  </span>
                  <button type="button" onClick={() => removeSkill(levelSkill.skill.id!)}>
                    <X size={14} />
                  </button>
                </div>
              ))
            ) : (
              <p style={{ color: '#666', fontStyle: 'italic' }}>Nessuna skill aggiunta. Seleziona dal menu sopra per aggiungere.</p>
            )}
          </div>
        </div>

        <div className="form-section">
          <h3>Soft Skills ({formData.softSkills?.length || 0})</h3>
          <div className="form-group">
            <label>Aggiungi Soft Skill</label>
            <select
              onChange={(e) => {
                if (e.target.value) {
                  addSoftSkill(Number(e.target.value));
                  e.target.value = '';
                }
              }}
              value=""
            >
              <option value="">Seleziona una soft skill da aggiungere...</option>
              {availableSoftSkills
                .filter((s) => !formData.softSkills?.some((lss) => lss.softSkill.id === s.id))
                .map((softSkill) => (
                  <option key={softSkill.id} value={softSkill.id}>
                    {softSkill.nome} {softSkill.descrizione ? `- ${softSkill.descrizione}` : ''}
                  </option>
                ))}
            </select>
            {availableSoftSkills.filter((s) => !formData.softSkills?.some((lss) => lss.softSkill.id === s.id)).length === 0 && (
              <small style={{ color: '#666', marginTop: '4px', display: 'block' }}>
                Tutte le soft skill disponibili sono state aggiunte
              </small>
            )}
            {!showNewSoftSkillForm && (
              <div style={{ marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="secondary"
                  icon={<Plus size={20} />}
                  onClick={() => setShowNewSoftSkillForm(true)}
                >
                  Crea Nuova Soft Skill
                </Button>
              </div>
            )}
          </div>
          {showNewSoftSkillForm && (
            <div style={{ marginTop: '15px', padding: '15px', border: '1px solid #ddd', borderRadius: '4px' }}>
              <h4>Crea Nuova Soft Skill</h4>
              <div className="form-grid">
                <div className="form-group">
                  <label>Nome *</label>
                  <input
                    type="text"
                    value={newSoftSkill.nome}
                    onChange={(e) => setNewSoftSkill({ ...newSoftSkill, nome: e.target.value })}
                    placeholder="Es: Leadership"
                  />
                </div>
                <div className="form-group">
                  <label>Descrizione *</label>
                  <input
                    type="text"
                    value={newSoftSkill.descrizione}
                    onChange={(e) => setNewSoftSkill({ ...newSoftSkill, descrizione: e.target.value })}
                    placeholder="Es: Capacità di guidare un team"
                  />
                </div>
              </div>
              <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="primary"
                  icon={<Plus size={20} />}
                  onClick={createAndAddNewSoftSkill}
                  disabled={!newSoftSkill.nome || !newSoftSkill.descrizione}
                >
                  Aggiungi
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => {
                    setShowNewSoftSkillForm(false);
                    setNewSoftSkill({ nome: '', descrizione: '' });
                  }}
                >
                  Annulla
                </Button>
              </div>
            </div>
          )}
          <div className="tags-container">
            {formData.softSkills && formData.softSkills.length > 0 ? (
              formData.softSkills.map((levelSoftSkill, index) => (
                <div key={levelSoftSkill.softSkill.id || index} className="tag">
                  <span>
                    {levelSoftSkill.softSkill.nome}
                    {levelSoftSkill.livello !== undefined && (
                      <>
                        {' '}- Livello:
                        <input
                          type="number"
                          min="1"
                          max="10"
                          value={levelSoftSkill.livello}
                          onChange={(e) => updateSoftSkillLevel(levelSoftSkill.softSkill.id!, Number(e.target.value))}
                          style={{ width: '50px', margin: '0 5px' }}
                        />
                        /10
                      </>
                    )}
                  </span>
                  <button type="button" onClick={() => removeSoftSkill(levelSoftSkill.softSkill.id!)}>
                    <X size={14} />
                  </button>
                </div>
              ))
            ) : (
              <p style={{ color: '#666', fontStyle: 'italic' }}>Nessuna soft skill aggiunta. Seleziona dal menu sopra per aggiungere.</p>
            )}
          </div>
        </div>

        <div className="form-section">
          <h3>Certificazioni ({formData.certificazioni?.length || 0})</h3>
          <div className="form-group">
            <label>Aggiungi Certificazione</label>
            <select
              onChange={(e) => {
                if (e.target.value) {
                  addCertificazione(Number(e.target.value));
                  e.target.value = '';
                }
              }}
              value=""
            >
              <option value="">Seleziona una certificazione da aggiungere...</option>
              {availableCerts
                .filter((c) => !formData.certificazioni?.some((fc) => fc.id === c.id))
                .map((cert) => (
                  <option key={cert.id} value={cert.id}>
                    {cert.nome}
                  </option>
                ))}
            </select>
            {availableCerts.filter((c) => !formData.certificazioni?.some((fc) => fc.id === c.id)).length === 0 && (
              <small style={{ color: '#666', marginTop: '4px', display: 'block' }}>
                Tutte le certificazioni disponibili sono state aggiunte
              </small>
            )}
            {!showNewCertForm && (
              <div style={{ marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="secondary"
                  icon={<Plus size={20} />}
                  onClick={() => setShowNewCertForm(true)}
                >
                  Crea Nuova Certificazione
                </Button>
              </div>
            )}
          </div>
          {showNewCertForm && (
            <div style={{ marginTop: '15px', padding: '15px', border: '1px solid #ddd', borderRadius: '4px' }}>
              <h4>Crea Nuova Certificazione</h4>
              <div className="form-grid">
                <div className="form-group">
                  <label>Nome *</label>
                  <input
                    type="text"
                    value={newCert.nome}
                    onChange={(e) => setNewCert({ ...newCert, nome: e.target.value })}
                    placeholder="Es: AWS Certified Solutions Architect"
                  />
                </div>
                <div className="form-group">
                  <label>Descrizione *</label>
                  <input
                    type="text"
                    value={newCert.descrizione}
                    onChange={(e) => setNewCert({ ...newCert, descrizione: e.target.value })}
                    placeholder="Es: Certificazione cloud computing"
                  />
                </div>
                <div className="form-group">
                  <label>Conseguimento</label>
                  <input
                    type="date"
                    value={newCert.conseguita || ''}
                    onChange={(e) => setNewCert({ ...newCert, conseguita: e.target.value })}
                  />
                </div>
              </div>
              <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                <Button
                  type="button"
                  variant="primary"
                  icon={<Plus size={20} />}
                  onClick={createAndAddNewCert}
                  disabled={!newCert.nome || !newCert.descrizione}
                >
                  Aggiungi
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => {
                    setShowNewCertForm(false);
                    setNewCert({ nome: '', descrizione: '', conseguita: '' });
                  }}
                >
                  Annulla
                </Button>
              </div>
            </div>
          )}
          <div className="tags-container">
            {formData.certificazioni && formData.certificazioni.length > 0 ? (
              formData.certificazioni.map((cert) => (
                <div key={cert.id} className="tag">
                  <span>{cert.nome}</span>
                  <button type="button" onClick={() => removeCertificazione(cert.id!)}>
                    <X size={14} />
                  </button>
                </div>
              ))
            ) : (
              <p style={{ color: '#666', fontStyle: 'italic' }}>Nessuna certificazione aggiunta. Seleziona dal menu sopra per aggiungere.</p>
            )}
          </div>
        </div>

        <div className="form-section">
          <h3>Esperienze Lavorative</h3>
          <div className="experience-form">
            <div className="form-grid">
              <div className="form-group">
                <label>Azienda</label>
                <input
                  type="text"
                  value={newEsperienza.azienda}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, azienda: e.target.value })
                  }
                />
              </div>
              <div className="form-group">
                <label>Ruolo</label>
                <input
                  type="text"
                  value={newEsperienza.ruolo}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, ruolo: e.target.value })
                  }
                />
              </div>
              <div className="form-group">
                <label>Data Inizio</label>
                <input
                  type="date"
                  value={newEsperienza.dataInizio}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, dataInizio: e.target.value })
                  }
                />
              </div>
              <div className="form-group">
                <label>Data Fine</label>
                <input
                  type="date"
                  value={newEsperienza.dataFine || ''}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, dataFine: e.target.value })
                  }
                />
              </div>
              <div className="form-group full-width">
                <label>Tecnologie</label>
                <input
                  type="text"
                  value={newEsperienza.tecnologie || ''}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, tecnologie: e.target.value })
                  }
                  placeholder="Es: Java, React, PostgreSQL"
                />
              </div>
              <div className="form-group full-width">
                <label>Descrizione</label>
                <textarea
                  value={newEsperienza.descrizione || ''}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, descrizione: e.target.value })
                  }
                />
              </div>
              <div className="form-group full-width">
                <label>Attività Svolte *</label>
                <textarea
                  value={newEsperienza.attivitaSvolte || ''}
                  onChange={(e) =>
                    setNewEsperienza({ ...newEsperienza, attivitaSvolte: e.target.value })
                  }
                  placeholder="Descrivi le attività principali svolte in questo ruolo"
                  required
                />
              </div>
            </div>
            <Button
              type="button"
              variant="secondary"
              icon={<Plus size={20} />}
              onClick={addEsperienza}
            >
              Aggiungi Esperienza
            </Button>
          </div>

          <div className="experiences-list">
            {formData.esperienze?.map((exp, index) => (
              <div key={index} className="experience-card">
                <div className="experience-header">
                  <h4>
                    {exp.ruolo} - {exp.azienda}
                  </h4>
                  <button
                    type="button"
                    className="action-btn delete"
                    onClick={() => removeEsperienza(index)}
                  >
                    <X size={16} />
                  </button>
                </div>
                <p className="experience-dates">
                  {exp.dataInizio} - {exp.dataFine || 'Presente'}
                </p>
                {exp.tecnologie && <p className="experience-tech">{exp.tecnologie}</p>}
                {exp.descrizione && <p className="experience-desc">{exp.descrizione}</p>}
                {exp.attivitaSvolte && <p className="experience-desc"><strong>Attività Svolte:</strong> {exp.attivitaSvolte}</p>}
              </div>
            ))}
          </div>
        </div>

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/curricula')}>
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

export default CurriculumForm;
