package com.example.demo.service;

import com.example.demo.model.Prodotto;
import com.example.demo.model.Movimento;
import com.example.demo.repository.ProdottoRepository;
import com.example.demo.repository.MovimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MagazzinoService {

    private final ProdottoRepository prodottoRepository;

    private final MovimentoRepository movimentoRepository;

    public MagazzinoService(ProdottoRepository prodottoRepository, MovimentoRepository movimentoRepository) {
        this.prodottoRepository = prodottoRepository;
        this.movimentoRepository = movimentoRepository;
    }

    public List<Prodotto> trovaTutti() {
        return prodottoRepository.findAll();
    }

    public long contaProdottiTotali() {
        return prodottoRepository.findAll().stream().mapToInt(Prodotto::getQuantita).sum();
    }

    // Restituisce una mappa: Nome Prodotto -> Somma Quantità Rimosse (assolute)
    public Map<String, Integer> getDettaglioUscite() {
        return movimentoRepository.findAll().stream()
                .filter(m -> m.getTipo() == Movimento.TipoMovimento.SCARICO
                        || m.getTipo() == Movimento.TipoMovimento.RESO_FORNITORE)
                .collect(Collectors.groupingBy(
                        m -> m.getProdotto().getNome(),
                        Collectors.summingInt(m -> Math.abs(m.getQuantita()))));
    }

    // Restituisce una mappa: Nome Prodotto -> Somma Quantità Aggiunte
    public Map<String, Integer> getDettaglioEntrate() {
        return movimentoRepository.findAll().stream()
                .filter(m -> m.getTipo() == Movimento.TipoMovimento.CARICO
                        || m.getTipo() == Movimento.TipoMovimento.RESO_CLIENTE)
                .collect(Collectors.groupingBy(
                        m -> m.getProdotto().getNome(),
                        Collectors.summingInt(m -> Math.abs(m.getQuantita()))));
    }

    public Prodotto getPrimoInScadenza() {
        return prodottoRepository.findAll().stream()
                .min(Comparator.comparing(Prodotto::getScadenza))
                .orElse(null);
    }

    @Transactional
    public void registraMovimento(Long prodottoId, int qta, Movimento.TipoMovimento tipo, String note) {
        Prodotto p = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        int variazione = qta;
        if (tipo == Movimento.TipoMovimento.SCARICO || tipo == Movimento.TipoMovimento.RESO_FORNITORE) {
            if (p.getQuantita() < qta) {
                throw new RuntimeException("Giacenza insufficiente!");
            }
            variazione = -qta;
        }

        p.setQuantita(p.getQuantita() + variazione);
        prodottoRepository.save(p);

        Movimento m = new Movimento();
        m.setProdotto(p);
        m.setQuantita(variazione);
        m.setTipo(tipo);
        m.setDataMovimento(LocalDateTime.now());
        m.setNote(note);
        movimentoRepository.save(m);
    }
}