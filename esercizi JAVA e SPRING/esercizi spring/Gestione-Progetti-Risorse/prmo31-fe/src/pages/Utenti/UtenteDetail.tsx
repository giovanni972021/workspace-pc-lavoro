import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, Edit } from 'lucide-react';
import Button from '../../components/Button';
import type { Utente } from '../../types';
import { utenteService } from '../../services/utente.service';
import './Utenti.css';

const UtenteDetail: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [utente, setUtente] = useState<Utente | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUtente();
  }, [id]);

  const loadUtente = async () => {
    try {
      setLoading(true);
      const data = await utenteService.getById(Number(id));
      setUtente(data);
    } catch (error) {
      console.error('Errore nel caricamento dell\'utente:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="page-container">Caricamento...</div>;
  }

  if (!utente) {
    return <div className="page-container">Utente non trovato</div>;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>
          {utente.nome} {utente.cognome}
        </h1>
        <div style={{ display: 'flex', gap: '12px' }}>
          <Button
            variant="primary"
            icon={<Edit size={20} />}
            onClick={() => navigate(`/utenti/${id}/edit`)}
          >
            Modifica
          </Button>
          <Button
            variant="secondary"
            icon={<ArrowLeft size={20} />}
            onClick={() => navigate('/utenti')}
          >
            Indietro
          </Button>
        </div>
      </div>

      <div className="detail-container">
        <div className="detail-section">
          <h3>Informazioni Personali</h3>
          <div className="detail-grid">
            <div className="detail-item">
              <label>Nome</label>
              <span>{utente.nome}</span>
            </div>
            <div className="detail-item">
              <label>Cognome</label>
              <span>{utente.cognome}</span>
            </div>
            <div className="detail-item">
              <label>Username</label>
              <span>{utente.username}</span>
            </div>
            <div className="detail-item">
              <label>Codice Fiscale</label>
              <span>{utente.codiceFiscale}</span>
            </div>
            <div className="detail-item">
              <label>Email</label>
              <span>{utente.email}</span>
            </div>
            <div className="detail-item">
              <label>Telefono</label>
              <span>{utente.telefono || '-'}</span>
            </div>
            <div className="detail-item">
              <label>Data di Nascita</label>
              <span>{utente.dataNascita || '-'}</span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Dati Lavorativi</h3>
          <div className="detail-grid">
            <div className="detail-item">
              <label>Ruolo</label>
              <span>{utente.ruolo || '-'}</span>
            </div>
            <div className="detail-item">
              <label>Data Assunzione</label>
              <span>{utente.dataAssunzione || '-'}</span>
            </div>
            <div className="detail-item">
              <label>Stato</label>
              <span className={`badge ${utente.attivo ? 'badge-success' : 'badge-danger'}`}>
                {utente.attivo ? 'Attivo' : 'Non attivo'}
              </span>
            </div>
          </div>
        </div>

        <div className="detail-section">
          <h3>Indirizzo</h3>
          <div className="detail-grid">
            <div className="detail-item full-width">
              <label>Indirizzo</label>
              <span>{utente.indirizzo || '-'}</span>
            </div>
            <div className="detail-item">
              <label>Città</label>
              <span>{utente.citta || '-'}</span>
            </div>
            <div className="detail-item">
              <label>Provincia</label>
              <span>{utente.provincia || '-'}</span>
            </div>
            <div className="detail-item">
              <label>CAP</label>
              <span>{utente.cap || '-'}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UtenteDetail;
