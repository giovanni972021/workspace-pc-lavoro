import React, { useState } from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import {
  Users,
  Briefcase,
  Building2,
  Award,
  FileText,
  Menu,
  X,
  LayoutDashboard,
  Moon,
  Sun,
  Settings,
  ChevronDown,
  ChevronRight,
  Calendar,
  LogOut,
  GitBranch,
} from 'lucide-react';
import { useTheme } from '../contexts/ThemeContext';
import { useAuth } from '../contexts/AuthContext';
import './MainLayout.css';

interface MenuItem {
  path?: string;
  icon: React.ComponentType<{ size?: number }>;
  label: string;
  submenu?: { path: string; label: string }[];
}

const MainLayout: React.FC = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [configMenuOpen, setConfigMenuOpen] = useState(false);
  const location = useLocation();
  const { theme, toggleTheme } = useTheme();
  const { logout, currentUser, isSuperUser } = useAuth();

  const menuItems: MenuItem[] = [
    { path: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
    { path: '/utenti', icon: Users, label: 'Utenti' },
    { path: '/progetti', icon: Briefcase, label: 'Progetti' },
    { path: '/clienti', icon: Building2, label: 'Clienti' },
    { path: '/gantt-allocazione', icon: Calendar, label: 'Allocazione Utenti' },
    { path: '/gantt-allocazione-progetti', icon: Calendar, label: 'Allocazione Progetti' },
    { path: '/gitlab', icon: GitBranch, label: 'GitLab Stats' },
    { path: '/certificazioni', icon: Award, label: 'Certificazioni' },
    { path: '/curricula', icon: FileText, label: 'Curricula' },
    {
      icon: Settings,
      label: 'Configurazione',
      submenu: [
        { path: '/configurazione/skills', label: 'Skill' },
        { path: '/configurazione/soft-skills', label: 'Soft Skill' },
        { path: '/configurazione/tipi-utente', label: 'Tipo Utente' },
        { path: '/configurazione/job-titles', label: 'Ruolo Professionale' },
        { path: '/configurazione/tipi-contatto', label: 'Tipo Contatto' },
        { path: '/configurazione/stati-progetto', label: 'Stato Progetto' },
        { path: '/configurazione/profili', label: 'Profili' },
      ],
    },
  ];

  const isActive = (path: string) => location.pathname.startsWith(path);

  return (
    <div className="main-layout">
      {/* Sidebar */}
      <aside className={`sidebar ${sidebarOpen ? 'open' : 'closed'}`}>
        <div className="sidebar-header">
          <img
            src="/logo-schema31.png"
            alt="Schema31"
            className="logo"
          />
          {sidebarOpen && (
            <h1 className="app-title">Gestione delle risorse e progetti</h1>
          )}
        </div>

        <nav className="sidebar-nav">
          {menuItems.map((item, index) => {
            const Icon = item.icon;

            // Menu item with submenu
            if (item.submenu) {
              const isConfigActive = item.submenu.some(sub => location.pathname.startsWith(sub.path));
              const isOpen = configMenuOpen || isConfigActive;

              return (
                <div key={`menu-${index}`} className="nav-item-wrapper">
                  <div
                    className={`nav-item ${isConfigActive ? 'active' : ''}`}
                    onClick={() => setConfigMenuOpen(!configMenuOpen)}
                    style={{ cursor: 'pointer' }}
                  >
                    <Icon size={20} />
                    {sidebarOpen && <span>{item.label}</span>}
                    {sidebarOpen && (
                      <span style={{ marginLeft: 'auto' }}>
                        {isOpen ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                      </span>
                    )}
                  </div>
                  {sidebarOpen && isOpen && (
                    <div className="submenu">
                      {item.submenu.map((subItem) => (
                        <Link
                          key={subItem.path}
                          to={subItem.path}
                          className={`submenu-item ${isActive(subItem.path) ? 'active' : ''}`}
                        >
                          {subItem.label}
                        </Link>
                      ))}
                    </div>
                  )}
                </div>
              );
            }

            // Regular menu item
            return (
              <Link
                key={item.path}
                to={item.path!}
                className={`nav-item ${isActive(item.path!) ? 'active' : ''}`}
              >
                <Icon size={20} />
                {sidebarOpen && <span>{item.label}</span>}
              </Link>
            );
          })}
        </nav>
      </aside>

      {/* Main Content */}
      <div className="main-content">
        <header className="top-header">
          <button
            className="toggle-sidebar"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            {sidebarOpen ? <X size={24} /> : <Menu size={24} />}
          </button>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            {currentUser && (
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', fontSize: '0.85rem' }}>
                <span style={{ fontWeight: 'bold' }}>
                  {currentUser.nome} {currentUser.cognome}
                </span>
                <span style={{ fontSize: '0.75rem', opacity: 0.8 }}>
                  {currentUser.profilo || 'Nessun profilo'}
                  {isSuperUser() && ' (SUPER USER)'}
                </span>
              </div>
            )}
            <button
              className="toggle-theme"
              onClick={toggleTheme}
              title={theme === 'light' ? 'Attiva tema scuro' : 'Attiva tema chiaro'}
            >
              {theme === 'light' ? <Moon size={20} /> : <Sun size={20} />}
            </button>
            <button
              className="toggle-theme"
              onClick={logout}
              title="Esci"
            >
              <LogOut size={20} />
            </button>
          </div>
        </header>

        <main className="content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default MainLayout;
