package com.example.demo.controller;

import com.example.demo.model.Prodotto;
import com.example.demo.model.Movimento;
import com.example.demo.service.MagazzinoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

@RestController
@RequestMapping("/prodotti")
public class MagazzinoController {

        private final MagazzinoService service;

        public MagazzinoController(MagazzinoService service) {
                this.service = service;
        }

        @GetMapping(produces = "text/plain;charset=UTF-8")
        public String getReportCompleto() {
                StringBuilder sb = new StringBuilder();

                // Recupero dati dal service
                Map<String, Integer> usciteDettaglio = service.getDettaglioUscite();
                Map<String, Integer> entrateDettaglio = service.getDettaglioEntrate();
                Prodotto inScadenza = service.getPrimoInScadenza();
                List<Prodotto> tuttiIProdotti = service.trovaTutti();

                sb.append("il magazzino contiene un totale di ").append(service.contaProdottiTotali())
                                .append(" articoli").append("\n\n");

                // Sezione RIMOSSI
                sb.append("numero operazioni di rimozione: ").append(usciteDettaglio.size()).append("\n");
                usciteDettaglio.forEach((nome, qta) -> sb.append("Rimosso articolo ").append(nome).append(" quantità ")
                                .append(qta).append("\n"));
                sb.append("\n");

                // Sezione AGGIUNTI
                sb.append("numero operazioni di aggiunta: ").append(entrateDettaglio.size()).append("\n");
                entrateDettaglio.forEach(
                                (nome, qta) -> sb.append("Aggiunto articolo ").append(nome).append(" quantità ")
                                                .append(qta).append("\n"));
                sb.append("\n");

                if (inScadenza != null) {
                        sb.append("primo prodotto in scadenza ").append(inScadenza.getScadenza())
                                        .append(" (").append(inScadenza.getNome()).append(")\n\n");
                }

                // --- PRIMA VISUALIZZAZIONE: ORDINE ALFABETICO CRESCENTE (A-Z) ---
                sb.append("--- ELENCO PRODOTTI (ORDINE ALFABETICO) ---\n\n");
                tuttiIProdotti.stream()
                                .sorted(Comparator.comparing(Prodotto::getNome))
                                .forEach(p -> appendRigaProdotto(sb, p));

                sb.append("\n");

                // --- SECONDA VISUALIZZAZIONE: ORDINE SCADENZA DECRESCENTE (Più lontana -> Più
                // vicina) ---
                sb.append("--- ELENCO PRODOTTI (ORDINE SCADENZA DECRESCENTE) ---\n\n");
                tuttiIProdotti.stream()
                                .sorted(Comparator.comparing(Prodotto::getScadenza).reversed())
                                .forEach(p -> appendRigaProdotto(sb, p));

                return sb.toString();
        }

        /**
         * Metodo helper professionale per evitare di ripetere il codice di stampa
         */
        private void appendRigaProdotto(StringBuilder sb, Prodotto p) {
                sb.append("ID: ").append(p.getId())
                                .append(" | Nome: ").append(String.format("%-20s", p.getNome())) // Allineamento testo
                                .append(" | Qta: ").append(p.getQuantita())
                                .append(" | Scadenza: ").append(p.getScadenza()).append("\n");
        }

        @PostMapping("/{id}/movimento")
        public String creaMovimento(@PathVariable Long id,
                        @RequestParam int qta,
                        @RequestParam Movimento.TipoMovimento tipo,
                        @RequestParam(required = false) String note) {
                service.registraMovimento(id, qta, tipo, note);
                return "Movimento di tipo " + tipo + " registrato con successo!";
        }
}