import React, { useState, useMemo, useEffect, useRef } from 'react';
import {
  GitBranch,
  Download,
  Calendar,
  Users,
  Code,
  GitMerge,
  AlertCircle,
  RefreshCw,
  TrendingUp,
  FileCode,
  FolderGit2,
  User,
} from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from 'recharts';
import * as XLSX from 'xlsx';
import { gitlabService } from '../../services/gitlab.service';
import type {
  GitLabStatsResponse,
  UserStats,
  UserProjectStats,
  MergeRequestStats,
  IssueStats,
  SimpleUtente,
  GitLabProject,
} from '../../types/index';
import './GitLabStats.css';

type ViewMode = 'byUser' | 'byUserProject';
type StatsType = 'commits' | 'mergeRequests' | 'issues';

const COLORS = ['#00d4aa', '#00b893', '#17a2b8', '#6c757d', '#ffc107', '#dc3545', '#28a745', '#6f42c1', '#e83e8c', '#fd7e14'];

const GitLabStats: React.FC = () => {
  const [startDate, setStartDate] = useState<string>(() => {
    const date = new Date();
    date.setMonth(date.getMonth() - 1);
    return date.toISOString().split('T')[0];
  });
  const [endDate, setEndDate] = useState<string>(() => new Date().toISOString().split('T')[0]);
  const [viewMode, setViewMode] = useState<ViewMode>('byUser');
  const [statsType, setStatsType] = useState<StatsType>('commits');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [commitStats, setCommitStats] = useState<GitLabStatsResponse | null>(null);
  const [mrStats, setMrStats] = useState<MergeRequestStats[]>([]);
  const [issueStats, setIssueStats] = useState<IssueStats[]>([]);
  const [pushStats, setPushStats] = useState<any[]>([]);

  // Filtri utente e progetto
  const [utenti, setUtenti] = useState<SimpleUtente[]>([]);
  const [progetti, setProgetti] = useState<GitLabProject[]>([]);
  const [selectedUtente, setSelectedUtente] = useState<SimpleUtente | null>(
    null
  );
  const [selectedProgetto, setSelectedProgetto] =
    useState<GitLabProject | null>(null);
  const [utenteSearch, setUtenteSearch] = useState("");
  const [progettoSearch, setProgettoSearch] = useState("");
  const [loadingUtenti, setLoadingUtenti] = useState(false);
  const [loadingProgetti, setLoadingProgetti] = useState(false);
  const [showUtenteDropdown, setShowUtenteDropdown] = useState(false);
  const [showProgettoDropdown, setShowProgettoDropdown] = useState(false);
  const utenteInputRef = useRef<HTMLInputElement>(null);
  const progettoInputRef = useRef<HTMLInputElement>(null);

  // Carica utenti quando si digita
  useEffect(() => {
    const loadUtenti = async () => {
      if (utenteSearch.length < 1) {
        setUtenti([]);
        return;
      }
      setLoadingUtenti(true);
      try {
        const data = await gitlabService.getUtenti(utenteSearch);
        setUtenti(data);
      } catch (err) {
        console.error("Errore caricamento utenti:", err);
      } finally {
        setLoadingUtenti(false);
      }
    };

    const debounce = setTimeout(loadUtenti, 300);
    return () => clearTimeout(debounce);
  }, [utenteSearch]);

  // Carica progetti GitLab
  const loadProgetti = async () => {
    setLoadingProgetti(true);
    try {
      const data = await gitlabService.getProgettiGitLab();
      setProgetti(data);
    } catch (err) {
      console.error("Errore caricamento progetti:", err);
    } finally {
      setLoadingProgetti(false);
    }
  };

  // Filtra progetti in base alla ricerca
  const filteredProgetti = useMemo(() => {
    if (!progettoSearch) return progetti;
    const search = progettoSearch.toLowerCase();
    return progetti.filter(
      (p) =>
        (p.name && p.name.toLowerCase().includes(search)) ||
        (p.nameWithNamespace &&
          p.nameWithNamespace.toLowerCase().includes(search))
    );
  }, [progetti, progettoSearch]);

  const loadStats = async () => {
    if (!startDate || !endDate) {
      setError("Seleziona data inizio e data fine");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const params = {
        startDate,
        endDate,
        userEmail: selectedUtente?.email || undefined,
        projectId: selectedProgetto?.id || undefined,
      };

      const [commits, mrs, issues, pushData] = await Promise.all([
        gitlabService.getCommitStats(params),
        gitlabService.getMergeRequestStats(params),
        gitlabService.getIssueStats(params),
        gitlabService.getPushEventsStats(params), // Questo chiama la tua query SQL
      ]);

      setCommitStats(commits);
      setMrStats(mrs);
      setIssueStats(issues);
      setPushStats(pushData);
    } catch (err: any) {
      setError(err.message || "Errore nel caricamento delle statistiche");
    } finally {
      setLoading(false);
    }
  };

  // Aggregate MR stats by user
  const mrStatsByUser = useMemo(() => {
    const map = new Map<string, { name: string; email: string; opened: number; merged: number; closed: number; total: number }>();
    mrStats.forEach(s => {
      const existing = map.get(s.userEmail) || { name: s.userName, email: s.userEmail, opened: 0, merged: 0, closed: 0, total: 0 };
      existing.opened += s.opened;
      existing.merged += s.merged;
      existing.closed += s.closed;
      existing.total += s.total;
      map.set(s.userEmail, existing);
    });
    return Array.from(map.values()).sort((a, b) => b.total - a.total);
  }, [mrStats]);

  // Aggregate Issue stats by user
  const issueStatsByUser = useMemo(() => {
    const map = new Map<string, { name: string; email: string; opened: number; closed: number; assigned: number; total: number }>();
    issueStats.forEach(s => {
      const existing = map.get(s.userEmail) || { name: s.userName, email: s.userEmail, opened: 0, closed: 0, assigned: 0, total: 0 };
      existing.opened += s.opened;
      existing.closed += s.closed;
      existing.assigned += s.assigned;
      existing.total += s.total;
      map.set(s.userEmail, existing);
    });
    return Array.from(map.values()).sort((a, b) => b.total - a.total);
  }, [issueStats]);

  const chartData = useMemo(() => {
    if (statsType === 'commits') {
      if (viewMode === 'byUser') {
        return (commitStats?.userStats || []).map(s => ({
          name: s.name,
          'Righe Aggiunte': s.additions,
          'Righe Rimosse': s.deletions,
          'Totale Righe': s.totalLines,
        }));
      } else {
        return (commitStats?.userProjectStats || []).map(s => ({
          name: `${s.userName} - ${s.projectName}`,
          'Righe Aggiunte': s.additions,
          'Righe Rimosse': s.deletions,
          'Totale Righe': s.totalLines,
        }));
      }
    } else if (statsType === 'mergeRequests') {
      if (viewMode === 'byUser') {
        return mrStatsByUser.map(s => ({
          name: s.name,
          'Aperte': s.opened,
          'Merged': s.merged,
          'Chiuse': s.closed,
        }));
      } else {
        return mrStats.map(s => ({
          name: `${s.userName} - ${s.projectName}`,
          'Aperte': s.opened,
          'Merged': s.merged,
          'Chiuse': s.closed,
        }));
      }
    } else {
      if (viewMode === 'byUser') {
        return issueStatsByUser.map(s => ({
          name: s.name,
          'Aperte': s.opened,
          'Chiuse': s.closed,
          'Assegnate': s.assigned,
        }));
      } else {
        return issueStats.map(s => ({
          name: `${s.userName} - ${s.projectName}`,
          'Aperte': s.opened,
          'Chiuse': s.closed,
          'Assegnate': s.assigned,
        }));
      }
    }
  }, [commitStats, mrStats, issueStats, mrStatsByUser, issueStatsByUser, viewMode, statsType]);

  const exportToExcel = () => {
    const wb = XLSX.utils.book_new();

    // Export commit stats
    if (commitStats) {
      const userSheet = XLSX.utils.json_to_sheet(commitStats.userStats.map(s => ({
        'Nome': s.name,
        'Email': s.email,
        'Commit': s.commits,
        'Righe Aggiunte': s.additions,
        'Righe Rimosse': s.deletions,
        'Totale Righe': s.totalLines,
      })));
      XLSX.utils.book_append_sheet(wb, userSheet, 'Commit per Utente');

      const userProjectSheet = XLSX.utils.json_to_sheet(commitStats.userProjectStats.map(s => ({
        'Nome': s.userName,
        'Email': s.userEmail,
        'Progetto': s.projectName,
        'Commit': s.commits,
        'Righe Aggiunte': s.additions,
        'Righe Rimosse': s.deletions,
        'Totale Righe': s.totalLines,
      })));
      XLSX.utils.book_append_sheet(wb, userProjectSheet, 'Commit per Utente-Progetto');
    }

    // Export MR stats
    if (mrStats.length > 0) {
      const mrUserSheet = XLSX.utils.json_to_sheet(mrStatsByUser.map(s => ({
        'Nome': s.name,
        'Email': s.email,
        'Aperte': s.opened,
        'Merged': s.merged,
        'Chiuse': s.closed,
        'Totale': s.total,
      })));
      XLSX.utils.book_append_sheet(wb, mrUserSheet, 'MR per Utente');

      const mrProjectSheet = XLSX.utils.json_to_sheet(mrStats.map(s => ({
        'Nome': s.userName,
        'Email': s.userEmail,
        'Progetto': s.projectName,
        'Aperte': s.opened,
        'Merged': s.merged,
        'Chiuse': s.closed,
        'Totale': s.total,
      })));
      XLSX.utils.book_append_sheet(wb, mrProjectSheet, 'MR per Utente-Progetto');
    }

    // Export Issue stats
    if (issueStats.length > 0) {
      const issueUserSheet = XLSX.utils.json_to_sheet(issueStatsByUser.map(s => ({
        'Nome': s.name,
        'Email': s.email,
        'Aperte': s.opened,
        'Chiuse': s.closed,
        'Assegnate': s.assigned,
        'Totale': s.total,
      })));
      XLSX.utils.book_append_sheet(wb, issueUserSheet, 'Issue per Utente');

      const issueProjectSheet = XLSX.utils.json_to_sheet(issueStats.map(s => ({
        'Nome': s.userName,
        'Email': s.userEmail,
        'Progetto': s.projectName,
        'Aperte': s.opened,
        'Chiuse': s.closed,
        'Assegnate': s.assigned,
        'Totale': s.total,
      })));
      XLSX.utils.book_append_sheet(wb, issueProjectSheet, 'Issue per Utente-Progetto');
    }

    XLSX.writeFile(wb, `gitlab-stats-${startDate}-${endDate}.xlsx`);
  };

  const renderTable = () => {
    if (statsType === 'commits') {
      const data = viewMode === 'byUser' ? commitStats?.userStats : commitStats?.userProjectStats;
      if (!data || data.length === 0) return <p>Nessun dato disponibile</p>;

      return (
        <table className="stats-table">
          <thead>
            <tr>
              <th>Nome</th>
              {viewMode === 'byUserProject' && <th>Progetto</th>}
              <th>Commit</th>
              <th>Righe Aggiunte</th>
              <th>Righe Rimosse</th>
              <th>Totale Righe</th>
            </tr>
          </thead>
          <tbody>
            {viewMode === 'byUser'
              ? (data as UserStats[]).map((s, i) => (
                  <tr key={i}>
                    <td>{s.name}</td>
                    <td>{s.commits.toLocaleString()}</td>
                    <td className="additions">+{s.additions.toLocaleString()}</td>
                    <td className="deletions">-{s.deletions.toLocaleString()}</td>
                    <td className="total">{s.totalLines.toLocaleString()}</td>
                  </tr>
                ))
              : (data as UserProjectStats[]).map((s, i) => (
                  <tr key={i}>
                    <td>{s.userName}</td>
                    <td>{s.projectName}</td>
                    <td>{s.commits.toLocaleString()}</td>
                    <td className="additions">+{s.additions.toLocaleString()}</td>
                    <td className="deletions">-{s.deletions.toLocaleString()}</td>
                    <td className="total">{s.totalLines.toLocaleString()}</td>
                  </tr>
                ))}
          </tbody>
        </table>
      );
    } else if (statsType === 'mergeRequests') {
      const data = viewMode === 'byUser' ? mrStatsByUser : mrStats;
      if (!data || data.length === 0) return <p>Nessun dato disponibile</p>;

      return (
        <table className="stats-table">
          <thead>
            <tr>
              <th>Nome</th>
              {viewMode === 'byUserProject' && <th>Progetto</th>}
              <th>Aperte</th>
              <th>Merged</th>
              <th>Chiuse</th>
              <th>Totale</th>
            </tr>
          </thead>
          <tbody>
            {data.map((s, i) => (
              <tr key={i}>
                <td>{viewMode === 'byUser' ? s.name : (s as MergeRequestStats).userName}</td>
                {viewMode === 'byUserProject' && <td>{(s as MergeRequestStats).projectName}</td>}
                <td>{s.opened}</td>
                <td className="merged">{s.merged}</td>
                <td>{s.closed}</td>
                <td className="total">{s.total}</td>
              </tr>
            ))}
          </tbody>
        </table>
      );
    } else {
      const data = viewMode === 'byUser' ? issueStatsByUser : issueStats;
      if (!data || data.length === 0) return <p>Nessun dato disponibile</p>;

      return (
        <table className="stats-table">
          <thead>
            <tr>
              <th>Nome</th>
              {viewMode === 'byUserProject' && <th>Progetto</th>}
              <th>Aperte</th>
              <th>Chiuse</th>
              <th>Assegnate</th>
              <th>Totale</th>
            </tr>
          </thead>
          <tbody>
            {data.map((s, i) => (
              <tr key={i}>
                <td>{viewMode === 'byUser' ? s.name : (s as IssueStats).userName}</td>
                {viewMode === 'byUserProject' && <td>{(s as IssueStats).projectName}</td>}
                <td>{s.opened}</td>
                <td className="closed">{s.closed}</td>
                <td>{s.assigned}</td>
                <td className="total">{s.total}</td>
              </tr>
            ))}
          </tbody>
        </table>
      );
    }
  };

  const getChartBars = () => {
    if (statsType === 'commits') {
      return (
        <>
          <Bar dataKey="Righe Aggiunte" fill="#28a745" />
          <Bar dataKey="Righe Rimosse" fill="#dc3545" />
        </>
      );
    } else if (statsType === 'mergeRequests') {
      return (
        <>
          <Bar dataKey="Aperte" fill="#17a2b8" />
          <Bar dataKey="Merged" fill="#28a745" />
          <Bar dataKey="Chiuse" fill="#6c757d" />
        </>
      );
    } else {
      return (
        <>
          <Bar dataKey="Aperte" fill="#17a2b8" />
          <Bar dataKey="Chiuse" fill="#28a745" />
          <Bar dataKey="Assegnate" fill="#ffc107" />
        </>
      );
    }
  };

  return (
    <div className="gitlab-stats-container">
      <div className="page-header">
        <h1>
          <GitBranch size={32} />
          Statistiche GitLab
        </h1>
      </div>

      {/* Tabella Vuota di Prova */}
      <div className="table-card" style={{ marginBottom: "20px" }}>
        <h3>Push events per utente e progetto</h3>
        <div className="table-container">
          <table className="stats-table">
            <thead>
              <tr>
                <th>username</th>
                <th>user_name</th>
                <th>project_name</th>
                <th>namespace_path</th>
                <th>full_project_path</th>
                <th>total_push_events</th>
                <th>total_commits</th>
                <th>first_push</th>
                <th>last_push</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td
                  colSpan={9}
                  style={{ textAlign: "center", padding: "20px" }}
                >
                  Nessun dato presente - Tabella in attesa di integrazione
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      {/* Filters */}
      <div className="filters-card">
        <div className="filters-row">
          <div className="filter-group">
            <label>
              <Calendar size={16} />
              Data Inizio
            </label>
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label>
              <Calendar size={16} />
              Data Fine
            </label>
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>

          {/* Filtro Utente con autocomplete */}
          <div className="filter-group autocomplete-container">
            <label>
              <User size={16} />
              Utente
            </label>
            <div className="autocomplete-input-wrapper">
              <input
                ref={utenteInputRef}
                type="text"
                placeholder="Cerca utente..."
                value={utenteSearch}
                onChange={(e) => {
                  setUtenteSearch(e.target.value);
                  setShowUtenteDropdown(true);
                  if (!e.target.value) {
                    setSelectedUtente(null);
                  }
                }}
                onFocus={() => setShowUtenteDropdown(true)}
                onBlur={() =>
                  setTimeout(() => setShowUtenteDropdown(false), 200)
                }
              />
              {selectedUtente && (
                <button
                  className="clear-selection"
                  onClick={() => {
                    setSelectedUtente(null);
                    setUtenteSearch("");
                  }}
                  title="Rimuovi filtro"
                >
                  ×
                </button>
              )}
              <button
                className="refresh-list-btn"
                onClick={async () => {
                  if (utenteSearch) {
                    setLoadingUtenti(true);
                    try {
                      const data = await gitlabService.getUtenti(utenteSearch);
                      setUtenti(data);
                      setShowUtenteDropdown(true);
                    } catch (err) {
                      console.error("Errore:", err);
                    } finally {
                      setLoadingUtenti(false);
                    }
                  }
                }}
                title="Aggiorna lista utenti"
              >
                <RefreshCw
                  size={14}
                  className={loadingUtenti ? "spinning" : ""}
                />
              </button>
            </div>
            {showUtenteDropdown && utenti.length > 0 && (
              <div className="autocomplete-dropdown">
                {utenti.map((u) => (
                  <div
                    key={u.id}
                    className="autocomplete-item"
                    onClick={() => {
                      setSelectedUtente(u);
                      setUtenteSearch(`${u.nome} ${u.cognome}`);
                      setShowUtenteDropdown(false);
                    }}
                  >
                    <span className="item-name">
                      {u.nome} {u.cognome}
                    </span>
                    {u.email && <span className="item-email">{u.email}</span>}
                  </div>
                ))}
              </div>
            )}
            {showUtenteDropdown && loadingUtenti && (
              <div className="autocomplete-dropdown">
                <div className="autocomplete-loading">Caricamento...</div>
              </div>
            )}
          </div>

          {/* Filtro Progetto GitLab con autocomplete */}
          <div className="filter-group autocomplete-container">
            <label>
              <FolderGit2 size={16} />
              Progetto GitLab
            </label>
            <div className="autocomplete-input-wrapper">
              <input
                ref={progettoInputRef}
                type="text"
                placeholder="Cerca progetto..."
                value={progettoSearch}
                onChange={(e) => {
                  setProgettoSearch(e.target.value);
                  setShowProgettoDropdown(true);
                  if (!e.target.value) {
                    setSelectedProgetto(null);
                  }
                }}
                onFocus={() => {
                  setShowProgettoDropdown(true);
                  if (progetti.length === 0) {
                    loadProgetti();
                  }
                }}
                onBlur={() =>
                  setTimeout(() => setShowProgettoDropdown(false), 200)
                }
              />
              {selectedProgetto && (
                <button
                  className="clear-selection"
                  onClick={() => {
                    setSelectedProgetto(null);
                    setProgettoSearch("");
                  }}
                  title="Rimuovi filtro"
                >
                  ×
                </button>
              )}
              <button
                className="refresh-list-btn"
                onClick={() => loadProgetti()}
                title="Aggiorna lista progetti"
              >
                <RefreshCw
                  size={14}
                  className={loadingProgetti ? "spinning" : ""}
                />
              </button>
            </div>
            {showProgettoDropdown && filteredProgetti.length > 0 && (
              <div className="autocomplete-dropdown">
                {filteredProgetti.slice(0, 20).map((p) => (
                  <div
                    key={p.id}
                    className="autocomplete-item"
                    onClick={() => {
                      setSelectedProgetto(p);
                      setProgettoSearch(p.name);
                      setShowProgettoDropdown(false);
                    }}
                  >
                    <span className="item-name">{p.name}</span>
                    <span className="item-email">{p.nameWithNamespace}</span>
                  </div>
                ))}
              </div>
            )}
            {showProgettoDropdown && loadingProgetti && (
              <div className="autocomplete-dropdown">
                <div className="autocomplete-loading">Caricamento...</div>
              </div>
            )}
          </div>

          <div className="filter-group">
            <label>
              <TrendingUp size={16} />
              Tipo Statistiche
            </label>
            <select
              value={statsType}
              onChange={(e) => setStatsType(e.target.value as StatsType)}
            >
              <option value="commits">Commit & Righe di Codice</option>
              <option value="mergeRequests">Merge Request</option>
              <option value="issues">Issue</option>
            </select>
          </div>

          <div className="filter-group">
            <label>
              <Users size={16} />
              Vista
            </label>
            <select
              value={viewMode}
              onChange={(e) => setViewMode(e.target.value as ViewMode)}
            >
              <option value="byUser">Totale per Utente</option>
              <option value="byUserProject">Per Utente e Progetto</option>
            </select>
          </div>

          <button
            className="btn btn-primary"
            onClick={loadStats}
            disabled={loading}
          >
            {loading ? (
              <RefreshCw size={16} className="spinning" />
            ) : (
              <RefreshCw size={16} />
            )}
            Carica
          </button>

          {(commitStats || mrStats.length > 0 || issueStats.length > 0) && (
            <button className="btn btn-secondary" onClick={exportToExcel}>
              <Download size={16} />
              Esporta Excel
            </button>
          )}
        </div>
      </div>

      {error && (
        <div className="error-message">
          <AlertCircle size={16} />
          {error}
        </div>
      )}

      {loading && (
        <div className="loading-container">
          <RefreshCw size={32} className="spinning" />
          <p>Caricamento statistiche in corso...</p>
          <p className="loading-hint">
            L'operazione potrebbe richiedere alcuni minuti
          </p>
        </div>
      )}

      {/* Summary Cards */}
      {commitStats && !loading && (
        <div className="summary-cards">
          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#00d4aa" }}>
              <FileCode size={24} />
            </div>
            <div className="summary-content">
              <h3>{commitStats.totalProjects}</h3>
              <p>Progetti</p>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#17a2b8" }}>
              <Code size={24} />
            </div>
            <div className="summary-content">
              <h3>{commitStats.totalCommits.toLocaleString()}</h3>
              <p>Commit Totali</p>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#28a745" }}>
              <TrendingUp size={24} />
            </div>
            <div className="summary-content">
              <h3>+{commitStats.totalAdditions.toLocaleString()}</h3>
              <p>Righe Aggiunte</p>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#dc3545" }}>
              <TrendingUp size={24} />
            </div>
            <div className="summary-content">
              <h3>-{commitStats.totalDeletions.toLocaleString()}</h3>
              <p>Righe Rimosse</p>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#6f42c1" }}>
              <GitMerge size={24} />
            </div>
            <div className="summary-content">
              <h3>{mrStatsByUser.reduce((acc, s) => acc + s.total, 0)}</h3>
              <p>Merge Request</p>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon" style={{ background: "#ffc107" }}>
              <AlertCircle size={24} />
            </div>
            <div className="summary-content">
              <h3>{issueStatsByUser.reduce((acc, s) => acc + s.total, 0)}</h3>
              <p>Issue</p>
            </div>
          </div>
        </div>
      )}

      {/* Chart */}
      {chartData.length > 0 && !loading && (
        <div className="chart-card">
          <h3>
            {statsType === "commits" && "Righe di Codice Modificate"}
            {statsType === "mergeRequests" && "Merge Request"}
            {statsType === "issues" && "Issue"}
            {" - "}
            {viewMode === "byUser" ? "Per Utente" : "Per Utente e Progetto"}
            {chartData.length > 30 && ` (Top 30 di ${chartData.length})`}
          </h3>
          <ResponsiveContainer
            width="100%"
            height={Math.min(
              400 + (Math.min(chartData.length, 30) - 10) * 15,
              600
            )}
          >
            <BarChart
              data={chartData.slice(0, 30)}
              margin={{ top: 20, right: 30, left: 20, bottom: 120 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                angle={-45}
                textAnchor="end"
                height={120}
                interval={0}
                tick={{ fontSize: 10 }}
              />
              <YAxis />
              <Tooltip />
              <Legend />
              {getChartBars()}
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}

      {/* Table */}
      {(commitStats || mrStats.length > 0 || issueStats.length > 0) &&
        !loading && (
          <div className="table-card">
            <h3>
              Dettaglio {statsType === "commits" && "Commit"}
              {statsType === "mergeRequests" && "Merge Request"}
              {statsType === "issues" && "Issue"}
            </h3>
            <div className="table-container">{renderTable()}</div>
          </div>
        )}
    </div>
  );
};

export default GitLabStats;
