import React, { useState } from 'react';
import { Filter, X } from 'lucide-react';
import Button from './Button';
import Modal from './Modal';
import './AdvancedFilters.css';

export interface FilterField {
  name: string;
  label: string;
  type: 'text' | 'select' | 'date' | 'number' | 'boolean';
  options?: { value: string; label: string }[];
}

interface AdvancedFiltersProps {
  fields: FilterField[];
  onApply: (filters: Record<string, any>) => void;
  onReset: () => void;
}

const AdvancedFilters: React.FC<AdvancedFiltersProps> = ({ fields, onApply, onReset }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [filters, setFilters] = useState<Record<string, any>>({});
  const [activeFiltersCount, setActiveFiltersCount] = useState(0);

  const handleChange = (name: string, value: any) => {
    setFilters((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleApply = () => {
    const activeFilters = Object.entries(filters).filter(([_, value]) => {
      if (typeof value === 'string') return value.trim() !== '';
      if (typeof value === 'boolean') return true;
      return value !== null && value !== undefined;
    });

    setActiveFiltersCount(activeFilters.length);
    onApply(Object.fromEntries(activeFilters));
    setIsOpen(false);
  };

  const handleReset = () => {
    setFilters({});
    setActiveFiltersCount(0);
    onReset();
    setIsOpen(false);
  };

  const renderField = (field: FilterField) => {
    const value = filters[field.name] || '';

    switch (field.type) {
      case 'text':
        return (
          <input
            type="text"
            value={value}
            onChange={(e) => handleChange(field.name, e.target.value)}
            placeholder={`Filtra per ${field.label.toLowerCase()}`}
          />
        );

      case 'number':
        return (
          <input
            type="number"
            value={value}
            onChange={(e) => handleChange(field.name, e.target.value)}
            placeholder={`Filtra per ${field.label.toLowerCase()}`}
          />
        );

      case 'date':
        return (
          <input
            type="date"
            value={value}
            onChange={(e) => handleChange(field.name, e.target.value)}
          />
        );

      case 'select':
        return (
          <select
            value={value}
            onChange={(e) => handleChange(field.name, e.target.value)}
          >
            <option value="">Tutti</option>
            {field.options?.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        );

      case 'boolean':
        return (
          <select
            value={value === true ? 'true' : value === false ? 'false' : ''}
            onChange={(e) =>
              handleChange(
                field.name,
                e.target.value === '' ? null : e.target.value === 'true'
              )
            }
          >
            <option value="">Tutti</option>
            <option value="true">Sì</option>
            <option value="false">No</option>
          </select>
        );

      default:
        return null;
    }
  };

  return (
    <>
      <div className="advanced-filters-trigger">
        <Button
          variant="secondary"
          icon={<Filter size={20} />}
          onClick={() => setIsOpen(true)}
        >
          Filtri Avanzati
          {activeFiltersCount > 0 && (
            <span className="filter-badge">{activeFiltersCount}</span>
          )}
        </Button>
        {activeFiltersCount > 0 && (
          <Button variant="secondary" icon={<X size={20} />} onClick={handleReset}>
            Rimuovi Filtri
          </Button>
        )}
      </div>

      <Modal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        title="Filtri Avanzati"
        footer={
          <>
            <Button variant="secondary" onClick={handleReset}>
              Resetta
            </Button>
            <Button variant="primary" onClick={handleApply}>
              Applica Filtri
            </Button>
          </>
        }
      >
        <div className="filters-form">
          {fields.map((field) => (
            <div key={field.name} className="form-group">
              <label>{field.label}</label>
              {renderField(field)}
            </div>
          ))}
        </div>
      </Modal>
    </>
  );
};

export default AdvancedFilters;
