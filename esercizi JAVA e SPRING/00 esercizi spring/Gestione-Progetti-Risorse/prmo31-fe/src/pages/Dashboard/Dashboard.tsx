import React, { useState, useEffect } from 'react';
import { Users, Briefcase, Building2, Award, TrendingUp, FileText } from 'lucide-react';
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import { utenteService } from '../../services/utente.service';
import { progettoService } from '../../services/progetto.service';
import { clienteService } from '../../services/cliente.service';
import { skillService } from '../../services/skill.service';
import { curriculumService } from '../../services/curriculum.service';
import type { ProgettoAllocazioneDTO } from '../../types/index';
import './Dashboard.css';

interface DashboardStats {
  totalUtenti: number;
  totalProgetti: number;
  totalClienti: number;
  totalSkills: number;
  totalCurricula: number;
  utentiAttivi: number;
  progettiInCorso: number;
}

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>({
    totalUtenti: 0,
    totalProgetti: 0,
    totalClienti: 0,
    totalSkills: 0,
    totalCurricula: 0,
    utentiAttivi: 0,
    progettiInCorso: 0,
  });
  const [loading, setLoading] = useState(true);
  const [skillsData, setSkillsData] = useState<any[]>([]);
  const [progettiData, setProgettiData] = useState<any[]>([]);
  const [allocazioniData, setAllocazioniData] = useState<ProgettoAllocazioneDTO[]>([]);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);

      const [utenti, progetti, clienti, skills, curricula, allocazioni] = await Promise.all([
        utenteService.getAll(),
        progettoService.getAll(),
        clienteService.getAll(),
        skillService.getAll(),
        curriculumService.getAll(),
        progettoService.getAllocazioni(),
      ]);

      const utentiAttivi = utenti.filter((u) => u.attivo).length;
      const progettiInCorso = progetti.filter((p) =>
        p.stato && typeof p.stato === 'object' && 'nome' in p.stato
          ? p.stato.nome === 'IN_CORSO'
          : p.stato === 'IN_CORSO'
      ).length;

      setStats({
        totalUtenti: utenti.length,
        totalProgetti: progetti.length,
        totalClienti: clienti.length,
        totalSkills: skills.length,
        totalCurricula: curricula.length,
        utentiAttivi,
        progettiInCorso,
      });

      // Raggruppa skills per categoria
      const skillsByCategory: { [key: string]: number } = {};
      skills.forEach((skill) => {
        const cat = skill.categoria || 'Altro';
        skillsByCategory[cat] = (skillsByCategory[cat] || 0) + 1;
      });

      const skillsChartData = Object.entries(skillsByCategory).map(([name, value]) => ({
        name,
        value,
      }));
      setSkillsData(skillsChartData);

      // Raggruppa progetti per stato
      const progettiByStato: { [key: string]: number } = {};
      progetti.forEach((progetto) => {
        let statoNome = 'Non definito';
        if (progetto.stato) {
          statoNome = typeof progetto.stato === 'object' && 'nome' in progetto.stato
            ? progetto.stato.nome
            : String(progetto.stato);
        }
        progettiByStato[statoNome] = (progettiByStato[statoNome] || 0) + 1;
      });

      const progettiChartData = Object.entries(progettiByStato).map(([name, count]) => ({
        name,
        count,
      }));
      setProgettiData(progettiChartData);

      // Imposta dati allocazioni progetti
      setAllocazioniData(allocazioni);
    } catch (error) {
      console.error('Errore nel caricamento dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  const COLORS = ['#00d4aa', '#00b893', '#17a2b8', '#6c757d', '#ffc107', '#dc3545'];

  if (loading) {
    return <div className="page-container">Caricamento dashboard...</div>;
  }

  return (
    <div className="dashboard-container">
      <div className="page-header">
        <h1>Dashboard</h1>
      </div>

      {/* Stats Cards */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#00d4aa' }}>
            <Users size={32} />
          </div>
          <div className="stat-content">
            <h3>{stats.totalUtenti}</h3>
            <p>Utenti Totali</p>
            <span className="stat-detail">{stats.utentiAttivi} attivi</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#17a2b8' }}>
            <Briefcase size={32} />
          </div>
          <div className="stat-content">
            <h3>{stats.totalProgetti}</h3>
            <p>Progetti</p>
            <span className="stat-detail">{stats.progettiInCorso} in corso</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#6c757d' }}>
            <Building2 size={32} />
          </div>
          <div className="stat-content">
            <h3>{stats.totalClienti}</h3>
            <p>Clienti</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#ffc107' }}>
            <TrendingUp size={32} />
          </div>
          <div className="stat-content">
            <h3>{stats.totalSkills}</h3>
            <p>Skills</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#28a745' }}>
            <FileText size={32} />
          </div>
          <div className="stat-content">
            <h3>{stats.totalCurricula}</h3>
            <p>Curricula</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#dc3545' }}>
            <Award size={32} />
          </div>
          <div className="stat-content">
            <h3>
              {stats.totalUtenti > 0
                ? Math.round((stats.utentiAttivi / stats.totalUtenti) * 100)
                : 0}
              %
            </h3>
            <p>Tasso Attività</p>
          </div>
        </div>
      </div>

      {/* Charts */}
      <div className="charts-grid">
        <div className="chart-card chart-card-highlight">
          <h3>Allocazione Risorse per Progetto</h3>
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={allocazioniData} margin={{ top: 20, right: 30, left: 20, bottom: 80 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis
                dataKey="progettoNome"
                angle={-45}
                textAnchor="end"
                height={100}
                interval={0}
              />
              <YAxis
                label={{ value: 'Percentuale Allocazione (%)', angle: -90, position: 'insideLeft' }}
              />
              <Tooltip
                content={({ active, payload }) => {
                  if (active && payload && payload.length) {
                    const data = payload[0].payload as ProgettoAllocazioneDTO;
                    return (
                      <div className="custom-tooltip">
                        <p className="label"><strong>{data.progettoNome}</strong></p>
                        <p>Allocazione: {data.percentualeAllocazione.toFixed(2)}%</p>
                        <p>Risorse allocate: {data.numeroRisorseAllocate}/{data.numeroRisorseRichieste}</p>
                      </div>
                    );
                  }
                  return null;
                }}
              />
              <Legend />
              <Bar
                dataKey="percentualeAllocazione"
                fill="#00d4aa"
                name="% Allocazione"
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Additional Charts */}
      <div className="charts-grid">
        <div className="chart-card">
          <h3>Progetti per Stato</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={progettiData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="count" fill="#00d4aa" name="Numero Progetti" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="chart-card">
          <h3>Skills per Categoria</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={skillsData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${percent ? (percent * 100).toFixed(0) : 0}%`}
                outerRadius={100}
                fill="#8884d8"
                dataKey="value"
              >
                {skillsData.map((_entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="quick-actions">
        <h3>Azioni Rapide</h3>
        <div className="actions-grid">
          <a href="/utenti/new" className="action-link">
            <Users size={24} />
            <span>Nuovo Utente</span>
          </a>
          <a href="/progetti/new" className="action-link">
            <Briefcase size={24} />
            <span>Nuovo Progetto</span>
          </a>
          <a href="/clienti/new" className="action-link">
            <Building2 size={24} />
            <span>Nuovo Cliente</span>
          </a>
          <a href="/curricula/new" className="action-link">
            <FileText size={24} />
            <span>Nuovo Curriculum</span>
          </a>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
