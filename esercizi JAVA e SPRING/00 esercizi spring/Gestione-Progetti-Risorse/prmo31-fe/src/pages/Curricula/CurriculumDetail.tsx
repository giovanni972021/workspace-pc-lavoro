import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, Edit, FileText, Download } from 'lucide-react';
import Button from '../../components/Button';
import type { Curriculum } from '../../types';
import { curriculumService } from '../../services/curriculum.service';
import { exportCurriculumToPDF } from '../../utils/pdfExport';
import { exportCurriculumToExcel } from '../../utils/excelExport';
import '../Utenti/Utenti.css';
import './Curriculum.css';

const CurriculumDetail: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [curriculum, setCurriculum] = useState<Curriculum | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadCurriculum();
  }, [id]);

  const loadCurriculum = async () => {
    try {
      setLoading(true);
      const data = await curriculumService.getById(Number(id));
      setCurriculum(data);
    } catch (error) {
      console.error('Errore nel caricamento del curriculum:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleExportPDF = async () => {
    if (curriculum) {
      await exportCurriculumToPDF(curriculum);
    }
  };

  const handleExportExcel = async () => {
    if (curriculum) {
      await exportCurriculumToExcel(curriculum);
    }
  };

  if (loading) {
    return <div className="page-container">Caricamento...</div>;
  }

  if (!curriculum) {
    return <div className="page-container">Curriculum non trovato</div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>
          Curriculum -{' '}
          {curriculum.utente
            ? `${curriculum.utente.nome} ${curriculum.utente.cognome}`
            : 'N/A'}
        </h1>
        <div style={{ display: 'flex', gap: '12px' }}>
          <Button
            variant="secondary"
            icon={<FileText size={20} />}
            onClick={handleExportPDF}
          >
            Esporta PDF
          </Button>
          <Button
            variant="secondary"
            icon={<Download size={20} />}
            onClick={handleExportExcel}
          >
            Esporta Excel
          </Button>
          <Button
            variant="primary"
            icon={<Edit size={20} />}
            onClick={() => navigate(`/curricula/${id}/edit`)}
          >
            Modifica
          </Button>
          <Button
            variant="secondary"
            icon={<ArrowLeft size={20} />}
            onClick={() => navigate('/curricula')}
          >
            Indietro
          </Button>
        </div>
      </div>

      <div className="detail-container">
        {curriculum.utente && (
          <div className="detail-section">
            <h3>Informazioni Utente</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <label>Nome Completo</label>
                <span>
                  {curriculum.utente.nome} {curriculum.utente.cognome}
                </span>
              </div>
              <div className="detail-item">
                <label>Email</label>
                <span>{curriculum.utente.email}</span>
              </div>
              <div className="detail-item">
                <label>Telefono</label>
                <span>{curriculum.utente.telefono || '-'}</span>
              </div>
              <div className="detail-item">
                <label>Ruolo</label>
                <span>{curriculum.utente.ruolo || '-'}</span>
              </div>
            </div>
          </div>
        )}

        <div className="detail-section">
          <h3>Skills ({curriculum.skills?.length || 0})</h3>
          {curriculum.skills && curriculum.skills.length > 0 ? (
            <div className="skills-grid">
              {curriculum.skills.map((levelSkill, index) => (
                <div key={levelSkill.id || index} className="skill-card">
                  <h4>{levelSkill.skill.nome}</h4>
                  <div className="skill-level">
                    <div className="skill-level-bar">
                      <div
                        className="skill-level-fill"
                        style={{ width: `${(levelSkill.livello / 10) * 100}%` }}
                      ></div>
                    </div>
                    <span className="skill-level-text">{levelSkill.livello}/10</span>
                  </div>
                  {levelSkill.skill.descrizione && <p className="skill-desc">{levelSkill.skill.descrizione}</p>}
                </div>
              ))}
            </div>
          ) : (
            <p>Nessuna skill presente</p>
          )}
        </div>

        <div className="detail-section">
          <h3>Certificazioni ({curriculum.certificazioni?.length || 0})</h3>
          {curriculum.certificazioni && curriculum.certificazioni.length > 0 ? (
            <div className="certifications-list">
              {curriculum.certificazioni.map((cert) => (
                <div key={cert.id} className="certification-card">
                  <h4>{cert.nome}</h4>
                  {cert.descrizione && <p className="cert-desc">{cert.descrizione}</p>}
                  {cert.conseguita && (
                    <p className="cert-date">
                      Conseguimento: {new Date(cert.conseguita).toLocaleDateString('it-IT')}
                    </p>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p>Nessuna certificazione presente</p>
          )}
        </div>

        <div className="detail-section">
          <h3>Esperienze Lavorative ({curriculum.esperienze?.length || 0})</h3>
          {curriculum.esperienze && curriculum.esperienze.length > 0 ? (
            <div className="experiences-timeline">
              {curriculum.esperienze.map((exp, index) => (
                <div key={index} className="experience-item">
                  <div className="experience-timeline-dot"></div>
                  <div className="experience-content">
                    <h4>
                      {exp.ruolo} - {exp.azienda}
                    </h4>
                    <p className="experience-period">
                      {new Date(exp.dataInizio).toLocaleDateString('it-IT')} -{' '}
                      {exp.dataFine
                        ? new Date(exp.dataFine).toLocaleDateString('it-IT')
                        : 'Presente'}
                    </p>
                    {exp.tecnologie && (
                      <div className="experience-tags">
                        {exp.tecnologie.split(',').map((tech, i) => (
                          <span key={i} className="tech-tag">
                            {tech.trim()}
                          </span>
                        ))}
                      </div>
                    )}
                    {exp.descrizione && <p className="experience-description">{exp.descrizione}</p>}
                    {exp.attivitaSvolte && <p className="experience-description"><strong>Attività Svolte:</strong> {exp.attivitaSvolte}</p>}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p>Nessuna esperienza presente</p>
          )}
        </div>

        {curriculum.softSkills && curriculum.softSkills.length > 0 && (
          <div className="detail-section">
            <h3>Soft Skills ({curriculum.softSkills.length})</h3>
            <div className="skills-grid">
              {curriculum.softSkills.map((levelSoftSkill, index) => (
                <div key={levelSoftSkill.id || index} className="skill-card">
                  <h4>{levelSoftSkill.softSkill.nome}</h4>
                  {levelSoftSkill.livello && (
                    <div className="skill-level">
                      <div className="skill-level-bar">
                        <div
                          className="skill-level-fill"
                          style={{ width: `${(levelSoftSkill.livello / 10) * 100}%` }}
                        ></div>
                      </div>
                      <span className="skill-level-text">{levelSoftSkill.livello}/10</span>
                    </div>
                  )}
                  {levelSoftSkill.softSkill.descrizione && (
                    <p className="skill-desc">{levelSoftSkill.softSkill.descrizione}</p>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CurriculumDetail;
