import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider } from './contexts/ThemeContext';
import { AuthProvider } from './contexts/AuthContext';
import MainLayout from './layouts/MainLayout';
import Login from './pages/Auth/Login';
import Callback from './pages/Auth/Callback';
import ProtectedRoute from './components/ProtectedRoute';

// Dashboard
import Dashboard from './pages/Dashboard/Dashboard';

// Utenti
import UtentiList from './pages/Utenti/UtentiList';
import UtenteForm from './pages/Utenti/UtenteForm';
import UtenteDetail from './pages/Utenti/UtenteDetail';

// Progetti
import ProgettiList from './pages/Progetti/ProgettiList';
import ProgettoForm from './pages/Progetti/ProgettoForm';

// Clienti
import ClientiList from './pages/Clienti/ClientiList';
import ClienteForm from './pages/Clienti/ClienteForm';

// Certificazioni
import CertificazioniList from './pages/Certificazioni/CertificazioniList';

// Skills
import SkillsList from './pages/Skills/SkillsList';

// Soft Skills
import SoftSkillsList from './pages/SoftSkills/SoftSkillsList';

// Curricula
import CurriculaList from './pages/Curricula/CurriculaList';
import CurriculumForm from './pages/Curricula/CurriculumForm';
import CurriculumDetail from './pages/Curricula/CurriculumDetail';

// Configurazione - Tipi Utente
import TipiUtenteList from './pages/TipiUtente/TipiUtenteList';
import TipoUtenteForm from './pages/TipiUtente/TipoUtenteForm';

// Configurazione - Job Titles
import JobTitlesList from './pages/JobTitles/JobTitlesList';
import JobTitleForm from './pages/JobTitles/JobTitleForm';

// Configurazione - Tipi Contatto
import TipiContattoList from './pages/TipiContatto/TipiContattoList';
import TipoContattoForm from './pages/TipiContatto/TipoContattoForm';

// Configurazione - Stati Progetto
import StatiProgettoList from './pages/StatiProgetto/StatiProgettoList';
import StatoProgettoForm from './pages/StatiProgetto/StatoProgettoForm';

// Configurazione - Profili
import ProfiliList from './pages/Profili/ProfiliList';
import ProfiloForm from './pages/Profili/ProfiloForm';

// Gantt - Allocazione Utenti
import GanttAllocazione from './pages/Gantt/GanttAllocazione';
import GanttAllocazioneProgetti from './pages/Gantt/GanttAllocazioneProgetti';

// GitLab Statistics
import GitLabStats from './pages/GitLab/GitLabStats';

import './App.css';
import './styles/theme.css';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Routes>
              {/* Public routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/callback" element={<Callback />} />

              {/* Protected routes */}
              <Route path="/" element={<ProtectedRoute><MainLayout /></ProtectedRoute>}>
                <Route index element={<Navigate to="/dashboard" replace />} />

              {/* Dashboard */}
              <Route path="dashboard" element={<Dashboard />} />

              {/* Utenti Routes */}
              <Route path="utenti" element={<UtentiList />} />
              <Route path="utenti/new" element={<UtenteForm />} />
              <Route path="utenti/:id" element={<UtenteDetail />} />
              <Route path="utenti/:id/edit" element={<UtenteForm />} />

              {/* Progetti Routes */}
              <Route path="progetti" element={<ProgettiList />} />
              <Route path="progetti/new" element={<ProgettoForm />} />
              <Route path="progetti/:id/edit" element={<ProgettoForm />} />

              {/* Clienti Routes */}
              <Route path="clienti" element={<ClientiList />} />
              <Route path="clienti/new" element={<ClienteForm />} />
              <Route path="clienti/:id/edit" element={<ClienteForm />} />

              {/* Gantt Allocazione Routes */}
              <Route path="gantt-allocazione" element={<GanttAllocazione />} />
              <Route path="gantt-allocazione-progetti" element={<GanttAllocazioneProgetti />} />

              {/* GitLab Statistics Routes */}
              <Route path="gitlab" element={<GitLabStats />} />

              {/* Certificazioni Routes */}
              <Route path="certificazioni" element={<CertificazioniList />} />

              {/* Curricula Routes */}
              <Route path="curricula" element={<CurriculaList />} />
              <Route path="curricula/new" element={<CurriculumForm />} />
              <Route path="curricula/:id" element={<CurriculumDetail />} />
              <Route path="curricula/:id/edit" element={<CurriculumForm />} />

              {/* Configurazione - Tipi Utente Routes */}
              <Route path="configurazione/tipi-utente" element={<TipiUtenteList />} />
              <Route path="configurazione/tipi-utente/new" element={<TipoUtenteForm />} />
              <Route path="configurazione/tipi-utente/:id/edit" element={<TipoUtenteForm />} />

              {/* Configurazione - Job Titles Routes */}
              <Route path="configurazione/job-titles" element={<JobTitlesList />} />
              <Route path="configurazione/job-titles/new" element={<JobTitleForm />} />
              <Route path="configurazione/job-titles/:id/edit" element={<JobTitleForm />} />

              {/* Configurazione - Tipi Contatto Routes */}
              <Route path="configurazione/tipi-contatto" element={<TipiContattoList />} />
              <Route path="configurazione/tipi-contatto/new" element={<TipoContattoForm />} />
              <Route path="configurazione/tipi-contatto/:id/edit" element={<TipoContattoForm />} />

              {/* Configurazione - Skills Routes */}
              <Route path="configurazione/skills" element={<SkillsList />} />

              {/* Configurazione - Soft Skills Routes */}
              <Route path="configurazione/soft-skills" element={<SoftSkillsList />} />

              {/* Configurazione - Stati Progetto Routes */}
              <Route path="configurazione/stati-progetto" element={<StatiProgettoList />} />
              <Route path="configurazione/stati-progetto/new" element={<StatoProgettoForm />} />
              <Route path="configurazione/stati-progetto/:id/edit" element={<StatoProgettoForm />} />

              {/* Configurazione - Profili Routes */}
              <Route path="configurazione/profili" element={<ProfiliList />} />
              <Route path="configurazione/profili/new" element={<ProfiloForm />} />
              <Route path="configurazione/profili/:id/edit" element={<ProfiloForm />} />
              </Route>
            </Routes>
          </BrowserRouter>
        </QueryClientProvider>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
