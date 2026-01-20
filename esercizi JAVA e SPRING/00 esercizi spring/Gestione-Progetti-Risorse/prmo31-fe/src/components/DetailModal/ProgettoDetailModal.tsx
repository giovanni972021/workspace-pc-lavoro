import React from 'react';
import Modal from '../Modal';
import type { Progetto } from '../../types';
import './DetailModal.css';

interface ProgettoDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  progetto: Progetto | null;
}

const ProgettoDetailModal: React.FC<ProgettoDetailModalProps> = ({
  isOpen,
  onClose,
  progetto,
}) => {
  if (!progetto) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Dettaglio Progetto">
      <div className="detail-modal-content">
        <div className="detail-section">
          <h3>Informazioni Generali</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>ID</label>
              <span>{progetto.id}</span>
            </div>
            <div className="detail-field">
              <label>Nome</label>
              <span>{progetto.nome}</span>
            </div>
            <div className="detail-field">
              <label>Stato</label>
              <span className="badge badge-info">{progetto.stato?.nome || '-'}</span>
            </div>
          </div>
          {progetto.descrizione && (
            <div className="detail-field full-width">
              <label>Descrizione</label>
              <p className="detail-description">{progetto.descrizione}</p>
            </div>
          )}
        </div>

        <div className="detail-section">
          <h3>Pianificazione</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Data Inizio Presunta</label>
              <span>
                {progetto.dataInizioPresunta
                  ? new Date(progetto.dataInizioPresunta).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Data Inizio Effettiva</label>
              <span>
                {progetto.dataInizioEffettiva
                  ? new Date(progetto.dataInizioEffettiva).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Data Fine Presunta</label>
              <span>
                {progetto.dataFinePresunta
                  ? new Date(progetto.dataFinePresunta).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Data Fine Effettiva</label>
              <span>
                {progetto.dataFineEffettiva
                  ? new Date(progetto.dataFineEffettiva).toLocaleDateString('it-IT')
                  : '-'}
              </span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Budget</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Budget Preventivato</label>
              <span>
                {progetto.budgetPreventivato
                  ? `€ ${progetto.budgetPreventivato.toLocaleString('it-IT')}`
                  : '-'}
              </span>
            </div>
            <div className="detail-field">
              <label>Budget Effettivo</label>
              <span>
                {progetto.budgetEffettivo
                  ? `€ ${progetto.budgetEffettivo.toLocaleString('it-IT')}`
                  : '-'}
              </span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Valutazione</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Priorità</label>
              <span>{progetto.priorita ? `${progetto.priorita}/10` : '-'}</span>
            </div>
            <div className="detail-field">
              <label>Fattibilità</label>
              <span>{progetto.fattibilita ? `${progetto.fattibilita}/10` : '-'}</span>
            </div>
          </div>
        </div>

        {progetto.numberJobTitles && progetto.numberJobTitles.length > 0 && (
          <div className="detail-section">
            <h3>Risorse Richieste</h3>
            <div className="detail-list">
              {progetto.numberJobTitles.map((njt, index) => (
                <div key={index} className="detail-list-item">
                  <strong>{njt.jobTitle?.nome || 'N/A'}:</strong> {njt.number || 0} risorse
                </div>
              ))}
            </div>
          </div>
        )}

        {progetto.commonEntity?.updatedAt && (
          <div className="detail-footer">
            Ultimo aggiornamento:{' '}
            {new Date(progetto.commonEntity.updatedAt).toLocaleString('it-IT')}
            {progetto.commonEntity.updatedBy && ` da ${progetto.commonEntity.updatedBy}`}
          </div>
        )}
      </div>
    </Modal>
  );
};

export default ProgettoDetailModal;
