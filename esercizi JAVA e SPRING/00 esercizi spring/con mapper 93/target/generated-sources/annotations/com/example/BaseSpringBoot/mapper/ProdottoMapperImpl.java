package com.example.BaseSpringBoot.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

import com.example.ConMapper.dto.ProdottoDTO;
import com.example.ConMapper.entity.Prodotto;
import com.example.ConMapper.mapper.ProdottoMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-05T16:13:12+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    @Override
    public ProdottoDTO toDto(Prodotto p) {
        if ( p == null ) {
            return null;
        }

        ProdottoDTO prodottoDTO = new ProdottoDTO();

        prodottoDTO.setNomeProdotto( p.getNome() );

        prodottoDTO.setPrezzoInfo( p.getPrezzo() + " Euro" );

        return prodottoDTO;
    }
}
