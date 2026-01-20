import React, { useState, useEffect } from 'react';
import { utenteProgettoService } from '../../services/utenteProgetto.service';
import { utenteService } from '../../services/utente.service';
import { progettoService } from '../../services/progetto.service';
import type { GanttResponseDTO, GanttGroupBy, Progetto, UtenteListDTO } from '../../types/index.js';
import { Calendar, ChevronLeft, ChevronRight, ChevronDown, ChevronRight as ChevronRightIcon } from 'lucide-react';
import Modal from '../../components/Modal';
import './GanttAllocazione.css';

const GanttAllocazione: React.FC = () => {
  const [ganttData, setGanttData] = useState<GanttResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [groupBy, setGroupBy] = useState<GanttGroupBy>('week');
  const [startDate, setStartDate] = useState<string>(() => {
    const today = new Date();
    const monday = new Date(today);
    monday.setDate(today.getDate() - today.getDay() + 1);
    return monday.toISOString().split('T')[0];
  });
  const [expandedUsers, setExpandedUsers] = useState<Set<number>>(new Set());

  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingAllocationId, setEditingAllocationId] = useState<number | null>(null);
  const [utenti, setUtenti] = useState<UtenteListDTO[]>([]);
  const [progetti, setProgetti] = useState<Progetto[]>([]);
  const [formData, setFormData] = useState({
    utenteId: '',
    progettoId: '',
    percentualeImpegno: 100,
    dataInizio: '',
    dataFine: '',
    note: ''
  });
  const [formError, setFormError] = useState<string>('');
  const [submitting, setSubmitting] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  const loadGanttData = async () => {
    setLoading(true);
    try {
      const data = await utenteProgettoService.getGantt({
        groupBy,
        startDate,
        periods: groupBy === 'day' ? 30 : groupBy === 'week' ? 52 : groupBy === 'month' ? 12 : 3
      });
      setGanttData(data);
    } catch (error) {
      console.error('Errore nel caricamento dei dati Gantt:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadGanttData();
  }, [groupBy, startDate]);

  useEffect(() => {
    if (isModalOpen) {
      loadUtentiAndProgetti();
    }
  }, [isModalOpen]);

  const loadUtentiAndProgetti = async () => {
    try {
      const [utentiData, progettiData] = await Promise.all([
        utenteService.getAll(),
        progettoService.getAll()
      ]);
      setUtenti(utentiData);
      setProgetti(progettiData);
    } catch (error) {
      console.error('Errore nel caricamento di utenti e progetti:', error);
    }
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    setIsEditMode(false);
    setEditingAllocationId(null);
    setShowDeleteConfirm(false);
    setFormData({
      utenteId: '',
      progettoId: '',
      percentualeImpegno: 100,
      dataInizio: '',
      dataFine: '',
      note: ''
    });
    setFormError('');
  };

  const handleAllocationClick = async (allocation: any, utenteId: number) => {
    setIsEditMode(true);
    setEditingAllocationId(allocation.utenteProgettoId);

    // Pre-populate form with allocation data
    setFormData({
      utenteId: utenteId.toString(),
      progettoId: allocation.progettoId.toString(),
      percentualeImpegno: allocation.percentualeImpegno,
      dataInizio: allocation.dataInizio,
      dataFine: allocation.dataFine,
      note: allocation.note || ''
    });

    setIsModalOpen(true);
  };

  const handleFormChange = (field: string, value: string | number) => {
    setFormData(prev => ({ ...prev, [field]: value }));
    setFormError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormError('');

    // Client-side validation
    if (!formData.utenteId || !formData.progettoId || !formData.dataInizio || !formData.dataFine) {
      setFormError('Tutti i campi sono obbligatori');
      return;
    }

    if (formData.percentualeImpegno < 1 || formData.percentualeImpegno > 100) {
      setFormError('La percentuale deve essere tra 1 e 100');
      return;
    }

    if (formData.dataFine < formData.dataInizio) {
      setFormError('La data fine non può essere precedente alla data inizio');
      return;
    }

    setSubmitting(true);

    try {
      const allocation = {
        utente: { id: parseInt(formData.utenteId) },
        progetto: { id: parseInt(formData.progettoId) },
        percentualeImpegno: formData.percentualeImpegno,
        dataInizio: formData.dataInizio,
        dataFine: formData.dataFine,
        note: formData.note || undefined
      };

      if (isEditMode && editingAllocationId) {
        await utenteProgettoService.update(editingAllocationId, allocation as any);
      } else {
        await utenteProgettoService.create(allocation as any);
      }

      handleModalClose();
      loadGanttData();
    } catch (error: any) {
      console.error(`Errore nella ${isEditMode ? 'modifica' : 'creazione'} dell'allocazione:`, error);
      if (error.response?.status === 409) {
        setFormError('Esiste già un\'allocazione per questo utente e progetto nelle date specificate');
      } else if (error.response?.data?.message) {
        setFormError(error.response.data.message);
      } else {
        setFormError(`Errore nella ${isEditMode ? 'modifica' : 'creazione'} dell'allocazione`);
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!editingAllocationId) return;

    setSubmitting(true);
    try {
      await utenteProgettoService.delete(editingAllocationId);
      handleModalClose();
      loadGanttData();
    } catch (error: any) {
      console.error('Errore nella eliminazione dell\'allocazione:', error);
      setFormError('Errore nella eliminazione dell\'allocazione');
      setSubmitting(false);
      setShowDeleteConfirm(false);
    }
  };

  const navigatePeriod = (direction: 'prev' | 'next') => {
    const date = new Date(startDate);
    const multiplier = direction === 'prev' ? -1 : 1;

    if (groupBy === 'day') {
      date.setDate(date.getDate() + multiplier);
    } else if (groupBy === 'week') {
      date.setDate(date.getDate() + (7 * multiplier));
    } else if (groupBy === 'month') {
      date.setMonth(date.getMonth() + multiplier);
    } else if (groupBy === 'year') {
      date.setFullYear(date.getFullYear() + multiplier);
    }

    setStartDate(date.toISOString().split('T')[0]);
  };

  const goToToday = () => {
    const today = new Date();
    let newStartDate: Date;

    if (groupBy === 'day') {
      newStartDate = today;
    } else if (groupBy === 'week') {
      newStartDate = new Date(today);
      newStartDate.setDate(today.getDate() - today.getDay() + 1);
    } else if (groupBy === 'month') {
      newStartDate = new Date(today.getFullYear(), today.getMonth(), 1);
    } else {
      newStartDate = new Date(today.getFullYear(), 0, 1);
    }

    setStartDate(newStartDate.toISOString().split('T')[0]);
  };

  const toggleUserExpanded = (utenteId: number) => {
    setExpandedUsers(prev => {
      const newSet = new Set(prev);
      if (newSet.has(utenteId)) {
        newSet.delete(utenteId);
      } else {
        newSet.add(utenteId);
      }
      return newSet;
    });
  };

  const getCellColor = (percentuale: number): string => {
    if (percentuale === 0) return '';
    if (percentuale < 50) return 'green';
    if (percentuale < 100) return 'yellow';
    if (percentuale === 100) return 'blue';
    return 'red';
  };

  if (loading && !ganttData) {
    return (
      <div className="gantt-container">
        <div className="loading-message">Caricamento in corso...</div>
      </div>
    );
  }

  return (
    <div className="gantt-container">
      <div className="gantt-header">
        <h1>
          <Calendar className="title-icon" />
          Diagramma Risorse - Allocazione Utenti
        </h1>

        <div className="gantt-controls">
          <div className="time-selector">
            <button
              className={`time-btn ${groupBy === 'day' ? 'active' : ''}`}
              onClick={() => setGroupBy('day')}
            >
              Giorni
            </button>
            <button
              className={`time-btn ${groupBy === 'week' ? 'active' : ''}`}
              onClick={() => setGroupBy('week')}
            >
              Settimane
            </button>
            <button
              className={`time-btn ${groupBy === 'month' ? 'active' : ''}`}
              onClick={() => setGroupBy('month')}
            >
              Mesi
            </button>
            <button
              className={`time-btn ${groupBy === 'year' ? 'active' : ''}`}
              onClick={() => setGroupBy('year')}
            >
              Anni
            </button>
          </div>

          <div className="navigation-controls">
            <button className="nav-btn" onClick={() => navigatePeriod('prev')}>
              <ChevronLeft size={20} />
            </button>
            <button className="today-btn" onClick={goToToday}>
              Oggi
            </button>
            <button className="nav-btn" onClick={() => navigatePeriod('next')}>
              <ChevronRight size={20} />
            </button>
          </div>

          <button className="refresh-btn" onClick={loadGanttData}>
            Aggiorna
          </button>
          <button className="allocate-btn" onClick={() => setIsModalOpen(true)}>
            Alloca
          </button>
        </div>
      </div>

      <div className="legend">
        <div className="legend-item">
          <span className="legend-color green"></span>
          <span className="legend-label">1-49%</span>
        </div>
        <div className="legend-item">
          <span className="legend-color yellow"></span>
          <span className="legend-label">50-99%</span>
        </div>
        <div className="legend-item">
          <span className="legend-color blue"></span>
          <span className="legend-label">100%</span>
        </div>
        <div className="legend-item">
          <span className="legend-color red"></span>
          <span className="legend-label">Oltre 100%</span>
        </div>
      </div>

      {ganttData && (
        <div className="gantt-table-wrapper">
          <table className="gantt-table">
            <thead>
              <tr>
                <th className="sticky-col name-col">Nome</th>
                <th className="sticky-col role-col">Ruolo predefinito</th>
                {ganttData.periodi.map((periodo, idx) => (
                  <th key={idx} className="period-header">
                    {periodo.label}
                    <div className="period-dates">
                      {new Date(periodo.dataInizio).toLocaleDateString('it-IT', {
                        day: '2-digit',
                        month: '2-digit'
                      })}
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {ganttData.utenti.map((utente) => (
                <React.Fragment key={utente.utenteId}>
                  {/* User row with totals */}
                  <tr className="user-row">
                    <td className="sticky-col name-col">
                      <div className="user-name-cell">
                        <button
                          className="expand-btn"
                          onClick={() => toggleUserExpanded(utente.utenteId)}
                          aria-label={expandedUsers.has(utente.utenteId) ? "Collapse" : "Expand"}
                        >
                          {expandedUsers.has(utente.utenteId) ? (
                            <ChevronDown size={16} />
                          ) : (
                            <ChevronRightIcon size={16} />
                          )}
                        </button>
                        <span>{utente.cognome} {utente.nome}</span>
                      </div>
                    </td>
                    <td className="sticky-col role-col">{utente.ruoloPredefinito}</td>
                    {utente.periods.map((cell, idx) => (
                      <td
                        key={idx}
                        className={`gantt-cell ${getCellColor(cell.percentualeTotale)}`}
                      >
                        {cell.percentualeTotale > 0 && (
                          <div className="cell-content">
                            <span className="percentage">{cell.percentualeTotale}%</span>
                          </div>
                        )}
                      </td>
                    ))}
                  </tr>

                  {/* Project rows (indented, shown when expanded) */}
                  {expandedUsers.has(utente.utenteId) && utente.progetti.map((progetto) => (
                    <tr key={`${utente.utenteId}-${progetto.progettoId}`} className="project-row">
                      <td className="sticky-col name-col">
                        <div className="project-name-cell">
                          <span>{progetto.progettoNome}</span>
                        </div>
                      </td>
                      <td className="sticky-col role-col"></td>
                      {progetto.periods.map((cell, idx) => (
                        <td
                          key={idx}
                          className={`gantt-cell ${getCellColor(cell.percentualeTotale)} clickable-cell`}
                          onClick={() => {
                            if (cell.allocazioni.length > 0) {
                              handleAllocationClick(cell.allocazioni[0], utente.utenteId);
                            }
                          }}
                          title={cell.allocazioni.length > 0
                            ? `${cell.allocazioni[0].progettoNome}: ${cell.allocazioni[0].percentualeImpegno}%${
                                cell.allocazioni[0].note ? ` - ${cell.allocazioni[0].note}` : ''
                              }\nClicca per modificare`
                            : ''}
                        >
                          {cell.percentualeTotale > 0 && (
                            <div className="cell-content">
                              <span className="percentage">{cell.percentualeTotale}%</span>
                            </div>
                          )}
                        </td>
                      ))}
                    </tr>
                  ))}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {ganttData && ganttData.utenti.length === 0 && (
        <div className="no-data-message">
          Nessun utente attivo trovato
        </div>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={handleModalClose}
        title={isEditMode ? 'Modifica Allocazione' : 'Nuova Allocazione'}
        footer={
          <div style={{ display: 'flex', gap: '10px', justifyContent: 'space-between', width: '100%' }}>
            {isEditMode && !showDeleteConfirm && (
              <button
                className="btn-danger"
                onClick={() => setShowDeleteConfirm(true)}
                disabled={submitting}
                style={{ marginRight: 'auto' }}
              >
                Elimina
              </button>
            )}
            {showDeleteConfirm && (
              <div style={{ display: 'flex', gap: '10px', marginRight: 'auto', alignItems: 'center' }}>
                <span style={{ color: '#d32f2f', fontWeight: 'bold' }}>Confermi l'eliminazione?</span>
                <button className="btn-danger" onClick={handleDelete} disabled={submitting}>
                  {submitting ? 'Eliminazione...' : 'Sì, elimina'}
                </button>
                <button className="btn-secondary" onClick={() => setShowDeleteConfirm(false)} disabled={submitting}>
                  Annulla
                </button>
              </div>
            )}
            <div style={{ display: 'flex', gap: '10px', marginLeft: 'auto' }}>
              <button className="btn-secondary" onClick={handleModalClose} disabled={submitting}>
                {showDeleteConfirm ? 'Chiudi' : 'Annulla'}
              </button>
              {!showDeleteConfirm && (
                <button className="btn-primary" onClick={handleSubmit} disabled={submitting}>
                  {submitting ? 'Salvataggio...' : 'Salva'}
                </button>
              )}
            </div>
          </div>
        }
      >
        <form onSubmit={handleSubmit} className="allocation-form">
          {formError && (
            <div className="form-error">{formError}</div>
          )}

          <div className="form-group">
            <label htmlFor="utente">Utente *</label>
            <select
              id="utente"
              value={formData.utenteId}
              onChange={(e) => handleFormChange('utenteId', e.target.value)}
              required
              disabled={submitting}
            >
              <option value="">Seleziona un utente</option>
              {utenti.map((utente) => (
                <option key={utente.id} value={utente.id}>
                  {utente.cognome} {utente.nome}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="progetto">Progetto *</label>
            <select
              id="progetto"
              value={formData.progettoId}
              onChange={(e) => handleFormChange('progettoId', e.target.value)}
              required
              disabled={submitting}
            >
              <option value="">Seleziona un progetto</option>
              {progetti.map((progetto) => (
                <option key={progetto.id} value={progetto.id}>
                  {progetto.nome}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="percentuale">Percentuale Impegno (%) *</label>
            <input
              type="number"
              id="percentuale"
              min="1"
              max="100"
              value={formData.percentualeImpegno}
              onChange={(e) => handleFormChange('percentualeImpegno', parseInt(e.target.value) || 0)}
              required
              disabled={submitting}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="dataInizio">Data Inizio *</label>
              <input
                type="date"
                id="dataInizio"
                value={formData.dataInizio}
                onChange={(e) => handleFormChange('dataInizio', e.target.value)}
                required
                disabled={submitting}
              />
            </div>

            <div className="form-group">
              <label htmlFor="dataFine">Data Fine *</label>
              <input
                type="date"
                id="dataFine"
                value={formData.dataFine}
                onChange={(e) => handleFormChange('dataFine', e.target.value)}
                required
                disabled={submitting}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="note">Note</label>
            <textarea
              id="note"
              value={formData.note}
              onChange={(e) => handleFormChange('note', e.target.value)}
              rows={3}
              disabled={submitting}
              placeholder="Note opzionali sull'allocazione"
            />
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default GanttAllocazione;
