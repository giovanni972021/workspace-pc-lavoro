import React, { useState, useEffect } from 'react';
import { utenteProgettoService } from '../../services/utenteProgetto.service';
import { progettoService } from '../../services/progetto.service';
import type { GanttProgettiResponseDTO, GanttGroupBy, Progetto } from '../../types/index.js';
import { Calendar, ChevronLeft, ChevronRight, ChevronDown, ChevronRight as ChevronRightIcon } from 'lucide-react';
import Modal from '../../components/Modal';
import './GanttAllocazione.css';

const GanttAllocazioneProgetti: React.FC = () => {
  const [ganttData, setGanttData] = useState<GanttProgettiResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [groupBy, setGroupBy] = useState<GanttGroupBy>('week');
  const [startDate, setStartDate] = useState<string>(() => {
    const today = new Date();
    const monday = new Date(today);
    monday.setDate(today.getDate() - today.getDay() + 1);
    return monday.toISOString().split('T')[0];
  });
  const [expandedProjects, setExpandedProjects] = useState<Set<number>>(new Set());

  // Modal state for project details
  const [isProjectModalOpen, setIsProjectModalOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState<Progetto | null>(null);
  const [loadingProject, setLoadingProject] = useState(false);

  const loadGanttData = async () => {
    setLoading(true);
    try {
      const data = await utenteProgettoService.getGanttProgetti({
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

  const toggleProjectExpanded = (progettoId: number) => {
    setExpandedProjects(prev => {
      const newSet = new Set(prev);
      if (newSet.has(progettoId)) {
        newSet.delete(progettoId);
      } else {
        newSet.add(progettoId);
      }
      return newSet;
    });
  };

  const handleProjectClick = async (progettoId: number) => {
    setLoadingProject(true);
    setIsProjectModalOpen(true);
    try {
      const project = await progettoService.getById(progettoId);
      setSelectedProject(project);
    } catch (error) {
      console.error('Errore nel caricamento del progetto:', error);
    } finally {
      setLoadingProject(false);
    }
  };

  const handleModalClose = () => {
    setIsProjectModalOpen(false);
    setSelectedProject(null);
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
          Diagramma Allocazione - Progetti
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
                <th className="sticky-col name-col">Progetto</th>
                <th className="sticky-col role-col"></th>
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
              {ganttData.progetti.map((progetto) => (
                <React.Fragment key={progetto.progettoId}>
                  {/* Project row with totals */}
                  <tr className="user-row">
                    <td className="sticky-col name-col">
                      <div className="user-name-cell">
                        <button
                          className="expand-btn"
                          onClick={() => toggleProjectExpanded(progetto.progettoId)}
                          aria-label={expandedProjects.has(progetto.progettoId) ? "Collapse" : "Expand"}
                        >
                          {expandedProjects.has(progetto.progettoId) ? (
                            <ChevronDown size={16} />
                          ) : (
                            <ChevronRightIcon size={16} />
                          )}
                        </button>
                        <span
                          onClick={() => handleProjectClick(progetto.progettoId)}
                          style={{ cursor: 'pointer', textDecoration: 'underline' }}
                          title="Clicca per visualizzare dettagli progetto"
                        >
                          {progetto.progettoNome}
                        </span>
                      </div>
                    </td>
                    <td className="sticky-col role-col"></td>
                    {progetto.periods.map((cell, idx) => (
                      <td
                        key={idx}
                        className={`gantt-cell ${getCellColor(cell.percentualeTotale)}`}
                      >
                        {cell.percentualeTotale > 0 && (
                          <div className="cell-content">
                            <span className="percentage">{cell.percentualeTotale}%</span>
                            {cell.percentualeAllocazione !== undefined && cell.percentualeAllocazione > 0 && (
                              <span
                                className="allocation-percentage"
                                title="Percentuale allocazione: somma percentuali utenti / numero figure richieste"
                                style={{ display: 'block', fontSize: '0.85em', color: 'var(--text-secondary)' }}
                              >
                                ({cell.percentualeAllocazione.toFixed(2)}%)
                              </span>
                            )}
                          </div>
                        )}
                      </td>
                    ))}
                  </tr>

                  {/* User rows (indented, shown when expanded) */}
                  {expandedProjects.has(progetto.progettoId) && progetto.utenti.map((utente) => (
                    <tr key={`${progetto.progettoId}-${utente.utenteId}`} className="project-row">
                      <td className="sticky-col name-col">
                        <div className="project-name-cell">
                          <span>{utente.cognome} {utente.nome}</span>
                        </div>
                      </td>
                      <td className="sticky-col role-col"></td>
                      {utente.periods.map((cell, idx) => (
                        <td
                          key={idx}
                          className={`gantt-cell ${getCellColor(cell.percentualeTotale)}`}
                          title={cell.allocazioni.length > 0
                            ? `${utente.cognome} ${utente.nome}: ${cell.allocazioni[0].percentualeImpegno}%${
                                cell.allocazioni[0].note ? ` - ${cell.allocazioni[0].note}` : ''
                              }`
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

      {ganttData && ganttData.progetti.length === 0 && (
        <div className="no-data-message">
          Nessun progetto con allocazioni trovato
        </div>
      )}

      {/* Project Details Modal */}
      <Modal
        isOpen={isProjectModalOpen}
        onClose={handleModalClose}
        title="Dettagli Progetto"
      >
        {loadingProject ? (
          <div style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}>
            Caricamento...
          </div>
        ) : selectedProject ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <div>
              <strong style={{ color: 'var(--text-primary)' }}>Nome:</strong>
              <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                {selectedProject.nome}
              </div>
            </div>

            <div>
              <strong style={{ color: 'var(--text-primary)' }}>Descrizione:</strong>
              <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                {selectedProject.descrizione || 'N/A'}
              </div>
            </div>

            {selectedProject.stato && (
              <div>
                <strong style={{ color: 'var(--text-primary)' }}>Stato:</strong>
                <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                  {selectedProject.stato.nome}
                </div>
              </div>
            )}

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
              {selectedProject.dataInizioPresunta && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Data Inizio Presunta:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {new Date(selectedProject.dataInizioPresunta).toLocaleDateString('it-IT')}
                  </div>
                </div>
              )}

              {selectedProject.dataFinePresunta && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Data Fine Presunta:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {new Date(selectedProject.dataFinePresunta).toLocaleDateString('it-IT')}
                  </div>
                </div>
              )}

              {selectedProject.dataInizioEffettiva && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Data Inizio Effettiva:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {new Date(selectedProject.dataInizioEffettiva).toLocaleDateString('it-IT')}
                  </div>
                </div>
              )}

              {selectedProject.dataFineEffettiva && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Data Fine Effettiva:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {new Date(selectedProject.dataFineEffettiva).toLocaleDateString('it-IT')}
                  </div>
                </div>
              )}
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
              {selectedProject.budgetPreventivato !== undefined && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Budget Preventivato:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    € {selectedProject.budgetPreventivato.toLocaleString('it-IT')}
                  </div>
                </div>
              )}

              {selectedProject.budgetEffettivo !== undefined && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Budget Effettivo:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    € {selectedProject.budgetEffettivo.toLocaleString('it-IT')}
                  </div>
                </div>
              )}
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
              {selectedProject.priorita !== undefined && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Priorità:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {selectedProject.priorita}/100
                  </div>
                </div>
              )}

              {selectedProject.fattibilita !== undefined && (
                <div>
                  <strong style={{ color: 'var(--text-primary)' }}>Fattibilità:</strong>
                  <div style={{ marginTop: '4px', color: 'var(--text-secondary)' }}>
                    {selectedProject.fattibilita}/100
                  </div>
                </div>
              )}
            </div>
          </div>
        ) : (
          <div style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}>
            Errore nel caricamento del progetto
          </div>
        )}
      </Modal>
    </div>
  );
};

export default GanttAllocazioneProgetti;
