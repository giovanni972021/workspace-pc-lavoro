package com.example.ConMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.ConMapper.dto.ProdottoDTO;
import com.example.ConMapper.entity.Prodotto;
import com.example.ConMapper.mapper.ProdottoMapper;

@SpringBootApplication
public class ConMapperApp implements CommandLineRunner {

    @Autowired
    private ProdottoMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(ConMapperApp.class, args);
    }

    @Override
    public void run(String... args) {
        Prodotto entity = new Prodotto("iPhone 15", 999.00);
        ProdottoDTO dto = mapper.toDto(entity);
        System.out.println("================================");
        System.out.println("RISULTATO MAPPER: " + dto);
        System.out.println("================================");
    }
}