import React from 'react';
import Modal from '../Modal';
import type { Utente } from '../../types';
import './DetailModal.css';

interface UtenteDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  utente: Utente | null;
}

const UtenteDetailModal: React.FC<UtenteDetailModalProps> = ({
  isOpen,
  onClose,
  utente,
}) => {
  if (!utente) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Dettaglio Utente">
      <div className="detail-modal-content">
        <div className="detail-section">
          <h3>Informazioni Generali</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>ID</label>
              <span>{utente.id}</span>
            </div>
            <div className="detail-field">
              <label>Nome</label>
              <span>{utente.nome}</span>
            </div>
            <div className="detail-field">
              <label>Cognome</label>
              <span>{utente.cognome}</span>
            </div>
            <div className="detail-field">
              <label>Username</label>
              <span>{utente.username}</span>
            </div>
            <div className="detail-field">
              <label>Codice Fiscale</label>
              <span>{utente.codiceFiscale}</span>
            </div>
            <div className="detail-field">
              <label>Stato</label>
              <span className={`badge ${utente.attivo ? 'badge-success' : 'badge-danger'}`}>
                {utente.attivo ? 'Attivo' : 'Non attivo'}
              </span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Ruolo e Profilo</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Tipo Utente</label>
              <span>
                {typeof utente.tipoUtente === 'object' && utente.tipoUtente
                  ? utente.tipoUtente.tipo
                  : utente.tipoUtente || '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Job Title</label>
              <span>
                {typeof utente.jobTitle === 'object' && utente.jobTitle
                  ? utente.jobTitle.nome
                  : utente.jobTitle || '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Profilo</label>
              <span>{utente.profilo?.nome || '-'}</span>
            </div>
          </div>
        </div>

        {utente.contatti && utente.contatti.length > 0 && (
          <div className="detail-section">
            <h3>Contatti</h3>
            <div className="detail-list">
              {utente.contatti.map((contatto, index) => (
                <div key={index} className="detail-list-item">
                  <strong>{contatto.tipoContatto?.nome || 'N/A'}:</strong> {contatto.valore}
                </div>
              ))}
            </div>
          </div>
        )}

        <div className="detail-section">
          <h3>Collaborazione</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Data Inizio</label>
              <span>
                {utente.dataInizioCollaborazione
                  ? new Date(utente.dataInizioCollaborazione).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Data Fine</label>
              <span>
                {utente.dataFineCollaborazione
                  ? new Date(utente.dataFineCollaborazione).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Valutazione</label>
              <span>{utente.valutazione ? `${utente.valutazione}/10` : '-'}</span>
            </div>
          </div>
        </div>

        {utente.note && (
          <div className="detail-section">
            <h3>Note</h3>
            <p className="detail-notes">{utente.note}</p>
          </div>
        )}

        {utente.commonEntity?.updatedAt && (
          <div className="detail-footer">
            Ultimo aggiornamento:{' '}
            {new Date(utente.commonEntity.updatedAt).toLocaleString('it-IT')}
            {utente.commonEntity.updatedBy && ` da ${utente.commonEntity.updatedBy}`}
          </div>
        )}
      </div>
    </Modal>
  );
};

export default UtenteDetailModal;
