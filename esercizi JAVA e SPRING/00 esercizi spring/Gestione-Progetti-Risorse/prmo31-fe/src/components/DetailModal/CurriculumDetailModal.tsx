import React from 'react';
import Modal from '../Modal';
import type { Curriculum } from '../../types';
import './DetailModal.css';

interface CurriculumDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  curriculum: Curriculum | null;
}

const CurriculumDetailModal: React.FC<CurriculumDetailModalProps> = ({
  isOpen,
  onClose,
  curriculum,
}) => {
  if (!curriculum) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Dettaglio Curriculum">
      <div className="detail-modal-content">
        <div className="detail-section">
          <h3>Informazioni Utente</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>ID</label>
              <span>{curriculum.id}</span>
            </div>
            <div className="detail-field">
              <label>Utente</label>
              <span>
                {curriculum.utente
                  ? `${curriculum.utente.nome} ${curriculum.utente.cognome}`
                  : '-'}
              </span>
            </div>
            {curriculum.utente?.email && (
              <div className="detail-field">
                <label>Email</label>
                <span>{curriculum.utente.email}</span>
              </div>
            )}
            {curriculum.dataNascita && (
              <div className="detail-field">
                <label>Data di Nascita</label>
                <span>
                  {new Date(curriculum.dataNascita).toLocaleDateString('it-IT')}
                </span>
              </div>
            )}
          </div>
        </div>

        {curriculum.skills && curriculum.skills.length > 0 && (
          <div className="detail-section">
            <h3>Competenze Tecniche ({curriculum.skills.length})</h3>
            <div className="detail-list">
              {curriculum.skills.map((levelSkill, index) => (
                <div key={index} className="detail-list-item">
                  <strong>{levelSkill.skill.nome}</strong>
                  {levelSkill.livello && (
                    <span className="badge badge-info ml-2">
                      Livello: {levelSkill.livello}/10
                    </span>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {curriculum.softSkills && curriculum.softSkills.length > 0 && (
          <div className="detail-section">
            <h3>Soft Skills ({curriculum.softSkills.length})</h3>
            <div className="detail-list">
              {curriculum.softSkills.map((levelSoftSkill, index) => (
                <div key={index} className="detail-list-item">
                  <strong>{levelSoftSkill.softSkill.nome}</strong>
                  {levelSoftSkill.livello && (
                    <span className="badge badge-info ml-2">
                      Livello: {levelSoftSkill.livello}/10
                    </span>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {curriculum.certificazioni && curriculum.certificazioni.length > 0 && (
          <div className="detail-section">
            <h3>Certificazioni ({curriculum.certificazioni.length})</h3>
            <div className="detail-list">
              {curriculum.certificazioni.map((cert, index) => (
                <div key={index} className="detail-list-item">
                  <strong>{cert.nome}</strong>
                  {cert.conseguita && (
                    <span className="text-muted ml-2">
                      ({new Date(cert.conseguita).toLocaleDateString('it-IT')})
                    </span>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {curriculum.esperienze && curriculum.esperienze.length > 0 && (
          <div className="detail-section">
            <h3>Esperienze Lavorative ({curriculum.esperienze.length})</h3>
            <div className="detail-list">
              {curriculum.esperienze.map((esp, index) => (
                <div key={index} className="detail-list-item experience-item">
                  <div>
                    <strong>{esp.ruolo}</strong> @ {esp.azienda}
                  </div>
                  <div className="text-muted">
                    {new Date(esp.dataInizio).toLocaleDateString('it-IT')} -{' '}
                    {esp.dataFine
                      ? new Date(esp.dataFine).toLocaleDateString('it-IT')
                      : 'Presente'}
                  </div>
                  {esp.descrizione && <p className="mt-1">{esp.descrizione}</p>}
                  {esp.tecnologie && (
                    <div className="mt-1">
                      <em>Tecnologie: {esp.tecnologie}</em>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {curriculum.incarichi && curriculum.incarichi.length > 0 && (
          <div className="detail-section">
            <h3>Incarichi ({curriculum.incarichi.length})</h3>
            <div className="detail-list">
              {curriculum.incarichi.map((inc, index) => (
                <div key={index} className="detail-list-item">
                  <div>
                    <strong>{inc.ruolo}</strong>
                    {inc.progetto && ` - ${inc.progetto.nome}`}
                  </div>
                  <div className="text-muted">
                    {new Date(inc.dataInizio).toLocaleDateString('it-IT')} -{' '}
                    {inc.dataFine
                      ? new Date(inc.dataFine).toLocaleDateString('it-IT')
                      : 'Presente'}
                    {inc.percentualeImpegno && ` (${inc.percentualeImpegno}%)`}
                  </div>
                  {inc.descrizione && <p className="mt-1">{inc.descrizione}</p>}
                </div>
              ))}
            </div>
          </div>
        )}

        {curriculum.pathRelativoCv && (
          <div className="detail-section">
            <h3>File CV</h3>
            <div className="detail-field">
              <span>{curriculum.pathRelativoCv}</span>
            </div>
          </div>
        )}

        {curriculum.commonEntity?.updatedAt && (
          <div className="detail-footer">
            Ultimo aggiornamento:{' '}
            {new Date(curriculum.commonEntity.updatedAt).toLocaleString('it-IT')}
            {curriculum.commonEntity.updatedBy && ` da ${curriculum.commonEntity.updatedBy}`}
          </div>
        )}
      </div>
    </Modal>
  );
};

export default CurriculumDetailModal;
