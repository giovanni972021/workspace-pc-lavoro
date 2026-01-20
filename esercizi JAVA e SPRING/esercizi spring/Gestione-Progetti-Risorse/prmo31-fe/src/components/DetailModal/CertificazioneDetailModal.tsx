import React from 'react';
import Modal from '../Modal';
import type { Certificazione } from '../../types';
import './DetailModal.css';

interface CertificazioneDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  certificazione: Certificazione | null;
}

const CertificazioneDetailModal: React.FC<CertificazioneDetailModalProps> = ({
  isOpen,
  onClose,
  certificazione,
}) => {
  if (!certificazione) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Dettaglio Certificazione">
      <div className="detail-modal-content">
        <div className="detail-section">
          <h3>Informazioni</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>ID</label>
              <span>{certificazione.id}</span>
            </div>
            <div className="detail-field">
              <label>Nome</label>
              <span>{certificazione.nome}</span>
            </div>
            {certificazione.conseguita && (
              <div className="detail-field">
                <label>Data Conseguimento</label>
                <span>
                  {new Date(certificazione.conseguita).toLocaleDateString('it-IT')}
                </span>
              </div>
            )}
          </div>
          {certificazione.descrizione && (
            <div className="detail-field full-width">
              <label>Descrizione</label>
              <p className="detail-description">{certificazione.descrizione}</p>
            </div>
          )}
        </div>

        {certificazione.commonEntity?.updatedAt && (
          <div className="detail-footer">
            Ultimo aggiornamento:{' '}
            {new Date(certificazione.commonEntity.updatedAt).toLocaleString('it-IT')}
            {certificazione.commonEntity.updatedBy && ` da ${certificazione.commonEntity.updatedBy}`}
          </div>
        )}
      </div>
    </Modal>
  );
};

export default CertificazioneDetailModal;
