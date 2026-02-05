package com.example.ConMapper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ConMapper.dto.ProdottoDTO;
import com.example.ConMapper.entity.Prodotto;

@Mapper(componentModel = "spring")
public interface ProdottoMapper {
  @Mapping(source = "nome", target = "nomeProdotto")
  @Mapping(target = "prezzoInfo", expression = "java(p.getPrezzo() + \" Euro\")")
  ProdottoDTO toDto(Prodotto p);
}