import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import type { Curriculum } from '../types/index.js';

export const exportCurriculumToPDF = async (curriculum: Curriculum) => {
  const doc = new jsPDF();
  const pageWidth = doc.internal.pageSize.getWidth();

  // Header
  doc.setFontSize(20);
  doc.setTextColor(26, 26, 46);
  doc.text('CURRICULUM VITAE', pageWidth / 2, 20, { align: 'center' });

  // User info
  if (curriculum.utente) {
    doc.setFontSize(16);
    doc.setTextColor(0, 212, 170);
    doc.text(
      `${curriculum.utente.nome} ${curriculum.utente.cognome}`,
      pageWidth / 2,
      35,
      { align: 'center' }
    );

    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100);
    let yPos = 45;
    if (curriculum.utente.email) {
      doc.text(`Email: ${curriculum.utente.email}`, 20, yPos);
      yPos += 6;
    }
    if (curriculum.utente.telefono) {
      doc.text(`Telefono: ${curriculum.utente.telefono}`, 20, yPos);
      yPos += 6;
    }
    if (curriculum.utente.ruolo) {
      doc.text(`Ruolo: ${curriculum.utente.ruolo}`, 20, yPos);
      yPos += 10;
    } else {
      yPos += 4;
    }

    // Skills
    if (curriculum.skills && curriculum.skills.length > 0) {
      doc.setFontSize(14);
      doc.setTextColor(26, 26, 46);
      doc.text('COMPETENZE TECNICHE', 20, yPos);
      yPos += 8;

      const skillsData = curriculum.skills.map((levelSkill) => [
        levelSkill.skill.nome,
        levelSkill.skill.categoria || '-',
        levelSkill.livello ? `${levelSkill.livello}/10` : '-',
      ]);

      autoTable(doc, {
        startY: yPos,
        head: [['Skill', 'Categoria', 'Livello']],
        body: skillsData,
        theme: 'striped',
        headStyles: { fillColor: [0, 212, 170] },
      });

      yPos = (doc as any).lastAutoTable.finalY + 10;
    }

    // Certificazioni
    if (curriculum.certificazioni && curriculum.certificazioni.length > 0) {
      if (yPos > 250) {
        doc.addPage();
        yPos = 20;
      }

      doc.setFontSize(14);
      doc.setTextColor(26, 26, 46);
      doc.text('CERTIFICAZIONI', 20, yPos);
      yPos += 8;

      const certsData = curriculum.certificazioni.map((cert) => [
        cert.nome,
        cert.ente || '-',
        cert.dataOttenimento
          ? new Date(cert.dataOttenimento).toLocaleDateString('it-IT')
          : '-',
      ]);

      autoTable(doc, {
        startY: yPos,
        head: [['Certificazione', 'Ente', 'Data Ottenimento']],
        body: certsData,
        theme: 'striped',
        headStyles: { fillColor: [0, 212, 170] },
      });

      yPos = (doc as any).lastAutoTable.finalY + 10;
    }

    // Esperienze
    if (curriculum.esperienze && curriculum.esperienze.length > 0) {
      if (yPos > 250) {
        doc.addPage();
        yPos = 20;
      }

      doc.setFontSize(14);
      doc.setTextColor(26, 26, 46);
      doc.text('ESPERIENZE LAVORATIVE', 20, yPos);
      yPos += 8;

      curriculum.esperienze.forEach((exp) => {
        if (yPos > 260) {
          doc.addPage();
          yPos = 20;
        }

        doc.setFontSize(12);
        doc.setTextColor(0, 0, 0);
        doc.text(`${exp.ruolo} - ${exp.azienda}`, 20, yPos);
        yPos += 6;

        doc.setFontSize(10);
        doc.setTextColor(100, 100, 100);
        const periodo = `${new Date(exp.dataInizio).toLocaleDateString('it-IT')} - ${
          exp.dataFine ? new Date(exp.dataFine).toLocaleDateString('it-IT') : 'Presente'
        }`;
        doc.text(periodo, 20, yPos);
        yPos += 6;

        if (exp.tecnologie) {
          doc.text(`Tecnologie: ${exp.tecnologie}`, 20, yPos);
          yPos += 6;
        }

        if (exp.descrizione) {
          const splitDesc = doc.splitTextToSize(exp.descrizione, pageWidth - 40);
          doc.text(splitDesc, 20, yPos);
          yPos += splitDesc.length * 5 + 4;
        }

        yPos += 4;
      });
    }
  }

  // Footer
  const fileName = curriculum.utente
    ? `CV_${curriculum.utente.cognome}_${curriculum.utente.nome}.pdf`
    : `CV_${curriculum.id}.pdf`;

  doc.save(fileName);
};
