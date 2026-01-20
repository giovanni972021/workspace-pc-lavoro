import React from 'react';
import Modal from '../Modal';
import type { Cliente } from '../../types';
import './DetailModal.css';

interface ClienteDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  cliente: Cliente | null;
}

const ClienteDetailModal: React.FC<ClienteDetailModalProps> = ({
  isOpen,
  onClose,
  cliente,
}) => {
  if (!cliente) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Dettaglio Cliente">
      <div className="detail-modal-content">
        <div className="detail-section">
          <h3>Informazioni Generali</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>ID</label>
              <span>{cliente.id}</span>
            </div>
            <div className="detail-field">
              <label>Nome</label>
              <span>{cliente.nome}</span>
            </div>
            {cliente.feedbackScore !== undefined && cliente.feedbackScore !== null && (
              <div className="detail-field">
                <label>Feedback Score</label>
                <span>{cliente.feedbackScore}/10</span>
              </div>
            )}
          </div>
          {cliente.descrizione && (
            <div className="detail-field full-width">
              <label>Descrizione</label>
              <p className="detail-description">{cliente.descrizione}</p>
            </div>
          )}
        </div>

        <div className="detail-section">
          <h3>Dati Fiscali</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Partita IVA</label>
              <span>{cliente.partitaIva || '-'}</span>
            </div>
            <div className="detail-field">
              <label>Codice Fiscale</label>
              <span>{cliente.codiceFiscale || '-'}</span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Indirizzo</h3>
          <div className="detail-grid">
            <div className="detail-field full-width">
              <label>Via</label>
              <span>{cliente.indirizzo || '-'}</span>
            </div>
            <div className="detail-field">
              <label>Città</label>
              <span>{cliente.citta || '-'}</span>
            </div>
            <div className="detail-field">
              <label>Provincia</label>
              <span>{cliente.provincia || '-'}</span>
            </div>
            <div className="detail-field">
              <label>CAP</label>
              <span>{cliente.cap || '-'}</span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Contatti</h3>
          <div className="detail-grid">
            <div className="detail-field">
              <label>Telefono</label>
              <span>{cliente.telefono || '-'}</span>
            </div>
            <div className="detail-field">
              <label>Email</label>
              <span>{cliente.email || '-'}</span>
            </div>
            <div className="detail-field">
              <label>PEC</label>
              <span>{cliente.pec || '-'}</span>
            </div>
            <div className="detail-field">
              <label>Referente</label>
              <span>{cliente.referente || '-'}</span>
            </div>
          </div>
        </div>

        {cliente.progetti && cliente.progetti.length > 0 && (
          <div className="detail-section">
            <h3>Progetti Associati</h3>
            <div className="detail-list">
              {cliente.progetti.map((progetto, index) => (
                <div key={index} className="detail-list-item">
                  {progetto.nome}
                </div>
              ))}
            </div>
          </div>
        )}

        {cliente.commonEntity?.updatedAt && (
          <div className="detail-footer">
            Ultimo aggiornamento:{' '}
            {new Date(cliente.commonEntity.updatedAt).toLocaleString('it-IT')}
            {cliente.commonEntity.updatedBy && ` da ${cliente.commonEntity.updatedBy}`}
          </div>
        )}
      </div>
    </Modal>
  );
};

export default ClienteDetailModal;
