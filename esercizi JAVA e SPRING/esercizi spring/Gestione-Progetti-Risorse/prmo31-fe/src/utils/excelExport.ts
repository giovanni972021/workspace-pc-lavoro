import * as XLSX from 'xlsx';
import type { Curriculum } from '../types/index.js';

export const exportCurriculumToExcel = (curriculum: Curriculum) => {
  const workbook = XLSX.utils.book_new();

  // Sheet 1: Informazioni generali
  const infoData = [];
  if (curriculum.utente) {
    infoData.push(['Nome', curriculum.utente.nome]);
    infoData.push(['Cognome', curriculum.utente.cognome]);
    infoData.push(['Email', curriculum.utente.email]);
    infoData.push(['Telefono', curriculum.utente.telefono || '']);
    infoData.push(['Ruolo', curriculum.utente.ruolo || '']);
  }

  const infoSheet = XLSX.utils.aoa_to_sheet(infoData);
  XLSX.utils.book_append_sheet(workbook, infoSheet, 'Informazioni');

  // Sheet 2: Skills
  if (curriculum.skills && curriculum.skills.length > 0) {
    const skillsData = curriculum.skills.map((levelSkill) => ({
      Nome: levelSkill.skill.nome,
      Categoria: levelSkill.skill.categoria || '',
      Livello: levelSkill.livello || '',
      Descrizione: levelSkill.skill.descrizione || '',
    }));
    const skillsSheet = XLSX.utils.json_to_sheet(skillsData);
    XLSX.utils.book_append_sheet(workbook, skillsSheet, 'Skills');
  }

  // Sheet 3: Certificazioni
  if (curriculum.certificazioni && curriculum.certificazioni.length > 0) {
    const certsData = curriculum.certificazioni.map((cert) => ({
      Nome: cert.nome,
      Ente: cert.ente || '',
      'Data Ottenimento': cert.dataOttenimento
        ? new Date(cert.dataOttenimento).toLocaleDateString('it-IT')
        : '',
      'Data Scadenza': cert.dataScadenza
        ? new Date(cert.dataScadenza).toLocaleDateString('it-IT')
        : '',
      Descrizione: cert.descrizione || '',
    }));
    const certsSheet = XLSX.utils.json_to_sheet(certsData);
    XLSX.utils.book_append_sheet(workbook, certsSheet, 'Certificazioni');
  }

  // Sheet 4: Esperienze
  if (curriculum.esperienze && curriculum.esperienze.length > 0) {
    const expData = curriculum.esperienze.map((exp) => ({
      Azienda: exp.azienda,
      Ruolo: exp.ruolo,
      'Data Inizio': new Date(exp.dataInizio).toLocaleDateString('it-IT'),
      'Data Fine': exp.dataFine
        ? new Date(exp.dataFine).toLocaleDateString('it-IT')
        : 'Presente',
      Tecnologie: exp.tecnologie || '',
      Descrizione: exp.descrizione || '',
    }));
    const expSheet = XLSX.utils.json_to_sheet(expData);
    XLSX.utils.book_append_sheet(workbook, expSheet, 'Esperienze');
  }

  const fileName = curriculum.utente
    ? `CV_${curriculum.utente.cognome}_${curriculum.utente.nome}.xlsx`
    : `CV_${curriculum.id}.xlsx`;

  XLSX.writeFile(workbook, fileName);
};

export const exportToExcel = <T extends Record<string, any>>(
  data: T[],
  filename: string,
  sheetName: string = 'Data'
) => {
  const workbook = XLSX.utils.book_new();
  const worksheet = XLSX.utils.json_to_sheet(data);
  XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);
  XLSX.writeFile(workbook, `${filename}.xlsx`);
};
